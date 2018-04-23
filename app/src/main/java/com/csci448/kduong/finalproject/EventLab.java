package com.csci448.kduong.finalproject;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by darks on 4/22/2018.
 */

public class EventLab {
    private static EventLab sEventLab;
    private List<Event> mEvents;

    public static EventLab get(Context context) {
        if (sEventLab == null) {
            sEventLab = new EventLab(context);
        }
        return sEventLab;
    }

    private EventLab(Context context) {
        mEvents = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Event ev = new Event();
            ev.setTitle("Event #" + i);
            ev.setHost("Me");
            mEvents.add(ev);
        }
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public Event getEvent(UUID id) {
        for (Event ev : mEvents) {
            if (ev.getId().equals(id)) {
                return ev;
            }
        }
        return null;
    }
}