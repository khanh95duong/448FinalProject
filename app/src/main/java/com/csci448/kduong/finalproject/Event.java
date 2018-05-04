package com.csci448.kduong.finalproject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by darks on 4/22/2018.
 */

public class Event {
    private UUID mId;
    private String mTitle;
    private String mDate;
    private String mHost;
    private String mAddress;

    public Event() {
        this(UUID.randomUUID());
    }

    public Event(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}
