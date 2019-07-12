package com.example.drestrau.Activities.User;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drestrau.Activities.ProfileActivity;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profileFragment extends Fragment {
   private String uid;
    private TextView name,email,phno1,phno2,address1,address2,id,pin,edit,register_rest;
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        uid= FirebaseAuth.getInstance().getUid();
        initialiseViews(view);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog();
            }
        });
        getIdInfo();


        register_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), addRestrau.class);
                startActivity(intent);
            }
        });

        return view;
    }
    private void initialiseViews(View parent){
        name=parent.findViewById(R.id.profile_name);
        email=parent.findViewById(R.id.profile_email);
        phno1=parent.findViewById(R.id.profile_ph1);
        phno2=parent.findViewById(R.id.profile_ph2);
        address1=parent.findViewById(R.id.profile_address1);
        address2=parent.findViewById(R.id.profile_address2);
        id=parent.findViewById(R.id.profile_uid);
        pin=parent.findViewById(R.id.profile_address_pin);
        edit=parent.findViewById(R.id.profile_edit_btn);
        register_rest=parent.findViewById(R.id.profile_reg_rest);
    }
    private void editDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setTitle("EDIT PROFILE");
        View view= LayoutInflater.from(getContext()).inflate(R.layout.edit_dialog,null);
        builder.setView(view);
        final EditText nm,ph1,ph2,ad1,ad2,pn;
        Button update;
        TextView message;
        message=view.findViewById(R.id.edit_dialog_message);
        nm=view.findViewById(R.id.edit_dialog_name);
        ph1=view.findViewById(R.id.edit_dialog_ph1);
        ph2=view.findViewById(R.id.edit_dialog_ph2);
        ad1=view.findViewById(R.id.edit_dialog_add1);
        ad2=view.findViewById(R.id.edit_dialog_add2);
        pn=view.findViewById(R.id.edit_dialog_pin);
        update=view.findViewById(R.id.edit_dialog_update);


            message.setVisibility(View.GONE);

        if (!name.getText().toString().equals("null")) {
            nm.setText(name.getText().toString());
        }
        if(!phno1.getText().toString().equals("0")){
            ph1.setText(phno1.getText().toString());
        }
        if(!phno2.getText().toString().equals("0")){
            ph2.setText(phno2.getText().toString());
        }
        if(!pin.getText().toString().equals("0")){
            pn.setText(pin.getText().toString());
        }
        if(!ad1.getText().toString().equals("null")){
            ad1.setText(address1.getText().toString());
        }
        if(!ad2.getText().toString().equals("null")){
            ad2.setText(address2.getText().toString());
        }

        final AlertDialog dialog=builder.create();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=nm.getText().toString();
                long p1=Long.parseLong(ph1.getText().toString());
                long p2=Long.parseLong(ph2.getText().toString());
                String a1=ad1.getText().toString();
                String a2=ad2.getText().toString();
                long pin=Long.parseLong(pn.getText().toString());

                FirebaseDatabase.getInstance().getReference("users").child(uid).child("name").setValue(n);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("phno1").setValue(p1);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("phno2").setValue(p2);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("address1").setValue(a1);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("address2").setValue(a2);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("pincode").setValue(pin);
                Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void getIdInfo(){
        FirebaseDatabase.getInstance().getReference("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users user=dataSnapshot.getValue(users.class);
                if(user!=null){
                    name.setText(user.getName());
                    email.setText(user.getEmail());
                    phno1.setText(String.valueOf(user.getPhno1()));
                    phno2.setText(String.valueOf(user.getPhno2()));
                    address1.setText(user.getAddress1());
                    address2.setText(user.getAddress2());
                    id.setText(user.getUid());
                    pin.setText(String.valueOf(user.getPincode()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}