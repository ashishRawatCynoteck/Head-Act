package com.cynoteck.demoheadsup;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class GameActivity extends FragmentActivity implements SensorEventListener {

    private Camera mCamera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Button capture, switchCamera;
    private Context myContext;
    private LinearLayout cameraPreview;
    private boolean cameraFront = false;
    boolean recording = false;


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
    CountDownTimer fiveSecond;

    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        checkAllPermission();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        myContext = this;
        if (checkAllPermission()){
            cameraInitialize();
        }

        soundInit();
        gameAndPauseTime();
        init();
//        sensorRegister();
        firstCountDownStart();

    }
    private boolean checkAllPermission() {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
            int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED &&result2 == PackageManager.PERMISSION_GRANTED &&result3 == PackageManager.PERMISSION_GRANTED;

    }


    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }
    private void cameraInitialize() {
        cameraPreview = (LinearLayout) findViewById(R.id.camera_preview);

        mPreview = new CameraPreview(myContext, mCamera);
        cameraPreview.addView(mPreview);



    }
    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }
    private boolean prepareMediaRecorder() {

        mediaRecorder = new MediaRecorder();

        mCamera.unlock();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mediaRecorder.setOutputFile("/sdcard/myvideo.mp4");
        mediaRecorder.setMaxDuration(600000); // Set max duration 60 sec.
        mediaRecorder.setMaxFileSize(50000000); // Set max file size 50M

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            releaseMediaRecorder();
            return false;
        }
        return true;

    }


    private void releaseCamera() {
        // stop and release camera
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
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
                if (checkAllPermission()){
                    startStopSaveCamera();
                }
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
                if (checkAllPermission()){
                    startStopSaveCamera();}
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
            Log.e("XXXXXXX", x+ "");
            Log.e("YYYYYYY", y+ "");
            Log.e("ZZZZZZZ", z+ "");

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

                    Collections.shuffle(movie);
                    presentStr[0] = movie.get(0).toString();
                    doneStrArr.add(textViewText.getText().toString());
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

    private void startStopSaveCamera() {
        if (recording) {
            // stop recording and release camera
            mediaRecorder.stop(); // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            Toast.makeText(GameActivity.this, "Video captured!", Toast.LENGTH_LONG).show();
            recording = false;
        } else {
            if (!prepareMediaRecorder()) {
                Toast.makeText(GameActivity.this, "Fail in prepareMediaRecorder()!\n - Ended -", Toast.LENGTH_LONG).show();
                finish();
            }
            // work on UiThread for better performance
            runOnUiThread(new Runnable() {
                public void run() {
                    // If there are stories, add them to the table

                    try {
                        mediaRecorder.start();
                    } catch (final Exception ex) {
                        // Log.i("---","Exception in thread");
                    }
                }
            });

            recording = true;
        }
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


    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the back facing camera
        // get the number of cameras
        int numberOfCameras = Camera.getNumberOfCameras();
        // for every camera check
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    private int findFrontFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }
    @Override
    public void onResume()
    {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onResume();

        if (checkAllPermission()){
            if (!hasCamera(myContext)) {
                Toast toast = Toast.makeText(myContext, "Sorry, your phone does not have a camera!", Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
            if (mCamera == null) {
                // if the front facing camera does not exist
                if (findBackFacingCamera() < 0) {
                    Toast.makeText(this, "No front facing camera found.", Toast.LENGTH_LONG).show();
                    switchCamera.setVisibility(View.GONE);
                }
                mCamera = Camera.open(findFrontFacingCamera());
                mPreview.refreshCamera(mCamera);
            }
        }


    }
    @Override
    protected void onPause()
    {
       onBackPressed();
        super.onPause();

    }

    @Override
    protected void onStop() {
       onBackPressed();
        super.onStop();

    }

    @Override
    public void onBackPressed()
    {
        onGamePause();
        super.onBackPressed();
    }
    protected void onGamePause() {
        mSensorManager.unregisterListener(this);
        gameTime.cancel();
        gamePauseTime.cancel();
    }
}