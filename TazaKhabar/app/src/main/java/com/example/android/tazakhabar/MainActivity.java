package com.example.android.tazakhabar;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    //Query URL with Test API key
    private static final String NEWS_TEST_API_URL =
            "http://content.guardianapis.com/search?show-tags=contributor&api-key=test";
    private static final int NEWS_LOADER_ID = 1;
    private NewsAdapter mNewsAdapter;
    private TextView mNetworkErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Text to display when no data returned
        mNetworkErrorTextView = findViewById(R.id.network_error);
        ListView newsListView = findViewById(R.id.news_listView);
        newsListView.setEmptyView(mNetworkErrorTextView);

        //Custom Adapter for News List
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mNewsAdapter);

        //action to perform on selecting News Item
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News currentNews = mNewsAdapter.getItem(i);
                //parse the Uri and Implicit Intent to News Website
                Uri newsUri = Uri.parse(currentNews.getmURL());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(webIntent);
            }
        });

        //To check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        //Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {
            //fetch data if connection successful
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            //Set text if no Data Received
            mNetworkErrorTextView.setText(R.string.network_error_warning);
            //Set DrawableTop to the above TextView
            mNetworkErrorTextView.setCompoundDrawablesWithIntrinsicBounds
                    (0, R.drawable.icons8_skull_100, 0, 0);
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //pass context and API url
        return new NewsLoader(this, NEWS_TEST_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        //Make progressBar GONE
        View progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        //Set text if no Data Received
        mNetworkErrorTextView.setText(R.string.network_error_warning);
        //Set DrawableTop to the above TextView
        mNetworkErrorTextView.setCompoundDrawablesWithIntrinsicBounds
                (0, R.drawable.icons8_skull_100, 0, 0);
        mNewsAdapter.clear();

        //Add newsList if valid data entry returned
        if (newsList != null && !newsList.isEmpty()) {
            mNewsAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsAdapter.clear();
    }
}
