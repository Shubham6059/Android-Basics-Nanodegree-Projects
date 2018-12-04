package com.example.android.tazakhabar.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.tazakhabar.News;
import com.example.android.tazakhabar.NewsAdapter;
import com.example.android.tazakhabar.NewsLoader;
import com.example.android.tazakhabar.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>,
        SharedPreferences.OnSharedPreferenceChangeListener {
    //Query URL with Test API key
    private static final String GUARDIAN_NEWS_API_URL =
            "http://content.guardianapis.com/search?";
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.order_by_key)) ||
                s.equals(getString(R.string.section_news_key))) {
            //Clear the ListView as a new query will be initiated
            mNewsAdapter.clear();

            //Hide the empty state text view as the progressBar will be displayed
            mNetworkErrorTextView.setVisibility(View.GONE);

            //Show the progressBar while new data is being fetched
            View progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            //Restart the loader to query this
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String orderBy = preferences.getString(
                getString(R.string.order_by_key),
                getString(R.string.order_by_default));

        String section = preferences.getString(
                getString(R.string.section_news_key),
                getString(R.string.section_news_default)
        );

        Uri baseUri = Uri.parse(GUARDIAN_NEWS_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //add query parameters to the API Uri
        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("order-by", orderBy);

        //do this when section value changed from default
        if (!section.equals(getString(R.string.section_news_default))) {
            uriBuilder.appendQueryParameter("section", section);
        }

        //pass context and uriBuilder
        return new NewsLoader(this, uriBuilder.toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings_menu) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
