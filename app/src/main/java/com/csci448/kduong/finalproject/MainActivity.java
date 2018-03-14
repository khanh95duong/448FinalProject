package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mHomeButton;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        // This is to test the buttons created in main to see if the buttons in the fragment will show
        /*mSearchButton = (Button) this.findViewById(R.id.search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/

        if(savedInstanceState == null) {
            Fragment fragment = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();

        }
    }

}
