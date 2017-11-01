package com.benjamin.android.shake15;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayScoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_score);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(btnNewGameListener);

        /*
        editor.clear();
        editor.putInt("highScoreTimeLimit", -2);
        editor.putLong("highScoreFixedNumber", -1);
        editor.commit();
        */

        int highScoreTimeLimit = sharedPref.getInt("highScoreTimeLimit", -1);
        long highScoreFixedNumber = sharedPref.getLong("highScoreFixedNumber", -1);

        Bundle extras = getIntent().getExtras();
        //Toast.makeText(this, extras.getString("GAMEMODE"), Toast.LENGTH_LONG).show();


        SharedPreferences.Editor editor = sharedPref.edit();



        if (extras.getString("GAMEMODE").equals("fixedNumber")) {
            long timeStart = extras.getLong("TIME_START");

            long timeStop = SystemClock.uptimeMillis();
            long duration = timeStop - timeStart;



            if (highScoreFixedNumber > duration || highScoreFixedNumber == -1) {
                //write new highscore

                editor.putLong("highScoreFixedNumber", duration);
                editor.commit();
                Toast.makeText(this, "You made a new highscore !", Toast.LENGTH_LONG).show();
            }

            TextView scoreDisplay = (TextView) findViewById(R.id.score);
            scoreDisplay.setText(Double.toString((double) duration/(double) 1000) + "s.");
        }
        else if (extras.getString("GAMEMODE").equals("timeLimit"))
        {
            int score = extras.getInt("SCORE");

            if (score > highScoreTimeLimit) {
                //write new highscore

                editor.putInt("highScoreTimeLimit", score);
                editor.commit();
                Toast.makeText(this, "You made a new highscore !", Toast.LENGTH_LONG).show();
            }


            TextView scoreDisplay = (TextView) findViewById(R.id.score);
            scoreDisplay.setText(Integer.toString(score));
            //scoreDisplay.setText("c " + Long.toString(SystemClock.uptimeMillis()) + " ");
        }

        getIntent().removeExtra("GAMEMODE");
        getIntent().removeExtra("SCORE");
        getIntent().removeExtra("TIME_START");

    }

    private View.OnClickListener btnNewGameListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
    };



}
