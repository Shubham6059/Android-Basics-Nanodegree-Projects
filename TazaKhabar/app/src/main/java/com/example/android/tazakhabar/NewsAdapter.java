package com.example.android.tazakhabar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Custom Adapter to inflate view
public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(@NonNull Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //if null then inflate the custom News Item View
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_cardview_item,
                    parent, false);
        }

        //get current item position
        News currentNews = getItem(position);

        //Populate Title TextView
        TextView titleTextView = convertView.findViewById(R.id.title);
        titleTextView.setText(currentNews.getmTitle());

        //Populate Section TextView
        TextView sectionTextView = convertView.findViewById(R.id.section);
        sectionTextView.setText(currentNews.getmSectionName());

        //Populate Date by reformatting it
        TextView dateTextView = convertView.findViewById(R.id.date);
        String publicationDate = formatDate(currentNews.getmDate());
        dateTextView.setText(publicationDate);

        //Populate Author TextView if available in data
        TextView authorTextView = convertView.findViewById(R.id.author);
        authorTextView.setText(currentNews.getmAuthor());

        return convertView;
    }

    //Format the given date String to a different format i.e. 18 Nov 2018, 01:00
    private static String formatDate(String publicationDate) {
        SimpleDateFormat givenDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",
                Locale.getDefault());
        SimpleDateFormat convertDateFormat = new SimpleDateFormat("dd MMM yyyy, HH:mm",
                Locale.getDefault());
        //Try catch block to handle ParseException
        Date date = null;
        try {
            date = givenDateFormat.parse(publicationDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDateFormat.format(date);
    }
}
