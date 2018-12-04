package com.example.android.tazakhabar;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String mURL;

    public NewsLoader(@NonNull Context context, String URL) {
        super(context); //context
        mURL = URL;     //url to fetch data
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<News> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract news list
        return QueryUtility.fetchNewsItems(mURL);
    }
}
