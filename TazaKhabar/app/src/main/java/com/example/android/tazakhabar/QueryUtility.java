package com.example.android.tazakhabar;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtility {

    //LOG_TAG for log messages
    private static final String LOG_TAG = QueryUtility.class.getSimpleName();

    private QueryUtility() { //private constructor so no object instance gets created outside
    }

    //Query the News API and return a list of News objects.
    public static List<News> fetchNewsItems(String requestUrl) {
        // Create URL object
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //extract a list of jsonResponse
        return parseJson(jsonResponse);
    }

    //Returns new URL object from the given String
    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    //Make an HTTP request to the given URL and return a String as the response
    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null)
            return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /*milliseconds*/);
            httpURLConnection.setConnectTimeout(15000 /*milliseconds*/);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //Read and parse is successful(i.e responseCode = 200)
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                //Show log with error code
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    //Convert the InputStream into a String which contains the whole JSON response from the server
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder outputStringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
                    Charset.forName("UTF-8"/*encoding*/));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                outputStringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return outputStringBuilder.toString();
    }


    //Return NewsList by parsing JSON response
    private static List<News> parseJson(String jsonResponse) {
        //If the JSON string is empty or null, then return
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<News> newsList = new ArrayList<>();
        try {
            //Parse as root->response->results
            JSONObject root = new JSONObject(jsonResponse);
            JSONObject response = root.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                String title, sectionName, date, url;
                JSONObject currentResults = resultsArray.getJSONObject(i);

                title = currentResults.getString("webTitle");   //Title
                sectionName = currentResults.getString("sectionName");  //Section Name

                date = currentResults.optString("webPublicationDate");  //Date

                //Parse the JSON and check if Author Name available
                JSONArray tagsAuthor = currentResults.optJSONArray("tags");
                String author = "";
                if (tagsAuthor.length() != 0) {
                    JSONObject currentTagsAuthor = tagsAuthor.optJSONObject(0);
                    author = currentTagsAuthor.optString("webTitle");
                }
                url = currentResults.getString("webUrl");

                //Add add this to a list by passing through News Constructor
                News news = new News(title, sectionName, date, author, url);
                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return newsList;
    }
}