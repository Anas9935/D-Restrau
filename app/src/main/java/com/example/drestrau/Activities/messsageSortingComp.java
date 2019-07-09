package com.example.drestrau.Activities;

import com.example.drestrau.Objects.messageObject;

import java.util.Comparator;

public class messsageSortingComp implements Comparator<messageObject> {
    @Override
    public int compare(messageObject o1, messageObject o2) {
        return o1.getTimestamp()>o2.getTimestamp()?1:-1;
    }
}
