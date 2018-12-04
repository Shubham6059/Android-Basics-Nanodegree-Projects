package com.example.android.indie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SongsAdapter extends ArrayAdapter<Songs> {
    /**
     * A custom Adapter {@link SongsAdapter} to populate ListView based on custom class Songs
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param songsList is the list of {@link Songs}s to be displayed.
     */
    public SongsAdapter(@NonNull Context context, @NonNull ArrayList<Songs> songsList) {
        super(context, 0, songsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.song_file, parent, false);
        }
        // Get the object located at this position in the list
        Songs currentSong = getItem(position);

        //set values of song name. artist name and song duration to the ListView
        TextView songName = convertView.findViewById(R.id.songName);
        songName.setText(currentSong.getmSongName());

        TextView artistName = convertView.findViewById(R.id.artistName);
        artistName.setText(currentSong.getmArtistName());

        TextView duration = convertView.findViewById(R.id.duration);
        duration.setText(currentSong.getmDuration());

        return convertView;
    }
}
