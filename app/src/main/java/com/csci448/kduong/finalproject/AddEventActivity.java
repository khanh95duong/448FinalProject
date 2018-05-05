package com.csci448.kduong.finalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private Button setDate;
    private Button setTime;
    private Button registerEvent;

    private String mEventDate;
    private String mEventTime;
    private String userName;
    private int year, month, day, hour, min;

    private EditText eventName, eventAddress;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mUser;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            mEventDate = Integer.toString(i1 + 1) + "/" + Integer.toString(i2) + "/" + Integer.toString(i);
            setDate.setText(mEventDate);
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            String ap;
            if (i > 12) {
                i -= 12;
                ap = "PM";
            }
            else {
                ap = "AM";
            }

            if (i == 0) {
                i = 12;
                ap = "AM";
            }
            else if (i == 12) {
                ap = "PM";
            }

            mEventTime = Integer.toString(i) + ":" + Integer.toString(i1) + " " + ap;
            setTime.setText(mEventTime);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

        // get firebase and get db reference and user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Events");

        // get user's name
        loadInfo();

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        eventName = (EditText) findViewById(R.id.event_name);
        eventAddress = (EditText) findViewById(R.id.event_address);

        setDate = (Button) findViewById(R.id.set_date);
        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });

        setTime = (Button) findViewById(R.id.set_time);
        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(998);
            }
        });

        registerEvent = (Button) findViewById(R.id.register);
        registerEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInformation();
            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        else if (id == 998) {
            return new TimePickerDialog(this, myTimeListener, hour, min, false);
        }
        return null;
    }

    // save the current information to the database
    public void saveInformation() {
        String name = eventName.getText().toString().trim();
        String address = eventAddress.getText().toString().trim();
        String date = setDate.getText().toString();
        String time = setTime.getText().toString();
        UUID uuid = UUID.randomUUID();

        Event eventInfo = new Event(uuid);
        eventInfo.setId(uuid);
        eventInfo.setAddress(address);
        eventInfo.setHost(userName);
        eventInfo.setDate(date);
        eventInfo.setTime(time);

        // Add information to database under the current user
        mDatabaseReference.child(name).setValue(eventInfo);

        Toast.makeText(this, "Information Saved ...", Toast.LENGTH_LONG).show();
        finish();
    }

    public void loadInfo() {
        // get user information
        DatabaseReference userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mUser.getUid());
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    userName = dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
