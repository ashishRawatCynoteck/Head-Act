package com.cynoteck.demoheadsup;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class GameActivity extends FragmentActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    String names[] = {"Hera Pheri","Jannat", "Goolmall","OMG","Dhoom 3","3 idiots","Bhool bulaiya","Bhahubali","Rebel","Mahabharat","Kites","Bang Bang","Ra-One","1920 evil returns","Jajantram Mamantarm","Dream Girl","Saaho","Batla House","Mission Mangal","War","De De Pyaar De","Kabir Singh","The Tashkent Files","Super 30","Gully Boy","Total Dhamaal","Badla","Kesari","Uri: The Surgical Strike","Thackeray","Manikarnika: The Queen Of Jhansi"};
    public Intent resultIntent;
    MediaPlayer mpNew;
    MediaPlayer mpRed;
    MediaPlayer mpGreen;
    MediaPlayer mpTimeOut;
    MediaPlayer mpTick;
    MediaPlayer mpStart;
    RelativeLayout correctOverlay, passOverlay;
    Random randomInt = new Random();
    float[] mGravity;
    float[] mGeomagnetic;
    ArrayList skippedStrArr = new ArrayList();
    ArrayList<String> doneStrArr = new ArrayList();
    String[] presentStr = null;
    ArrayList<String> movie = new ArrayList();
    TextView timerText;
    TextView promptText, textViewText;
    TextView threeTwoOneText;
    private static long total;
    CountDownTimer gamePauseTime;
    CountDownTimer gameTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        soundInit();
        gameAndPauseTime();
        init();
