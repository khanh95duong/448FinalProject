package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.fabric.sdk.android.Fabric;

/**
 * Created by The Ngo on 4/22/2018.
 */

public class ProfileEditActivity extends AppCompatActivity {

    private EditText mName, mAge, mBio;
    private Button mSaveInfo;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_edit);
        Fabric.with(this, new Crashlytics());

        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(user.getUid());

        mName = (EditText) findViewById(R.id.edit_name);
        mAge = (EditText) findViewById(R.id.edit_age);
        mBio = (EditText) findViewById(R.id.edit_bio);
        mSaveInfo = (Button) findViewById(R.id.save_information);

        loadInfo();

        mSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();
            }
        });
    }

    // save the current information to the database
    public void saveInformation() {
        String name = mName.getText().toString().trim();
        int age = Integer.valueOf(mAge.getText().toString().trim());
        String bio = mBio.getText().toString().trim();

        UserInformation userInfo = new UserInformation(name, age, bio);

        // Add information to database under the current user
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        mDatabaseReference.setValue(userInfo);

        Toast.makeText(this, "Information Saved ...", Toast.LENGTH_LONG).show();
        finish();
        // start a new activity in main, there might be a better way to do this
        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }

    // load current information into the edit texts
    public void loadInfo() {
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    mName.setText(dataSnapshot.child("name").getValue().toString());
                    mAge.setText(dataSnapshot.child("age").getValue().toString());
                    mBio.setText(dataSnapshot.child("bio").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}