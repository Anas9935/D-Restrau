package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.drestrau.Objects.paymentObject;
import com.example.drestrau.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Adapter_for_payment_manager extends ArrayAdapter<paymentObject> {
    private final ArrayList<paymentObject> list;

    public Adapter_for_payment_manager(Context context,ArrayList<paymentObject> ls) {
        super(context, 0,ls);
        list=ls;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.payment_for_manager_todo,parent,false);
        }
        paymentObject current=list.get(position);
        TextView pid,date,time,mode,amount;
        pid=view.findViewById(R.id.payment_for_manager_todo_pid);
        date=view.findViewById(R.id.payment_for_manager_todo_date);
        time=view.findViewById(R.id.payment_for_manager_todo_time);
        mode=view.findViewById(R.id.payment_for_manager_todo_mode);
        amount=view.findViewById(R.id.payment_for_manager_todo_amount);

        pid.setText(current.getPid());
        date.setText(getDate(current.getTimestamp()));
        time.setText(getTime(current.getTimestamp()));
        switch (current.getMode()){
            case 0:{
                mode.setText("cash");
                break;
            }
            case 1:{
                mode.setText("card");
                break;
            }
            case 2:{
                mode.setText("paytm");
                break;
            }
        }
        amount.setText(String.valueOf(current.getTotal()));



        return view;
    }
    private String getDate(long timestamp){
        DateFormat format=new SimpleDateFormat("dd/mm/yyyy");
        return format.format(new Date(timestamp));
    }
    private String getTime(long timestamp){
        DateFormat format=new SimpleDateFormat("hh:mm a");
        return format.format(new Date(timestamp));
    }
}
