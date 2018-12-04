package com.example.android.droidq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    //Suppressed concatenation warning in setText
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //to get name entered
        String name = getIntent().getStringExtra("user");
        TextView textView = findViewById(R.id.result);

        //Text to be displayed on result
        if (QSet1Activity.score >= 0 && QSet1Activity.score <= 3) {
            textView.setText("Good work " + name + "\nYou've scored " +
                    QSet1Activity.score + "/8" + "\nDon't be sad, you can certainly improve!");
        } else if (QSet1Activity.score > 3 && QSet1Activity.score <= 7) {
            textView.setText("Great job " + name + " \nYou've scored " +
                    QSet1Activity.score + "/8" + "\nYou can certainly improve!");
        } else {
            textView.setText("Brilliant job " + name + "\nYour all answers were correct\n" +
                    "You scored " + QSet1Activity.score + "/8");
        }
    }

}
