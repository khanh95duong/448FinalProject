package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by darks on 3/14/2018.
 */

public class SettingFragment extends Fragment {

    private Button mLogout;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

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
