package com.csci448.kduong.finalproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import io.fabric.sdk.android.ActivityLifecycleManager;

/**
 * Created by The Ngo on 3/12/2018.
 */

public class HomeFragment extends Fragment {

    FloatingActionButton mAddEvents;
    RecyclerView mEventRecyclerView;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private List<Event> events;

    private EventAdapter mAdapter;
    private ActivityLifecycleManager.Callbacks mCallbacks;

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Event mEvent;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mHostTextView;

        public EventHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.event_list_layout, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView)itemView.findViewById(R.id.event_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.event_date);
            mHostTextView = (TextView) itemView.findViewById(R.id.event_host);
        }

        public void bind(Event event) {
            mEvent = event;
            mTitleTextView.setText(mEvent.getTitle());
            mDateTextView.setText(mEvent.getDate());
            mHostTextView.setText(mEvent.getHost());
            if (mEvent.isAParticipant(mUser.getUid())) {
                itemView.setBackgroundColor(0xff00ff00);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), EventActivity.class);
            intent.putExtra("EventId", mEvent.getId());
            startActivityForResult(intent, 0);
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
            Event event = mEvents.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStates) {
        Log.i("HomeFragment", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                updateUI();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                updateUI();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                updateUI();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                updateUI();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                updateUI();
            }
        });

        EventLab.getInstance().loadEvents();

        mEventRecyclerView = (RecyclerView) view.findViewById(R.id.events_recycler_view);

        mAddEvents = (FloatingActionButton) view.findViewById(R.id.add_events);
        mAddEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddEventActivity.class);
                startActivityForResult(i, 0);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("HomeFragment", "On activity result called in home");
        updateUI();
        getActivity().recreate();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUI();
        //new SearchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("HomeFragment", "onStart");
        updateUI();
        //new SearchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("HomeFragment", "onResume");
        updateUI();
        //new SearchTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void updateUI() {
        events = EventLab.getInstance().getEvents();
        mEventRecyclerView.setAdapter(null);
        mEventRecyclerView.setLayoutManager(null);
        mAdapter = new EventAdapter(events);
        mEventRecyclerView.setAdapter(mAdapter);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.notifyDataSetChanged();
    }

    public void getSize() {
        Log.i("Event SIZE HF", ""+events.size());
    }

    private class SearchTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute()
        {
            Log.i("START", "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            EventLab.getInstance().loadEvents();
            Log.i("BACKGROUND", "doInBackground");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateUI();
            Log.i("EVENT SIZE", ""+events.size());
        }
    }
}