//        sensorRegister();
        firstCountDownStart();

    }

    private void sensorRegister() {
        mSensorManager.registerListener(GameActivity.this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(GameActivity.this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void gameAndPauseTime() {
        gamePauseTime = new CountDownTimer(total, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                total = millisUntilFinished;
                timerText.setText(millisUntilFinished / 1000 +"");
            }

            @Override
            public void onFinish() {
                textViewText.setText("Time Over!");
                textViewText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                    resultIntent.putExtra("Skipped", skippedStrArr.size());
//                    resultIntent.putExtra("Correct", doneStrArr.size());
//                    resultIntent.putExtra("SkippedStrings", skippedStrArr.toArray());
//                    resultIntent.putExtra("CorrectStrings", doneStrArr.toArray());
//                    mContext.startActivity(resultIntent);
//                    skippedStrArr.clear();
//                    doneStrArr.clear();
                    }
                }, 1500);
                timerText.setVisibility(View.GONE);
            }
        };
        gameTime = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                total = millisUntilFinished;
                timerText.setText(millisUntilFinished / 1000 + "");
            }

            @Override
            public void onFinish() {
                mSensorManager.unregisterListener(GameActivity.this);
                textViewText.setText("Time Over!");
                mpTimeOut.start();
                textViewText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resultIntent = new Intent(GameActivity.this, ResultActivity.class);
                        resultIntent.putExtra("SkippedArrSize", skippedStrArr.size());
                        resultIntent.putExtra("CorrectArrSize", doneStrArr.size());
                        resultIntent.putExtra("SkippedStrings", skippedStrArr.toArray());
                        resultIntent.putExtra("CorrectStrings", doneStrArr);
                        Log.e("CorrectStrings",doneStrArr.toString());
                        Log.e("SkippedArrSize",skippedStrArr.toString());

                        startActivity(resultIntent);
//                    skippedStrArr.clear();
//                    doneStrArr.clear();
                    }
                }, 1500);
                timerText.setVisibility(View.GONE);
            }
        };
    }

    private void firstCountDownStart() {
        mSensorManager.unregisterListener(GameActivity.this);
        movie.addAll(Arrays.asList(names));
        Collections.shuffle(movie);
        presentStr = new String[]{movie.get(0)};
        Log.e("presentstr", String.valueOf(presentStr));
        movie.remove(0);
        textViewText.setText(presentStr[0]);
        promptText.setVisibility(View.VISIBLE);
        textViewText.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        threeTwoOneText.setVisibility(View.VISIBLE);
        skippedStrArr.clear();
        doneStrArr.clear();
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mpTick.start();
                threeTwoOneText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                sensorRegister();
                promptText.setVisibility(View.GONE);
                mpStart.start();
                threeTwoOneText.setText("Start!");
            }
        }.start();

        textViewText.postDelayed(new Runnable() {
            @Override
            public void run() {
                startGame();
            }
        }, 5000);
    }

    private void init() {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        textViewText = findViewById(R.id.textView);
        timerText = findViewById(R.id.timer);
        threeTwoOneText = findViewById(R.id.three_two_one_text);
        promptText = findViewById(R.id.promptText);
        correctOverlay = findViewById(R.id.correctOverlay);
        timerText = findViewById(R.id.timer);
        textViewText =  findViewById(R.id.textView);
        passOverlay = findViewById(R.id.passOverlay);


    }

    private void soundInit() {
        mpGreen = MediaPlayer.create(this, R.raw.green);
        mpRed = MediaPlayer.create(this, R.raw.red);
        mpNew = MediaPlayer.create(this, R.raw.newname);
        mpTimeOut = MediaPlayer.create(this, R.raw.timeout);
        mpTick = MediaPlayer.create(this, R.raw.tick);
        mpStart = MediaPlayer.create(this, R.raw.start);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            mGeomagnetic = event.values;
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            if ((z > 1) && (z < 9) && (x > 0 && x < 3) && (y > -3 && y < 3))
            {
                mpRed.setVolume(0.5f, 0.5f);
                mpRed.start();
                skippedStrArr.add(textViewText.getText().toString());
                passOverlay.setVisibility(View.VISIBLE);
                if ((z > 1) && (z < 9) && (x > 0 && x < 9) && (y > -3 && y < 3)) {
                    mSensorManager.unregisterListener(this);
//                    Toast.makeText(this, "Upward", Toast.LENGTH_SHORT).show();
                textViewText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sensorRegister();
                        presentStr[0] = movie.get(randomInt.nextInt(movie.size()));
                        passOverlay.setVisibility(View.GONE);
                        textViewText.setText(presentStr[0]);
                        mpNew.start();

                    }
                }, 1000);
                }


            } else if ((z > -9) && (z < 1) && (x > 0 && x < 3) && (y > -3 && y < 3)) {
                mpGreen.setVolume(0.5f, 0.5f);
                mpGreen.start();
                correctOverlay.setVisibility(View.VISIBLE);

//                    Toast.makeText(this, "Downward", Toast.LENGTH_SHORT).show();
                    if ((z > -9) && (z < 1) && (x > 0 && x < 9) && (y > -3 && y < 3)) {
//                        Toast.makeText(this, "Center", Toast.LENGTH_SHORT).show();
                        mSensorManager.unregisterListener(this);
                        Log.e("XXXXXXX", x+ "");
                        Log.e("YYYYYYY", y+ "");
                        Log.e("ZZZZZZZ", z+ "");
                        Collections.shuffle(movie);
                        presentStr[0] = movie.get(0).toString();
                        doneStrArr.add(presentStr[0]);
                        Log.e("done", presentStr[0]);
                        movie.remove(0);
                        textViewText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sensorRegister();
                                correctOverlay.setVisibility(View.GONE);
                                textViewText.setText(presentStr[0]);
                                mpNew.start();
                            }
                        }, 1000);
                    }


                }
            }


        }



    private void startGame() {
        threeTwoOneText.setVisibility(View.GONE);
        textViewText.setVisibility(View.VISIBLE);
        timerText.setVisibility(View.VISIBLE);
        promptText.setVisibility(View.GONE);
        gameTime.start();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onDestroy()
    {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
    }
    @Override
    public void onBackPressed()
    {
        mSensorManager.unregisterListener(this);
        super.onBackPressed();
    }
    @Override
    public void onResume()
    {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onResume();
    }
    @Override
    protected void onPause()
    {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }
}
