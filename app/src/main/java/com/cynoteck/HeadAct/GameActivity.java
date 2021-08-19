package com.cynoteck.HeadAct;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
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
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class GameActivity extends FragmentActivity implements SensorEventListener, SurfaceHolder.Callback {
    boolean isRecording = false;
    private Camera camera;
    private CameraPreview mPreview;
    private MediaRecorder mediaRecorder;
    private Button capture, switchCamera;
    private Context myContext;
    private boolean cameraFront = false;
    private SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean recording = false;
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    String names[] = {"Paap Ko Jalakar Rakh Kar Dunga","Jal Bin Machhli Nritya Bin Bijli","Bandook Dahej Ke Seenay Par","Andheri Raat Mein Diya Tere Haath Mein","Dhoti Lota Aur Chowpatty","Allah Meharban To Gadha Pehalwan","Arvind Desai Ki Ajeeb Dastaan","Albert Pinto Ko Gussa Kyoon Aata Hai","Matru Ki Bijlee Ka Mandola","Luv Shuv Tey Chicken Khurana","Jajantaram Mamantaram","Stanley Ka Dabba","Basanti Ki Shadi Honeymoon Gabbar Ka ","Laali Ki Shaadi Mein Laddoo Deewana","The Playboy Mr. Sawhney ","Do Ladke Dono Kadke","Badhti Ka Naam Dadhi","Kaashi in Search of Ganga","Paharganj: The Little Amsterdam Of India","Majaz Ae Ghan-E-Dil Kya Karun","Chhota Bheem And The Throne of Bali","Murde Ki Jaan Khatre Mein","Salim Langde Pe Mat Ro","Howrah Bridge Pe Latki Laash","Bhediyon Ka Samooh","Hullabaloo Over Georgie and Bonnie’s Pictures","Saanch Ko Aanch Nahin ","Pappu Ki Pugdandi","Uttejana – The Fire","Kesar Kasturi","Chinar Daastan-E-Ishq ","Antarpravaah ","Chhappalled ","Phillauri ","Days of Tafree","Avgat ","Marudhar Express","Ganga Maang Rahi Balidan","Aaj Ka M.L.A. Ram Avtar","Aranyaka ","Putlibai ","Antarnaad ","Aarop","","Hera Pheri","Jannat", "Golmaal","Udaan","Dhoom 3","3 idiots","Bhool Bulaiya","Bhahubali","Rebel","Mahabharat","Kites","Bang Bang","Ra-One","1920 evil returns","Jajantram Mamantarm","Dream Girl","Saaho","Batla House","Mission Mangal","War","De De Pyaar De","Kabir Singh","The Tashkent Files","Super 30","Gully Boy","Total Dhamaal","Badla","Kesari","Uri: The Surgical Strike","Thackeray","Manikarnika: The Queen Of Jhansi","Band Baaja Baaraat","Ishqiya","Zindagi Na Milegi Dobara","Rockstar","Delhi Belly","Barfi!","Kahaani","Gangs of Wasseypur ","Vicky Donor","English Vinglish","Lootera","The Lunchbox","Highway","Queen","Haider","Piku","NH10","Masaan","Aligarh","Kapoor & Sons","Nil Battey Sannata","Udta Punjab","A Death in the Gunj","Newton","Andhadhun","October","Gully Boy","Article 15","Mard Ko Dard Nahi Hota","Dangal","Mukti Bhawan","Badhaai Ho","Ankhon Dekhi","Titli","Tumbbad","Ship of Theseus","Dil Dhadakne Do","Go Goa Gone","Dhobi Ghat","Raazi","Dilwale Dulhania Le Jayenge","Pyaasa","Insaf Ka Tarazu","Love Aaj Kal","Veer-Zaara","Mrs. Serial Killer","Fanaa","Panga ","Mother India","Dil Chahta Hai","Lagaan ","Sholay ","Jab We Met ","Lust Stories","Jodhaa Akbar","Mary Kom","Haider ","Kabhi Khushi Kabhie Gham","Khoobsurat ","Ek Tha Tiger","Devdas ","Hichki"," Main Prem Ki Diwani Hoon","Neerja ","Bajirao Mastani","Ek Ladki Ko Dekha Toh Aisa Laga","Bodyguard ","The White Tiger","The Dirty Picture","Is Love Enough? Sir","Saina ","Thapped "};
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
    InterstitialAd mInterstitialAd;

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
        interStitialAd();

    }

    private void interStitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.inter_ads_unit_Id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    private boolean checkAllPermission() {
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
            int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
            int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED &&result2 == PackageManager.PERMISSION_GRANTED &&result3 == PackageManager.PERMISSION_GRANTED;

    }

