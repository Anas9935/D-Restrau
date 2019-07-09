package com.example.drestrau.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.example.drestrau.Objects.selectionForChefObject;
import com.example.drestrau.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChefAdapter extends ArrayAdapter<selectionForChefObject> {
    ArrayList<selectionForChefObject> list;
    String rid;

    public ChefAdapter(Context context, ArrayList<selectionForChefObject> ls,String restId) {
        super(context, 0,ls);
        list=ls;
        rid=restId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.table_and_item__include,parent,false);
        }
        selectionForChefObject current=list.get(position);
        TextView table,food,quan;
        table=view.findViewById(R.id.simple_staff_tableNo);
        food=view.findViewById(R.id.simple_staff_foodItem);
        quan=view.findViewById(R.id.simple_staff_quantity);

        table.setText(String.valueOf(current.getTable()));
        quan.setText("");
        food.setText("");
        String[] items=current.getChoice().split(" ");
        for (String item : items) {
            CharSequence quant = item.subSequence(item.indexOf("(") + 1, item.indexOf(")"));

            quan.append(quant + "\n");
            String foods = item.substring(0, item.indexOf("("));
           // food.append(foods + "\n");      //gives only id
            appendFoodFromId(food,foods);
         //   Log.e("adapter chef", "getView: "+foods );
        }


        return view;
    }

    private void appendFoodFromId(final TextView text, final String foods) {
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject object=dataSnapshot.getValue(menuObject.class);
                if(object!=null){
                    if(object.getFid().equals(foods)){
                        text.append("* "+object.getName()+"\n");
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
