package com.csci448.kduong.finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class EventActivity extends AppCompatActivity {
    private TextView mTitle;
    private TextView mDate;
    private TextView mHost;
    private TextView mAddress;
    private LinearLayout mLinearLayout;
    private Button mJoin;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;

    private String userID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);
        UUID eventId = UUID.fromString((String) getIntent().getSerializableExtra("EventId"));
        Event event = EventLab.getInstance().getEvent(eventId);

        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());


        mTitle = (TextView) findViewById(R.id.event_activity_title);
        mTitle.setText(event.getTitle());

        mDate = (TextView) findViewById(R.id.event_activity_date);
        mDate.setText(event.getDate());

        mHost = (TextView) findViewById(R.id.event_actvity_host);
        mHost.setText(event.getHost());

        mAddress = (TextView) findViewById(R.id.event_activity_address);
        mAddress.setText(event.getAddress());

        mLinearLayout = (LinearLayout) findViewById(R.id.tv_activity_holder);

        for (String s : event.getParticipants()) {
            TextView t = new TextView(this);
            t.setText(s);
            t.setGravity(Gravity.CENTER_HORIZONTAL);
            mLinearLayout.addView(t);
        }

        mJoin = (Button) findViewById(R.id.join_event);
        getUser();
        getParticipantsId(eventId.toString());
        mJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventLab.getInstance().joinEvent(getIntent().getSerializableExtra("EventId").toString(), userName, userID);
            }
        });

    }

    public void getParticipantsId(final String eventId) {
        DatabaseReference partDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId).child("participantsId");
        partDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Log.i("User Id", mUser.getUid().toString());
                        Log.i("Current Id", ds.getValue().toString());
                        if(mUser.getUid().toString().equals(ds.getValue().toString())) {
                            mJoin.setVisibility(View.INVISIBLE);
                            Log.i("Button", "invisible");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUser() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    userID = mUser.getUid().toString();
                    userName = dataSnapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
