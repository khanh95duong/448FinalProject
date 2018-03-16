package com.csci448.kduong.finalproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by darks on 3/14/2018.
 */

public class ProfileFragment extends Fragment {

    FloatingActionButton mEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mEdit = (FloatingActionButton) v.findViewById(R.id.edit_profile);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This will open up a new page for user to edit their profile", Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }
}