/*
    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    private boolean hasCamera(Context context) {
        // check if the device has camera
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }*/
    private void cameraInitialize() {
        surfaceView = findViewById(R.id.sufaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback( this);



    }
/*    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            mCamera.lock(); // lock camera for later use
        }
    }*/
@RequiresApi(api = Build.VERSION_CODES.O)
private boolean prepareForVedioRecording(){
    camera.unlock();

    mediaRecorder = new MediaRecorder();
    mediaRecorder.setCamera(camera);
    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
    mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));
    mediaRecorder.setOutputFile(getOutputFile(MEDIA_TYPE_VIDEO));

    mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
    mediaRecorder.setVideoSize(1920,1080);

    try {
        mediaRecorder.prepare();
    } catch (IOException e) {
        e.printStackTrace();
        releaseMediaRecorder();
        return false;
    }
    return true;

}

    private void releaseMediaRecorder() {
        if (mediaRecorder!=null){
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder=null;
            camera.lock();
        }
    }
    private String currentTimeStamp(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyMMdd_HH_mm_ss");
        String currentTime = simpleDateFormat.format(new Date());
        return  currentTime;
    }

    private File getOutputFile(int mediaTypeVideo) {

        File dir = (Environment.getExternalStorageDirectory());
        String timeStamp = currentTimeStamp();

        if (mediaTypeVideo==MEDIA_TYPE_VIDEO){
            File  filepath = new File(dir.getPath() + File.separator + "VID"+timeStamp+".mp4");
/*
            ContentValues values = new ContentValues();
*/
            MediaScannerConnection.scanFile(this, new String[] { filepath.getPath() }, new String[] { "video/mp4" }, null);
           /* values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            values.put(MediaStore.Images.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.MediaColumns.DATA,filepath.toString());
            getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/
            Log.e("fileee", filepath.toString());
            return filepath;

        }else {return null;}

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
        gameTime = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                total = millisUntilFinished;
                timerText.setText(millisUntilFinished / 1000 + "");
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {

                if (checkAllPermission()){
                    if (isRecording){
                        mediaRecorder.stop();
                        releaseMediaRecorder();
                        camera.lock();
                        isRecording = false;
                        Toast.makeText(GameActivity.this, "Game Over !", Toast.LENGTH_LONG).show();
                    }else {
                        if (prepareForVedioRecording()){
                            mediaRecorder.start();
                            isRecording = true;
                        }
                    }
                }
                mSensorManager.unregisterListener(GameActivity.this);
                textViewText.setText("Time Over!");
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    mInterstitialAd.setAdListener(new AdListener() {
                        public void onAdLoaded() {

                        }
                        @Override
                        public void onAdClosed() {

                            resultIntent = new Intent(GameActivity.this, ResultActivity.class);
                            resultIntent.putExtra("SkippedArrSize", skippedStrArr.size());
                            resultIntent.putExtra("CorrectArrSize", doneStrArr.size());
                            resultIntent.putExtra("SkippedStrings", skippedStrArr.toArray());
                            resultIntent.putExtra("CorrectStrings", doneStrArr);
                            Log.e("CorrectStrings",doneStrArr.toString());
                            Log.e("SkippedArrSize",skippedStrArr.toString());

                            startActivity(resultIntent);

                        }
                    });
                }else {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    resultIntent = new Intent(GameActivity.this, ResultActivity.class);
                    resultIntent.putExtra("SkippedArrSize", skippedStrArr.size());
                    resultIntent.putExtra("CorrectArrSize", doneStrArr.size());
                    resultIntent.putExtra("SkippedStrings", skippedStrArr.toArray());
                    resultIntent.putExtra("CorrectStrings", doneStrArr);
                    Log.e("CorrectStrings",doneStrArr.toString());
                    Log.e("SkippedArrSize",skippedStrArr.toString());

                    startActivity(resultIntent);
                }
                mpTimeOut.start();

                timerText.setVisibility(View.GONE);
            }// textViewText.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        resultIntent = new Intent(GameActivity.this, ResultActivity.class);
//                        resultIntent.putExtra("SkippedArrSize", skippedStrArr.size());
//                        resultIntent.putExtra("CorrectArrSize", doneStrArr.size());
//                        resultIntent.putExtra("SkippedStrings", skippedStrArr.toArray());
//                        resultIntent.putExtra("CorrectStrings", doneStrArr);
//                        Log.e("CorrectStrings",doneStrArr.toString());
//                        Log.e("SkippedArrSize",skippedStrArr.toString());
//
//                        startActivity(resultIntent);
////                    skippedStrArr.clear();
////                    doneStrArr.clear();
//                    }
//                }, 1500);
        };


    }

    private void firstCountDownStart() {
        Log.e("Size",Arrays.asList(names).size()+"");
        mSensorManager.unregisterListener(GameActivity.this);
        movie.addAll(Arrays.asList(names));
        Collections.shuffle(movie);
        presentStr = new String[]{movie.get(0)};
        movie.remove(0);
        textViewText.setText(presentStr[0]);
        promptText.setVisibility(View.VISIBLE);
        textViewText.setVisibility(View.GONE);
        timerText.setVisibility(View.GONE);
        threeTwoOneText.setVisibility(View.VISIBLE);
        skippedStrArr.clear();
        doneStrArr.clear();
        fiveSecond = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mpTick.start();
                threeTwoOneText.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                if (checkAllPermission()){
                    if (isRecording){
                        mediaRecorder.stop();
                        releaseMediaRecorder();
                        camera.lock();
                        isRecording = false;
                    }else {
                        if (prepareForVedioRecording()){
                            mediaRecorder.start();
                            isRecording = true;
                        }
                    }
                }
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
//            Log.e("XXXXXXX", x+ "");
//            Log.e("YYYYYYY", y+ "");
//            Log.e("ZZZZZZZ", z+ "");

            if ((z > 1) && (z < 9) && (x > 0 && x < 3) && (y > -3 && y < 3))
            {
                mpRed.setVolume(0.5f, 0.5f);
                mpRed.start();
                skippedStrArr.add(textViewText.getText().toString());
                passOverlay.setVisibility(View.VISIBLE);
                if ((z > 1) && (z < 9) && (x > 0 && x < 9) && (y > -3 && y < 3)) {
                    mSensorManager.unregisterListener(this);
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

                if ((z > -9) && (z < 1) && (x > 0 && x < 9) && (y > -3 && y < 3)) {
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onDestroy()
    {
        mSensorManager.unregisterListener(this);
        super.onDestroy();
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
        fiveSecond.cancel();
        gameTime.cancel();
        gamePauseTime.cancel();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open(findFrontFacingCamera());
        }catch (Exception e){

        }Camera.Parameters parameters;

        parameters = camera.getParameters();
        parameters.setPreviewSize(352,288);
        parameters.setPreviewFrameRate(20);
        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}