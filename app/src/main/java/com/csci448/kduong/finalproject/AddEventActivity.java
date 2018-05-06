package com.csci448.kduong.finalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private Button setDate;
    private Button setTime;
    private Button registerEvent;

    private String mEventDate;
    private String mEventTime;
    private int year, month, day, hour, min;

    // booleans to make sure time and date are set
    private boolean timeIsSelected = false;
    private boolean dateIsSelected = false;

    private EditText eventName, eventAddress;

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            NumberFormat nf = new DecimalFormat("#00");

            mEventDate = nf.format(i1 + 1) + "/" + nf.format(i2) + "/" + nf.format(i);
            setDate.setText(mEventDate);
            dateIsSelected = true;
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
            NumberFormat nf = new DecimalFormat("#00");

            mEventTime = nf.format(i) + ":" + nf.format(i1) + " " + ap;
            setTime.setText(mEventTime);
            timeIsSelected = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_layout);

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
        if(TextUtils.isEmpty(eventName.getText())) {
            eventName.setError("Event name is required");
        }
        else if(TextUtils.isEmpty(eventAddress.getText())) {
            eventAddress.setError("Address is required");
        }
        else if(!dateIsSelected) {
            setDate.setError("Date is required");
        }
        else if(!timeIsSelected) {
            setTime.setError("Time is required");
        }
        else {
            String name = eventName.getText().toString().trim();
            String address = eventAddress.getText().toString().trim();
            String date = setDate.getText().toString();
            String time = setTime.getText().toString();
            UUID uuid = UUID.randomUUID();

            Event eventInfo = new Event(uuid);
            eventInfo.setTitle(name);
            eventInfo.setId(uuid);
            eventInfo.setAddress(address);
            eventInfo.setDate(date);
            eventInfo.setTime(time);

            EventLab.getInstance().addEvent(eventInfo);

            Toast.makeText(this, "Information Saved ...", Toast.LENGTH_LONG).show();
            finish();
        }
    }

}
