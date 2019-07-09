package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;

import java.util.ArrayList;

public class AdapterForNewStaff extends ArrayAdapter<users> {
    ArrayList<users> list;

    public AdapterForNewStaff(Context context,ArrayList<users> ls) {
        super(context, 0,ls);
        list=ls;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.new_staff_todo_listitems,parent,false);
        }
        ImageView img;
        TextView name,id;
        name=convertView.findViewById(R.id.newStaffDialog_lv_name);
        id=convertView.findViewById(R.id.newStaffDialog_lv_uid);
        users current=list.get(position);
        if(current.getName()!=null){
            name.setText(current.getName());
        }
        id.setText(current.getUid());
        return convertView;
    }
}
