package com.csci448.kduong.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewParticipantProfile extends AppCompatActivity {
    private String mUserId;
    private TextView mName, mAge, mBio, mEmail;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_wmenu);
        mUserId = getIntent().getStringExtra("UserId");
        FloatingActionButton fab = findViewById(R.id.edit_profile);
        fab.hide();

        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUserId);

        mName = (TextView) findViewById(R.id.profile_name);
        mAge = (TextView) findViewById(R.id.profile_age);
        mBio = (TextView) findViewById(R.id.profile_bio);
        mEmail = (TextView) findViewById(R.id.profile_email);

        makeMenu();
        loadInfo();
    }

    public void makeMenu() {
        findViewById(R.id.go_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loadInfo() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    mName.setText(dataSnapshot.child("name").getValue().toString());
                    mAge.setText(dataSnapshot.child("age").getValue().toString());
                    mBio.setText(dataSnapshot.child("bio").getValue().toString());
                    mEmail.setText(dataSnapshot.child("email").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
