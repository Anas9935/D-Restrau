package com.example.drestrau.Activities.Manager;


import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Adapters.Adapter_for_payment_manager;
import com.example.drestrau.Objects.paymentObject;
import com.example.drestrau.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class PaymentForManagerActivity extends AppCompatActivity {
ListView lv;
String rid;
Adapter_for_payment_manager adapter_for_payment_manager;
ArrayList<paymentObject> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_for_manager);

        initialeViews();
        rid=getIntent().getStringExtra("rid");
        list=new ArrayList<>();
        adapter_for_payment_manager=new Adapter_for_payment_manager(this,list);
        lv.setAdapter(adapter_for_payment_manager);
        populateList();
    }
    private void initialeViews(){
        lv=findViewById(R.id.payment_for_manager_lv);
    }
    private void populateList(){
        FirebaseDatabase.getInstance().getReference("payments").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                paymentObject obj=dataSnapshot.getValue(paymentObject.class);
                if (obj != null && obj.getStatus() == 2) {
                    adapter_for_payment_manager.add(obj);
                    Collections.sort(list,new paymentSortingComp());
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
