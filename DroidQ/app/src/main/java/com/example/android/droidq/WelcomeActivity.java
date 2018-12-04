package com.example.android.droidq;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final TextInputEditText nameText = findViewById(R.id.name);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when nothing is entered display a Snackbar
                if (nameText.length() == 0) {
                    Snackbar.make(view, "Please fill your name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                //when nameText is not empty go to next QSet1Activity
                else {
                    Intent intent = new Intent
                            (WelcomeActivity.this, QSet1Activity.class);
                    intent.putExtra("user", nameText.getText().toString());
                    startActivity(intent);
                    finish();
                }
                //When user play the quiz again the value is reset to 0
                QSet1Activity.score = 0;
            }
        });
    }
}
