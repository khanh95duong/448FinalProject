package com.csci448.kduong.finalproject;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.zip.Inflater;

/**
 * Created by The Ngo on 3/12/2018.
 */

public class HomeFragment extends Fragment {

    FloatingActionButton mAddEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mAddEvents = (FloatingActionButton) v.findViewById(R.id.add_events);
        mAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This will open up a new page for user to add events", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

}
