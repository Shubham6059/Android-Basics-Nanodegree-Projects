package com.example.android.droidq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import java.util.ArrayList;

public class QSet1Activity extends AppCompatActivity {
    static int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qset1);
        //to get name entered
        final String name = getIntent().getStringExtra("user");

        //to check if Checkbox "None of these" is selected
        final CheckBox not = findViewById(R.id.ans2e);

        //All correct radio button options
        final ArrayList<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add((RadioButton) findViewById(R.id.ans1));
        radioButtons.add((RadioButton) findViewById(R.id.ans3));
        radioButtons.add((RadioButton) findViewById(R.id.ans4));

        //All correct check box answers to question 2
        final ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) findViewById(R.id.ans2a));
        checkBoxes.add((CheckBox) findViewById(R.id.ans2b));
        checkBoxes.add((CheckBox) findViewById(R.id.ans2c));
        checkBoxes.add((CheckBox) findViewById(R.id.ans2d));

        FloatingActionButton floatingActionButton = findViewById(R.id.fab2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (RadioButton radioButton : radioButtons) {
                    if (radioButton.isChecked()) {
                        score++;        //increase when radiobutton is checked
                    }
                }
                int count = 0;
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        count++;        //count no. of correct options selected
                    }
                }
                if (count == 4) {
                    if (not.isChecked()) {
                        //Rest 4 options were correct not this one
                    } else
                        score++;        //increase when all correct selected
                }
                Intent intent = new Intent(getApplicationContext(), QSet2Activity.class);
                //passing name to next activity
                intent.putExtra("user", name);
                startActivity(intent);
                finish();
            }
        });
    }
}
