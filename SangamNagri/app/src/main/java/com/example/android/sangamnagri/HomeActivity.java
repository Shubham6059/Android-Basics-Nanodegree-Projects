package com.example.android.sangamnagri;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Set custom ToolBar
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set custom adapter to the ViewPager
        HomeFragmentPagerAdapter homeFragmentPagerAdapter = new HomeFragmentPagerAdapter(getSupportFragmentManager());
        ViewPager home = findViewById(R.id.viewPager);
        home.setAdapter(homeFragmentPagerAdapter);

        //Set TabLayout and assign Listener what to do on Tab select and on page change
        TabLayout tabLayout = findViewById(R.id.tabs);
        home.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(home));
    }
}
