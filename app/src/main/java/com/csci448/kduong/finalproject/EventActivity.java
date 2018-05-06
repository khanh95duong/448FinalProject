package com.csci448.kduong.finalproject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

public class EventActivity extends AppCompatActivity {
    private TextView mTitle;
    private TextView mDate;
    private TextView mHost;
    private TextView mAddress;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);
        UUID eventId = UUID.fromString((String) getIntent().getSerializableExtra("EventId"));
        Event event = EventLab.getInstance().getEvent(eventId);

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
    }


}
