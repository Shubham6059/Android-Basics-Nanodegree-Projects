package com.example.android.cricketscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final ImageView imageView = findViewById(R.id.cricketBall);
        //These two animation are declared in res/anim
        final Animation rotate = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation antiRotate = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anti_rotate);

        /*This executes in the start and anti rotate the ball*/
        imageView.startAnimation(antiRotate);
        //used default @Override methods
        antiRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            //call for rotating the ball
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(rotate);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        //call for predefined @Override method by rotate
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            //Intent to the MainActivity after Animation ends
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
