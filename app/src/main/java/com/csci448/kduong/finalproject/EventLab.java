package com.csci448.kduong.finalproject;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by darks on 4/22/2018.
 */

public class EventLab {
    private static EventLab sEventLab;
    private List<Event> mEvents;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;
    private String userName;
    private String userId;

    public static EventLab getInstance() {
        if (sEventLab == null) {
            sEventLab = new EventLab();
        }
        return sEventLab;
    }

    private EventLab() {
        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        mEvents = new ArrayList<>();
        loadInfo();
    }

    public Event getEvent(UUID id) {
        for (Event ev : mEvents) {
            if (ev.getId().toString().equals(id.toString())) {
                return ev;
            }
        }
        return null;
    }

    public void addEvent(Event ev) {
        // Add information to database under the current user
        ev.setHost(userName);
        ev.setHostId(userId);
        ev.addParticipant(userName);
        ev.addParticipantId(userId);
        mDatabaseReference.child(ev.getId().toString()).setValue(ev);
    }

    private void loadInfo() {
        // get user information
        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    userName = dataSnapshot.child("name").getValue().toString();
                    userId = mUser.getUid().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public void loadEvents() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    mEvents.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Event ev = new Event();
                        //Log.d("DATA", ds.toString());
                        //Log.d("DATA VALUES", ds.child("id").getValue().toString());
                        ev.setId(ds.child("id").getValue().toString());
                        ev.setTitle(ds.child("title").getValue().toString());
                        ev.setAddress(ds.child("address").getValue().toString());
                        ev.setDate(ds.child("date").getValue().toString());
                        ev.setTime(ds.child("time").getValue().toString());
                        ev.setHost(ds.child("host").getValue().toString());
                        ev.setHostId(ds.child("hostId").getValue().toString());

                        for (DataSnapshot p : ds.child("participants").getChildren()) {
                            ev.addParticipant(p.getValue().toString());
                        }
                        for (DataSnapshot p : ds.child("participantsId").getChildren()) {
                            ev.addParticipantId(p.getValue().toString());
                        }
                        mEvents.add(ev);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
