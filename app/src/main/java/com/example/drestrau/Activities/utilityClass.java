package com.example.drestrau.Activities;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class utilityClass {

    public static String getTime(long timestamp){
        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(timestamp*1000));
    }
    public static String getDate(long timestamp){
        SimpleDateFormat formatter=new SimpleDateFormat("dd/mm/yyyy");
        return formatter.format(timestamp*1000);//(new Date(timestamp*1000));
    }
    public static void populateTableObject(String rid, final String choice, final TextView foods, final TextView qunatities){
        final ArrayList<menuObject> list;
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject obj=dataSnapshot.getValue(menuObject.class);
                if(obj!=null){
                    list.add(obj);
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
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // listInterface.getMenuList(list);
                //Here ihave the list
                populateViews(choice,list,foods,qunatities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private static void populateViews(String choice, ArrayList<menuObject> list, TextView foodItems,TextView quantity){
        ArrayList<quantatySelected> itemsSel=new ArrayList<>();

        String[] items=choice.split(" ");
        for(int i=0;i<items.length;i++)
        {
            CharSequence quan=items[i].subSequence(items[i].indexOf("(")+1,items[i].indexOf(")"));
            //quantity.add(Integer.parseInt((String) quan));

            String food=items[i].substring(0,items[i].indexOf("("));
            //  foodlist.add(Integer.parseInt(food));
            quantatySelected object=new quantatySelected(food,Integer.parseInt((String)quan));
            itemsSel.add(object);
        }
        for( int i=0;i<itemsSel.size();i++){
            quantatySelected current=itemsSel.get(i);

            //finding the menu item in the list
            for(menuObject object:list){
                if(object.getFid().equals(current.getFoodId())){
                    foodItems.append(object.getName()+"\n");
                    quantity.append(current.getQuantity()+"\n");
                    break;
                }
            }

        }
    }
}
