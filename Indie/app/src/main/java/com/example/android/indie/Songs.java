package com.example.android.indie;

public class Songs {
    /**
     * Custom Java class to access Song Data
     */

    private String mSongName;
    private String mArtistName;
    private String mDuration;

    public Songs(String mSongName, String mArtistName, String mDuration) {
        this.mSongName = mSongName;
        this.mArtistName = mArtistName;
        this.mDuration = mDuration;
    }

    public String getmSongName() {
        return mSongName;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public String getmDuration() {
        return mDuration;
    }
}
