package com.example.android.indie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NowPlayingActivity extends AppCompatActivity {
    //NOTE: The SeekBar and 3 ImageButton in layout "activity_now_playing" are for illustration
    //purpose only, to show a structural view for the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        //Receive Intent from SongsListActivity
        Intent playing = getIntent();
        TextView songName = findViewById(R.id.song_playing);
        TextView artistName = findViewById(R.id.artist_name);

        //Assign text values for the selected song from playlist
        songName.setText(playing.getStringExtra("SONG_NAME"));
        artistName.setText(playing.getStringExtra("ARTIST_NAME"));
    }
}
