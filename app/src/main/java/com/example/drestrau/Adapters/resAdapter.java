package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.R;

import java.util.ArrayList;

public class resAdapter extends ArrayAdapter<RestObject> {

private final ArrayList<RestObject> list;
    public resAdapter(Context context, ArrayList<RestObject> l) {
        super(context,0,l);
        list=l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.restrau_todo,parent,false);
        }
        TextView name,add1,add2,add3,contact,timing;
        ImageView img;
        RatingBar bar;

        name=view.findViewById(R.id.rest_name);
        add1=view.findViewById(R.id.rest_add1);
        add2=view.findViewById(R.id.rest_add2);
        add3=view.findViewById(R.id.rest_add3);
        contact=view.findViewById(R.id.rest_contact);
        timing=view.findViewById(R.id.rest_timing);
        img=view.findViewById(R.id.rest_img);
        bar=view.findViewById(R.id.rest_rating);

        RestObject current=list.get(position);

        name.setText(current.getName());
        add1.setText(current.getAdd1());
        add2.setText(current.getAdd2());
        add3.setText(current.getAdd3());
        contact.setText(String.valueOf(current.getPhno()));
        timing.setText(current.getOpening()+":00-:"+current.getClosing()+":00");
        if(current.getpictureUrl()!=null){
            Glide.with(getContext())
                    .load(current.getpictureUrl())
                    .into(img);
        }
        bar.setRating(current.getRating());
        return view;
    }
}
