package com.example.android.tazakhabar;

//Custom Java class to fetch data using public Getter methods
public class News {
    private String mTitle;
    private String mSectionName;
    private String mDate;
    private String mAuthor;
    private String mURL;

    public News(String mTitle, String mSectionName, String mDate, String mAuthor, String mURL) {
        this.mTitle = mTitle;
        this.mSectionName = mSectionName;
        this.mDate = mDate;
        this.mAuthor = mAuthor;
        this.mURL = mURL;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmURL() {
        return mURL;
    }
}
