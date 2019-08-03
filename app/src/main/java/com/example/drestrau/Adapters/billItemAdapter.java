package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drestrau.Objects.bill_item_object;
import com.example.drestrau.R;

import java.util.ArrayList;

public class billItemAdapter extends ArrayAdapter {
    ArrayList<bill_item_object> list;
    int itemfor=0;
    public billItemAdapter(Context context,ArrayList<bill_item_object> ls){
        super(context,0,ls);
        list=ls;
    }
    public billItemAdapter(Context context,ArrayList<bill_item_object> ls,int itemfr){
        super(context,0,ls);
        list=ls;
        itemfor=itemfr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.bill_item_todo,parent,false);
        }
        bill_item_object current=list.get(position);

        TextView name,qtyAndAmt;
        ImageView cross;
        name=view.findViewById(R.id.bill_item_food_name);
        qtyAndAmt=view.findViewById(R.id.bill_item_quanAndamt);
        cross=view.findViewById(R.id.bill_item_cross);
        name.setText(current.getName());
        qtyAndAmt.setText(current.getQuantity()+" × ₹"+current.getAmount());
        if(itemfor==0){
            cross.setVisibility(View.GONE);
        }
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel this item from the sid

            }
        });
        return view;
    }
}
