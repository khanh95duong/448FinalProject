package com.csci448.kduong.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by darks on 3/14/2018.
 */

public class ProfileFragment extends Fragment {

    private FloatingActionButton mEdit;
    private TextView mName, mAge, mBio, mEmail;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        if (mUser.getUid() == null) {
            getActivity().finish();
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());

        mName = (TextView) v.findViewById(R.id.profile_name);
        mAge = (TextView) v.findViewById(R.id.profile_age);
        mBio = (TextView) v.findViewById(R.id.profile_bio);
        mEmail = (TextView) v.findViewById(R.id.profile_email);

        loadInfo();

        mEdit = (FloatingActionButton) v.findViewById(R.id.edit_profile);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This will open up a new page for user to edit their profile
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadInfo();
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