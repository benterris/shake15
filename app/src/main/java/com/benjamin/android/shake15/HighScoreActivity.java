package com.benjamin.android.shake15;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class HighScoreActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Button btnReset = (Button) findViewById(R.id.btnResetHighScore);
        btnReset.setOnClickListener(btnResetListener);

        Button btnBack = (Button) findViewById(R.id.btnBackToMenu);
        btnBack.setOnClickListener(btnBackListener);

        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);





        int highScoreTimeLimit = sharedPref.getInt("highScoreTimeLimit", -1);
        long highScoreFixedNumber = sharedPref.getLong("highScoreFixedNumber", -1);


        TextView tvHighScoreTimeLimit = (TextView) findViewById(R.id.highScoreTimeLimit);
        TextView tvHighScoreFixedNumber = (TextView) findViewById(R.id.highScoreFixedNumber);

        if (highScoreTimeLimit == -1) {
            tvHighScoreTimeLimit.setText("Time Limit Mode : No highscore yet !");
        }
        else {
            tvHighScoreTimeLimit.setText("Time Limit Mode : " + Integer.toString(highScoreTimeLimit));
        }

        if (highScoreFixedNumber == -1) {
            tvHighScoreFixedNumber.setText("Fixed Number Mode : No highscore yet !");
        }
        else {
            tvHighScoreFixedNumber.setText("Fixed Number Mode : " + Double.toString(((double)highScoreFixedNumber / (double)1000)) + " s.");
        }



    }


    private View.OnClickListener btnResetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HighScoreActivity.this);
            builder.setTitle(R.string.app_name);
            builder.setMessage(R.string.confirm);
            builder.setIcon(R.drawable.appicon);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    SharedPreferences sharedPref = HighScoreActivity.this.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong("highScoreFixedNumber", -1);
                    editor.putInt("highScoreTimeLimit", -1);
                    editor.commit();

                    Toast.makeText(HighScoreActivity.this, "All highscore reset", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(HighScoreActivity.this, MainActivity.class);
                    startActivity(intent);

                    dialog.dismiss();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };


    private View.OnClickListener btnBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HighScoreActivity.this, MainActivity.class);
            startActivity(intent);
        }
    };


}
