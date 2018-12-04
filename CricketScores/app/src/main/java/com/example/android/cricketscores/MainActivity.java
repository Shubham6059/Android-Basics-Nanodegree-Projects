package com.example.android.cricketscores;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int scoreIND = 0;
    int scoreAUS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*for team India*/

    public void add6IND(View view) {
        scoreIND += 6;
        displayIND(scoreIND);
    }

    public void add4IND(View view) {
        scoreIND += 4;
        displayIND(scoreIND);
    }

    public void add3IND(View view) {
        scoreIND += 3;
        displayIND(scoreIND);
    }

    public void add2IND(View view) {
        scoreIND += 2;
        displayIND(scoreIND);
    }

    public void add1IND(View view) {
        scoreIND += 1;
        displayIND(scoreIND);
    }

    /*for team Australia*/

    public void add6AUS(View view) {
        scoreAUS += 6;
        displayAUS(scoreAUS);
    }

    public void add4AUS(View view) {
        scoreAUS += 4;
        displayAUS(scoreAUS);
    }

    public void add3AUS(View view) {
        scoreAUS += 3;
        displayAUS(scoreAUS);
    }

    public void add2AUS(View view) {
        scoreAUS += 2;
        displayAUS(scoreAUS);
    }

    public void add1AUS(View view) {
        scoreAUS += 1;
        displayAUS(scoreAUS);
    }


    public void reset(View view) {          //to reset the scores
        scoreIND = 0;
        displayIND(scoreIND);
        scoreAUS = 0;
        displayAUS(scoreAUS);
    }

    private void displayIND(int score) {
        TextView scoreTextView = findViewById(R.id.scoreIND);
        scoreTextView.setText(String.valueOf(score));
    }

    private void displayAUS(int score) {
        TextView scoreTextView = findViewById(R.id.scoreAUS);
        scoreTextView.setText(String.valueOf(score));
    }
}
