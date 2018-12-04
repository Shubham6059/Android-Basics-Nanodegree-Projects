package com.example.android.sangamnagri;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsAdapter extends ArrayAdapter<Details> {
    //Constructor to pass the detailsArrayList and Context
    public DetailsAdapter(@NonNull Context context, @NonNull ArrayList<Details> detailsArrayList) {
        super(context, 0, detailsArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Inflate view if null i.e. at start position
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        //get current view position
        Details currentItem = getItem(position);

        //Set Title of Location
        TextView titleText = convertView.findViewById(R.id.titleText);
        titleText.setText(currentItem.getmTitleTextViewId());

        //Set Detail Info for location
        TextView detailsText = convertView.findViewById(R.id.detailsText);
        detailsText.setText(currentItem.getmDetailsTextViewId());

        //Set image for location
        ImageView locationImage = convertView.findViewById(R.id.locationImage);
        locationImage.setImageResource(currentItem.getmImageResourceId());

        //return this view
        return convertView;
    }
}
