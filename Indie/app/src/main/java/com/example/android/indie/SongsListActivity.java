package com.example.android.indie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class SongsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        //Random list of songs with details stored in an ArrayList
        final ArrayList<Songs> songsList = new ArrayList<>();
        songsList.add(new Songs("I Want to Break Free", "Queen Band", "4:29"));
        songsList.add(new Songs("Should Have Known Better", "Sufjan Stevens","5:07"));
        songsList.add(new Songs("Bohemian Rhapsody", "Queen Band", "6:07"));
        songsList.add(new Songs("Sugar", "Maroon 5", "5:02"));
        songsList.add(new Songs("Summer of 69", "Bryan Adams", "3:41"));
        songsList.add(new Songs("We Will Rock You", "Queen Band", "2:14"));
        songsList.add(new Songs("Believe", "Mumford & Sons", "3:40"));
        songsList.add(new Songs("Afreen Afreen", "Rahat Fateh Ali Khan", "6:45"));
        songsList.add(new Songs("Let Me In", "Grouplove", "3:59"));
        songsList.add(new Songs("Feel", "Bombay Bicyle Club", "4:59"));
        songsList.add(new Songs("Zombie", "Jamie T", "4:06"));

        SongsAdapter songsAdapter = new SongsAdapter(this, songsList);

        ListView selectSong = findViewById(R.id.songs_list);
        selectSong.setAdapter(songsAdapter);

        //On selecting a song from the ListView, with OnItemClickListener and Intent
        //Song Name and Artist Name is passed to NowPlayingActivity
        selectSong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //get position of Song selected
                Songs songs = songsList.get(i);

                //Explicit Intent to pass value to another activity
                Intent intent = new Intent(getApplicationContext(), NowPlayingActivity.class);
                intent.putExtra("SONG_NAME", songs.getmSongName());
                intent.putExtra("ARTIST_NAME", songs.getmArtistName());
                startActivity(intent);
            }
        });
    }
}
