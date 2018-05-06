package com.csci448.kduong.finalproject;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by darks on 4/22/2018.
 */

public class Event {
    private UUID mId;
    private String mTitle;
    private String mDate;
    private String mTime;
    private String mHost;
    private String mHostId;
    private String mAddress;
    private ArrayList<String> mParticipants;
    private ArrayList<String> mParticipantsId;

    public Event() {
        this(UUID.randomUUID());
    }

    public Event(UUID id) {
        mId = id;
        mParticipants = new ArrayList<String>();
        mParticipantsId = new ArrayList<String>();
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

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
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

    public ArrayList<String> getParticipants() {
        return mParticipants;
    }

    public void addParticipant(String p) {
        mParticipants.add(p);
    }

    public ArrayList<String> getParticipantsId() {
        return mParticipantsId;
    }

    public void addParticipantId(String id) {
        mParticipantsId.add(id);
    }

    public String getHostId() {
        return mHostId;
    }

    public void setHostId(String hostId) {
        mHostId = hostId;
    }
}
