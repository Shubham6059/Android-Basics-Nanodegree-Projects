package com.example.android.sangamnagri;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        //Switch to the Fragment based on the Tab position
        switch (i) {
            case 0:
                return new InfoFragment();
            case 1:
                return new EventsFragment();
            case 2:
                return new PlacesFragment();
            case 3:
                return new FoodFragment();
            default:
                return null;
        }
    }

    @Override
    //return no of tabs
    public int getCount() {
        return 4;
    }
}
