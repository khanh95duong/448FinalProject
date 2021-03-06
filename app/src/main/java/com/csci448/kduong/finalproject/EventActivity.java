package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.UUID;

public class EventActivity extends AppCompatActivity {
    private TextView mTitle;
    private TextView mDate;
    private TextView mTime;
    private TextView mHost;
    private TextView mAddress;
    private LinearLayout mLinearLayout;
    private Button mJoin;
    private Button mLeave;
    private Button mDeleteEvent;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference eventBaseReference;
    private FirebaseUser mUser;

    private String userID;
    private String userName;
    private String IdThatChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);
        final UUID eventId = UUID.fromString((String) getIntent().getSerializableExtra("EventId"));
        final Event event = EventLab.getInstance().getEvent(eventId);

        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        eventBaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId.toString());
        eventBaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //finish();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.getKey().equals("participantsId") && IdThatChanged == userID) {
                    finish();
                }
                Log.i("EventActivity", dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //finish();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mTitle = (TextView) findViewById(R.id.event_activity_title);
        mTitle.setText(event.getTitle());

        mDate = (TextView) findViewById(R.id.event_activity_date);
        mDate.setText(event.getDate());

        mTime = (TextView) findViewById(R.id.event_activity_time);
        mTime.setText(event.getTime());

        mHost = (TextView) findViewById(R.id.event_actvity_host);
        mHost.setText(event.getHost());

        mAddress = (TextView) findViewById(R.id.event_activity_address);
        mAddress.setText(event.getAddress());

        mLinearLayout = (LinearLayout) findViewById(R.id.tv_activity_holder);

        for (int i = 0; i < event.getParticipants().size(); i++) {
            final int j = i;
            String s = event.getParticipants().get(i);
            TextView t = new TextView(this);
            t.setText(s);
            t.setBackground(getResources().getDrawable(R.drawable.border));
            t.setTextSize(24.0f);
            t.setGravity(Gravity.CENTER_HORIZONTAL);
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(EventActivity.this, ViewParticipantProfile.class);
                    intent.putExtra("UserId", event.getParticipantsId().get(j));
                    startActivity(intent);
                }
            });
            mLinearLayout.addView(t);
        }

        mJoin = (Button) findViewById(R.id.join_event);
        mLeave = (Button) findViewById(R.id.leave_event);
        mDeleteEvent = (Button) findViewById(R.id.delete_event);

        makeMenu();
        getUser(eventId.toString());

        mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdThatChanged = EventLab.getInstance().joinEvent(getIntent().getSerializableExtra("EventId").toString(), userName, userID);
                Toast.makeText(getApplicationContext(), "You have joined an event", Toast.LENGTH_LONG).show();
                //finish();

            }
        });

        mLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IdThatChanged = EventLab.getInstance().leaveEvent(getIntent().getSerializableExtra("EventId").toString(), userName, userID);
                Toast.makeText(getApplicationContext(), "You have left an event", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        mDeleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventLab.getInstance().deleteEvent(eventId.toString());
                Toast.makeText(getApplicationContext(), "Your event was removed", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void makeMenu() {
        findViewById(R.id.go_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getParticipantsId(final String eventId) {
        DatabaseReference partDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId).child("participantsId");
        partDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    mJoin.setVisibility(View.VISIBLE);
                    mLeave.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if(mUser.getUid().toString().equals(ds.getValue().toString())) {
                            mJoin.setVisibility(View.GONE);
                            mLeave.setVisibility(View.VISIBLE);
                        }
                    }
                }
                checkEvent();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUser(final String eventId) {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    userID = mUser.getUid().toString();
                    userName = dataSnapshot.child("name").getValue().toString();
                }
                getParticipantsId(eventId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkEvent() {
        eventBaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    Log.i("EVENT", dataSnapshot.child("hostId").getValue().toString());
                    if(dataSnapshot.child("hostId").getValue().toString().equals(mUser.getUid().toString())) {
                        mDeleteEvent.setVisibility(View.VISIBLE);
                        mJoin.setVisibility(View.GONE);
                        mLeave.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
