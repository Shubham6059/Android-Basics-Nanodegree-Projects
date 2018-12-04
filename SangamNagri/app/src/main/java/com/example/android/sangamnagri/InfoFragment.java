package com.example.android.sangamnagri;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class InfoFragment extends Fragment {
    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get RootView from Activity
        View rootView = inflater.inflate(R.layout.location_listview, container, false);

        //ArrayList to store the location details
        final ArrayList<Details> detailsArrayList = new ArrayList<>();
        detailsArrayList.add(new Details(R.string.city_name, R.string.city_details,
                R.drawable.prayagraj));

        //Custom Adapter
        DetailsAdapter detailsAdapter = new DetailsAdapter(getActivity(), detailsArrayList);

        //Assign custom Adapter to ListView
        ListView listView = rootView.findViewById(R.id.locationListView);
        listView.setAdapter(detailsAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

}
