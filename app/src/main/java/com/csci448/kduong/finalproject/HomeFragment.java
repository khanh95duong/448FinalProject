package com.csci448.kduong.finalproject;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.util.zip.Inflater;

import io.fabric.sdk.android.ActivityLifecycleManager;

/**
 * Created by The Ngo on 3/12/2018.
 */

public class HomeFragment extends Fragment {

    FloatingActionButton mAddEvents;
    RecyclerView mEventRecyclerView;
    private EventAdapter mAdapter;
    private ActivityLifecycleManager.Callbacks mCallbacks;

    private class EventHolder extends RecyclerView.ViewHolder {
        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.event_list_layout, parent, false));
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private List<Event> mEvents;

        public EventAdapter(List<Event> events) {
            mEvents = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new EventHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStates) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mEventRecyclerView = (RecyclerView) view.findViewById(R.id.events_recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAddEvents = (FloatingActionButton) view.findViewById(R.id.add_events);
        mAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "This will open up a new page for user to add events", Toast.LENGTH_SHORT).show();
            }
        });

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void updateUI() {
        EventLab eventLab = EventLab.get(getActivity());
        List<Event> events = eventLab.getEvents();

        mAdapter = new EventAdapter(events);
        mEventRecyclerView.setAdapter(mAdapter);
    }
}
