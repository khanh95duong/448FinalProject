package com.csci448.kduong.finalproject;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
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
    private ArrayList<String> participants;
    private ArrayList<String> participantsId;

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

    public void addEvent(Event ev, String name, String id) {
        // Add information to database under the current user
        ev.addParticipant(name);
        ev.addParticipantId(id);
        mDatabaseReference.child(ev.getId().toString()).setValue(ev);
    }

    public String joinEvent(String eventId, String name, String id) {
        participants = new ArrayList<>();
        participantsId = new ArrayList<>();
        addParticipants(eventId, name, true);
        addParticipantsId(eventId, id, true);
        return id;
    }

    public String leaveEvent(String eventId, String name, String id) {
        participants = new ArrayList<>();
        participantsId = new ArrayList<>();
        addParticipants(eventId, name, false);
        addParticipantsId(eventId, id, false);
        return id;
    }

    public void addParticipants(final String eventId, final String name, final boolean join) {
        DatabaseReference partDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId).child("participants");
        partDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    participants.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        participants.add(ds.getValue().toString());
                        Log.i("Participants", ""+participants.size());
                    }
                    if(join) {
                        participants.add(name);
                    }else {
                        participants.remove(name);
                    }
                    mDatabaseReference.child(eventId).child("participants").setValue(participants);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addParticipantsId(final String eventId, final String id, final boolean join) {
        DatabaseReference partDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId).child("participantsId");
        partDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    participantsId.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        participantsId.add(ds.getValue().toString());                   }
                }
                if(join) {
                    participantsId.add(id);
                }else {
                    participantsId.remove(id);
                }
                mDatabaseReference.child(eventId).child("participantsId").setValue(participantsId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    public void deleteEvent(String eventId) {
        DatabaseReference eventBaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        eventBaseReference.child(eventId).removeValue();
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }
}
