package com.csci448.kduong.finalproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private Button setDate;
    private Button setTime;

    private String mEventDate;
    private String mEventTime;
    private int year, month, day, hour, min;

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

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

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
}
