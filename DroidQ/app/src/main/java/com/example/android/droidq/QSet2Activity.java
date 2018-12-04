package com.example.android.droidq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

public class QSet2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qset2);
        //to get name entered
        final String name = getIntent().getStringExtra("user");

        final CheckBox checkBox = findViewById(R.id.ans8b);
        final CheckBox checkBox1 = findViewById(R.id.ans8d);

        //All correct radio button options
        final ArrayList<RadioButton> radioButtons = new ArrayList<>();
        radioButtons.add((RadioButton) findViewById(R.id.ans5));
        radioButtons.add((RadioButton) findViewById(R.id.ans7));

        //All correct check box answers to question 8
        final ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) findViewById(R.id.ans8a));
        checkBoxes.add((CheckBox) findViewById(R.id.ans8c));

        //Go back to QSet1 with reset value for selection
        Button button = findViewById(R.id.cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QSet1Activity.score = 0;
                Intent intent = new Intent(getApplicationContext(), QSet1Activity.class);
                //passing name to previous activity
                intent.putExtra("user", name);
                startActivity(intent);
                finish();
            }
        });

        Button button1 = findViewById(R.id.submit);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (RadioButton radioButton : radioButtons) {
                    if (radioButton.isChecked()) {
                        QSet1Activity.score++;        //increase when radiobutton is checked
                    }
                }
                TextInputEditText answer6 = findViewById(R.id.ans6);
                if (answer6.length() != 0) {
                    int val = Integer.parseInt(answer6.getText().toString());
                    if (val == 27)
                        QSet1Activity.score++;
                }
                int count = 0;
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        count++;        //count no. of correct options selected
                    }
                }
                if (count == 2) {
                    if (checkBox.isChecked() || checkBox1.isChecked()) {
                        //Do nothing wrong answer
                    } else {
                        QSet1Activity.score++;
                    }
                }
                //toast to display result
                String res = Integer.toString(QSet1Activity.score);
                Toast.makeText(QSet2Activity.this, res + " questions were correct", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                //passing name to next activity
                intent.putExtra("user", name);
                startActivity(intent);
                finish();
            }
        });
    }
}
