package com.benjamin.android.shake15;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.hardware.SensorManager;
import android.os.Bundle;
import android .app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class GameActivity extends Activity implements SensorEventListener {

    //public static final String EXTRA_MESSAGE = "com.benjamin.android.myapplication.MESSAGE";
    //public static final String EXTRA_VALUE = "com.benjamin.android.myapplication.VALUE";


    double maxX = 0;
    boolean toShow = true;
    int currentObjective = 0;
    long timeStart = SystemClock.uptimeMillis();
    String gamemode;
    Sensor accelerometer;
    CountDownTimer timer;
    SensorManager mySensorManager;

    //Numbers defining the available modes
    int myMin = 0;
    int myMax = 2;
    int score = 0;
    int limitScore = 15;
    long lengthCountDown = 15000;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //List<Sensor> deviceSensors = mySensorManager.getSensorList(Sensor.TYPE_ALL);
        accelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mySensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Button btnAbort = (Button) findViewById(R.id.btnAbort);
        btnAbort.setOnClickListener(btnAbortListener);

        setNewObjective();

        //Toast.makeText(GameActivity.this, "Strt : sc " + Integer.toString(score) + " ts : " + Long.toString(timeStart), Toast.LENGTH_SHORT).show();

        Bundle extras = getIntent().getExtras();
        gamemode = extras.getString("GAMEMODE");
        //Toast.makeText(GameActivity.this, gamemode, Toast.LENGTH_LONG);

        if (gamemode.equals("fixedNumber")) {
            TextView te = (TextView) findViewById(R.id.chronoTxt);
            te.setText("");
            //timeStart = System.currentTimeMillis();

            if(timer != null) {
                //Toast.makeText(this, "no", Toast.LENGTH_LONG).show();
            }
            timer = new CountDownTimer(300000, 1000) {
                TextView te = (TextView) findViewById(R.id.chronoTxt);
                @Override
                public void onTick(long millisUntilFinished) {
                    te.setText(Long.toString(300 - millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    //Toast.makeText(GameActivity.this, "timer finish", Toast.LENGTH_LONG).show();
                    timer.cancel();
                    finish();
                }
            };
            timer.start();
        }
        else if (gamemode.equals("timeLimit")){
            if(timer != null) {
                //Toast.makeText(this, "no", Toast.LENGTH_LONG).show();
            }
            timer = new CountDownTimer(lengthCountDown, 1000) {
                TextView te = (TextView) findViewById(R.id.chronoTxt);
                @Override
                public void onTick(long millisUntilFinished) {
                    te.setText(Long.toString(millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    //Toast.makeText(GameActivity.this, "timer finish", Toast.LENGTH_LONG).show();
                    mySensorManager.unregisterListener(GameActivity.this, accelerometer);
                    Intent intent = new Intent(GameActivity.this, DisplayScoreActivity.class);
                    intent.putExtra("GAMEMODE", gamemode);
                    intent.putExtra("SCORE", score);
                    //score = 0;
                    timer.cancel();
                    startActivity(intent);
                    finish();
                }
            };
            timer.start();
        }




    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {// TODO Auto−generated method stub
    }



    @Override
    public void onSensorChanged(SensorEvent event) {// TODO Auto−generated method stub


        double accX = event.values[0], accY = event.values[1], accZ = event.values[2];





        TextView etScore = (TextView) findViewById(R.id.scoreTxt);
        if (gamemode.equals("fixedNumber")) {
            etScore.setText(Integer.toString(limitScore - score));
        }
        else {
            etScore.setText(Integer.toString(score));
        }


        if (isFulfilledObjective(accX, accY, accZ, currentObjective)) {
            setNewObjective();
            score++;
            //Toast.makeText(GameActivity.this, "score update : " + Integer.toString(score), Toast.LENGTH_SHORT).show();
            if (gamemode.equals("fixedNumber") && score >= limitScore) {
                //Toast.makeText(GameActivity.this, "score finish : " + Integer.toString(score), Toast.LENGTH_SHORT).show();
                //score = 0;
                mySensorManager.unregisterListener(this, accelerometer);
                Intent intent = new Intent(GameActivity.this, DisplayScoreActivity.class);
                intent.putExtra("GAMEMODE", gamemode);
                intent.putExtra("TIME_START", timeStart);
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                startActivity(intent);
                finish();
            }
        }


    }

    public boolean isFulfilledObjective(double accX, double accY, double accZ, int objectiveNo){
        //Objectives :
        //0 = shake x
        //1 = shake y
        //2 = shake z

        double baseIntensity = 4;
        double IntensityX = baseIntensity;
        double IntensityY = baseIntensity;
        double IntensityZ = baseIntensity;

        double maximumNotComparedIntensity = 4;

        double compared = -1;
        double notCompared1 = -1;
        double notCompared2 = -1;


        if(objectiveNo == 0){
            compared = accX;
            notCompared1 = accY;
            notCompared2 = accZ;
        }
        else if(objectiveNo == 1){
            compared = accY;
            notCompared1 = accX;
            notCompared2 = accZ;
        }
        else if (objectiveNo == 2){
            compared = accZ;
            notCompared1 = accY;
            notCompared2 = accX;
        }
        else{
            Toast.makeText(this, "Error : unknown objective", Toast.LENGTH_LONG).show();
        }


        if(Math.abs(compared) > baseIntensity && Math.abs(notCompared1) < maximumNotComparedIntensity && Math.abs(notCompared2) < maximumNotComparedIntensity){
            return true;
        }
        else {
            return false;
        }
    }

    public void setNewObjective(){
        Random rand;

        int randNum = (int) (myMin + Math.random()*(myMax - myMin + 1));

        if(randNum == currentObjective){ //to avoid the repetition of the same objective
            randNum = myMin + ((currentObjective + 1) % (myMax - myMin));
        }

        currentObjective = randNum;
        setObjectiveImage(currentObjective);
    }

    public void setObjectiveImage(int objective) {
        ImageView imgv = (ImageView) findViewById(R.id.imgObjective);
        if(objective == 0) {  //leftright
            imgv.setImageResource(R.drawable.leftright);
        }
        else if(objective == 1){
            imgv.setImageResource(R.drawable.updown);
        }
        else if (objective == 2) {
            imgv.setImageResource(R.drawable.frontback);
        }
        else {
            Toast.makeText(this, "Error : invalid objective no", Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener btnAbortListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            Toast.makeText(GameActivity.this, "Game cancelled", Toast.LENGTH_LONG).show();
            mySensorManager.unregisterListener(GameActivity.this, accelerometer);
            if(timer != null) {
                timer.cancel();
                timer = null;
            }
            startActivity(intent);
            finish();
        }
    };




}
