package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Button mHomeButton;
    private Button mSearchButton;
    private ViewPager mViewPager;
    private SimpleFragmentPagerAdapter mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mAdapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        makeMenu();

        // This is to test the buttons created in main to see if the buttons in the fragment will show
        /*mSearchButton = (Button) this.findViewById(R.id.search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OptionFragment();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

            }
        });

        if(savedInstanceState == null) {
            Fragment fragment = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        }*/
    }

    public void makeMenu()  {
        ImageButton goback = findViewById(R.id.go_back_button);
        goback.setVisibility(View.GONE);
    }

}
