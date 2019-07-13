package com.example.drestrau.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.drestrau.Objects.RecepObject;
import com.example.drestrau.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecepAdapter extends ArrayAdapter<RecepObject> {
    private final ArrayList<RecepObject> list;

    public RecepAdapter(Context context,ArrayList<RecepObject> ls) {
        super(context, 0,ls);
        list=ls;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        final int[] lock = {0};     //locked
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.recep_todo_new,parent,false);
        }
        final TextView name,id,amount,status;
        final ImageView custS,lockBtn;
        final Spinner mode;
        final View sts;
        name=view.findViewById(R.id.recep_todo_nametv);
        id=view.findViewById(R.id.recep_todo_pidTv);
        amount=view.findViewById(R.id.recep_todo_amount);
        custS=view.findViewById(R.id.recep_todo_indic2);
        sts=view.findViewById(R.id.recep_todo_indic1);
        mode=view.findViewById(R.id.recep_todo_mop_spin);
        status=view.findViewById(R.id.recep_todo_stat_spin);
        lockBtn=view.findViewById(R.id.lock);

            mode.setEnabled(false);
            status.setEnabled(false);

        lockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lock[0] ==0){
                    lock[0] =1;
                    lockBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_lock_open_black_24dp));
                    mode.setEnabled(true);
                    status.setEnabled(true);
                }else{
                    lock[0] =0;
                    lockBtn.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_lock_black_24dp));
                    mode.setEnabled(false);
                    status.setEnabled(false);
                }
            }
        });
        final RecepObject current=list.get(position);

        setName(name,current.getUid());
        id.setText(current.getPid());
        amount.setText(String.valueOf(current.getAmount()));


        switch (current.getCustStat()){
            case 0:{
               // custS.setText("Present");
                custS.setBackgroundColor(Color.rgb(0,128,0));
                break;
            }
            case 1:{
               // custS.setText("Absent");
                custS.setBackgroundColor(Color.rgb(255,0,0));
                break;
            }
            case 2:{
               // custS.setText("Gone");
                custS.setBackgroundColor(Color.rgb(0,0,255));
                break;
            }
        }
        switch (current.getStatus()){
            case 1:{
                sts.setBackgroundColor(Color.rgb(0,0,255));
                break;
            }
            case 2:{
                sts.setBackgroundColor(Color.rgb(0,128,0));
                break;
            }
            case 0:{
                sts.setBackgroundColor(Color.rgb(255,0,0));
                break;

            }
        }

        //setting up spinners
        mode.setSelection(current.getMode());
        final ArrayAdapter<CharSequence> mod=ArrayAdapter.createFromResource(getContext(),R.array.paymentMode,android.R.layout.simple_spinner_item);
        mod.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(mod);
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               current.setMode(position);
               updateCurrent(current);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(current.getStatus()==2){
            status.setText("Received");
            current.setCustStat(2);
            notifyDataSetChanged();
        }
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current.getStatus()!=2){
                status.setText("Received");
                current.setStatus(2);
                if(current.getStatus()==2){
                    current.setCustStat(2);
                }
                updateCurrent(current);
                }
            }

        });



        return view;
    }


    private void setName(final TextView tv, String id){
        FirebaseDatabase.getInstance().getReference("users").child(id).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nm=dataSnapshot.getValue(String.class);
                if(nm!=null){
                    tv.setText(nm);
                }else{
                    tv.setText("Customer");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void updateCurrent(RecepObject curr){
        //update status mode and timestamp
        FirebaseDatabase.getInstance().getReference("payments").child(curr.getRid()).child(curr.getPid()).child("mode").setValue(curr.getMode());
        FirebaseDatabase.getInstance().getReference("payments").child(curr.getRid()).child(curr.getPid()).child("status").setValue(curr.getStatus());
        long time=System.currentTimeMillis()/1000;
        FirebaseDatabase.getInstance().getReference("payments").child(curr.getRid()).child(curr.getPid()).child("timestamp").setValue(time);
    }
}
