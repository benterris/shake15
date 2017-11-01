package com.benjamin.android.shake15;

import android.content.Intent;
import android.os.Bundle;
import android .app.Activity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        Button btnFixedNumber = (Button) findViewById(R.id.btnFixedNumber);
        Button btnHighscore = (Button) findViewById(R.id.btnHighscore);
        Button btnTimeLimit = (Button) findViewById(R.id.btnTimeLimit);

        btnFixedNumber.setOnClickListener(btnFixedNumberListener);
        btnTimeLimit.setOnClickListener(btnTimeLimitListener);
        btnHighscore.setOnClickListener(btnHighScoreListener);



    }




    private View.OnClickListener btnFixedNumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GAMEMODE", "fixedNumber");
            startActivity(intent);
            finish();
            //Toast.makeText(MainActivity.this, "aaa", Toast.LENGTH_LONG).show();
        }
    };

    private View.OnClickListener btnTimeLimitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            intent.putExtra("GAMEMODE", "timeLimit");
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener btnHighScoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
            startActivity(intent);
        }
    };


}
