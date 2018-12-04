package com.example.android.sangamnagri;
//Custom Java class for accessing location information

public class Details {
    private int mTitleTextViewId;
    private int mDetailsTextViewId;
    private int mImageResourceId;

    public Details(int mTitleTextViewId, int mDetailsTextViewId, int mImageResourceId) {
        this.mTitleTextViewId = mTitleTextViewId;
        this.mDetailsTextViewId = mDetailsTextViewId;
        this.mImageResourceId = mImageResourceId;
    }

    public int getmTitleTextViewId() {
        return mTitleTextViewId;
    }

    public int getmDetailsTextViewId() {
        return mDetailsTextViewId;
    }

    public int getmImageResourceId() {
        return mImageResourceId;
    }
}