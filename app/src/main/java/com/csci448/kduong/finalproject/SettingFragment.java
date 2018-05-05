package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by darks on 3/14/2018.
 */

public class SettingFragment extends Fragment {

    private Button mLogout;
    private FirebaseAuth mFirebaseAuth;
    private Switch themeSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Edit theme of app
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            getActivity().setTheme(R.style.DarkTheme);
        } else {
            getActivity().setTheme(R.style.AppTheme);
        }
        getActivity().setTheme(R.style.DarkTheme);
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        themeSwitch = (Switch) v.findViewById(R.id.theme_switch);
        // Set switch based on theme of app
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            themeSwitch.setChecked(true);
        }

        //Change theme on switch change
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    Log.d("THEME: ", getActivity().getTheme().toString());
                    getActivity().setTheme(R.style.AppTheme);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    Log.d("THEME: ", getActivity().getTheme().toString());
                    getActivity().setTheme(R.style.AppTheme);
                }
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() == null) {
            getActivity().finish();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }

        mLogout = (Button) v.findViewById(R.id.logout_button);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.signOut();
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);            }
        });
        return v;
    }
}
