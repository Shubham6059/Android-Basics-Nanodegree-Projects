package com.example.android.sangamnagri;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class PlacesFragment extends Fragment {
    public PlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get RootView from Activity
        View rootView = inflater.inflate(R.layout.location_listview, container, false);

        //ArrayList to store the location details
        final ArrayList<Details> detailsArrayList = new ArrayList<>();
        detailsArrayList.add(new Details(R.string.prayagraj_fort, R.string.Prayagraj_Quila_details,
                R.drawable.akbar_fort_allahabad));
        detailsArrayList.add(new Details(R.string.Khusro_Bagh, R.string.Khusro_bagh_details,
                R.drawable.khusro_bagh));
        detailsArrayList.add(new Details(R.string.All_Saints_Cathedral,
                R.string.all_saints_cathedral_details, R.drawable.all_saints_cathedral));

        //Custom Adapter
        DetailsAdapter detailsAdapter = new DetailsAdapter(getActivity(), detailsArrayList);

        //Assign custom Adapter to ListView
        ListView listView = rootView.findViewById(R.id.locationListView);
        listView.setAdapter(detailsAdapter);

        // Inflate the layout for this fragment
        return rootView;
    }

}
