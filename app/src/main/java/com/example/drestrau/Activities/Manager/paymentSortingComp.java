package com.example.drestrau.Activities.Manager;

import com.example.drestrau.Objects.paymentObject;

import java.util.Comparator;

class paymentSortingComp implements Comparator<paymentObject> {
    @Override
    public int compare(paymentObject o1, paymentObject o2) {
        if(o1.getTimestamp()>o2.getTimestamp()){
            return 1;
        }else
            return -1;

        }

}
