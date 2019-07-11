package com.example.drestrau.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.User.addRestrau;
import com.example.drestrau.Objects.attendanceObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {
String uid,rid,staffId;
    TextView name,email,phno1,phno2,address1,address2,id,pin;
    Toolbar toolbar;
    ImageView img;
    //for the attendence
    TextView pres,abs,sal,register_rest;
    LinearLayout attendanceLayout;
    int isStaff=-1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rid=getIntent().getStringExtra("rid");
        uid= FirebaseAuth.getInstance().getUid();
        isStaff=getIntent().getIntExtra("isStaff",-1);      //if 1 means its a staff

        initialiseViews();
        //The collapsing action woth edit functionaloty --------------------------------------------
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_profile_edit:{
                        editDialog();
                        return true;
                    }
                    default:return  ProfileActivity.super.onMenuItemSelected(0,item);
                }
            }
        });

        collapsingToolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Profile");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
        //------------------------------------------------------------------------------------------
        if(isStaff==1){
            attendanceLayout.setVisibility(View.VISIBLE);
            getStaffId();
            //  getImageAndAttendance(staffId);
            register_rest.setVisibility(View.INVISIBLE);
        }else{
            register_rest.setVisibility(View.VISIBLE);
        }
        getId();


        register_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, addRestrau.class);
                startActivity(intent);
            }
        });
    }
    private void initialiseViews(){
        name=findViewById(R.id.profile_name);
        email=findViewById(R.id.profile_email);
        phno1=findViewById(R.id.profile_ph1);
        phno2=findViewById(R.id.profile_ph2);
        address1=findViewById(R.id.profile_address1);
        address2=findViewById(R.id.profile_address2);
        id=findViewById(R.id.profile_uid);
        pin=findViewById(R.id.profile_address_pin);
        img=findViewById(R.id.profile_pic);

        pres=findViewById(R.id.profile_attendance_present);
        abs=findViewById(R.id.profile_attendance_absent);
        sal=findViewById(R.id.profile_attendance_salary);
        attendanceLayout=findViewById(R.id.profile_attendance_layout);
        register_rest=findViewById(R.id.profile_reg_rest);
    }
    private void editDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("EDIT PROFILE");
        View view= LayoutInflater.from(this).inflate(R.layout.edit_dialog,null);
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

        if(staffId!=null){
            message.setVisibility(View.VISIBLE);
        }else{
            message.setVisibility(View.GONE);
        }

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
                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private void getId(){
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
    private void getStaffId() {
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject obj=dataSnapshot.getValue(staffObject.class);
                if(obj!=null){
                    if(obj.getUid().equals(uid)){
                        //we get the staff object
                        getImageAndAttendance(obj.getSid(),obj.getSalary(),obj.getPicUrl());
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
    private void getImageAndAttendance(String sid,final int salary,String picUrl) {
        if(picUrl!=null){
            Glide.with(ProfileActivity.this)
                    .load(picUrl)
                    .into(img);
        }

        FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendanceObject obj=dataSnapshot.getValue(attendanceObject.class);
                if(obj!=null){
                    pres.setText(String.valueOf(obj.getDayCount()));
                    long msDiff = Calendar.getInstance().getTimeInMillis() -obj.getLastPaidDate()*1000;
                    long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                    abs.setText(String.valueOf(daysDiff-obj.getDayCount()));
                    float salPerDay=(float)salary/obj.getMaxDayOfMonth();
                    int salnow=(int) salPerDay*obj.getDayCount();
                    sal.setText(String.valueOf(salnow));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return  true;
    }
}
