package com.example.drestrau.Activities.Manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Activities.Authentication.Register;
import com.example.drestrau.Activities.User.ProfileActivity;
import com.example.drestrau.Adapters.AdapterForNewStaff;
import com.example.drestrau.Adapters.allStaffAdapter;
import com.example.drestrau.Objects.staffForListObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllStaff extends AppCompatActivity {
ListView lv;
allStaffAdapter adapter;
ArrayList<staffForListObject> list;
FloatingActionButton addstaff;
String rid;
DatabaseReference staffRef;
    ArrayList<users> listForNewStaff;
    AdapterForNewStaff adapterForNewStaff;
    boolean isAllowed=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_staff);

        rid=getIntent().getStringExtra("rid");

        initialiseViews();
        staffRef= FirebaseDatabase.getInstance().getReference("users");
        list=new ArrayList<>();
        adapter=new allStaffAdapter(this,list);
        lv.setAdapter(adapter);
        getListItems();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(AllStaff.this, ProfileActivity.class);     //opening the profile of the staff
                intent.putExtra("rid",rid);
                intent.putExtra("staffId",list.get(position).getStaffId());
                startActivity(intent);
            }

        });
        addstaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllowed)
                    showDialog();
            }
        });
    }
    private void initialiseViews(){
        lv=findViewById(R.id.all_staff_listview);
        addstaff=findViewById(R.id.all_staff_fab);
    }
    private void getListItems(){
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject object=dataSnapshot.getValue(staffObject.class);
                String name=object.getName1()+" "+object.getName2()+" "+object.getName3();
                staffForListObject staff=new staffForListObject(name,null,object.getSid(),object.getUid(),object.getContact1(),object.getDesignation());
                staff.setPicUrl(object.getPicUrl());
               getPaydue(staff);
                //getPPURL(object.getUid(),staff);
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
    private void getPaydue(final staffForListObject object) {
        FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(object.getStaffUid()).child("payDue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer integer=dataSnapshot.getValue(Integer.class);
                if(integer!=null){
                    object.setPaydue(integer);
                }
                adapter.add(object);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showDialog(){
        listForNewStaff=new ArrayList<>();
        adapterForNewStaff=new AdapterForNewStaff(this,listForNewStaff);
        final AlertDialog.Builder builder=new AlertDialog.Builder(AllStaff.this);
        View view = null;
        view= LayoutInflater.from(AllStaff.this).inflate(R.layout.new_staff_dialog,null);
        builder.setView(view);


        //-----------------------------------------------------------------------------------
        final TextView nameEntered=view.findViewById(R.id.dialog_newstaffName);
        Button newId=view.findViewById(R.id.dialognewStaffAddBtn);
        ListView lv=view.findViewById(R.id.dialog_newstaff_lv);
        lv.setAdapter(adapterForNewStaff);
        nameEntered.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null&&s!=""){
                    populateListForStaffs(s.toString());
                }else{
                    listForNewStaff.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                users crnt=listForNewStaff.get(position);
                Intent intent=new Intent(AllStaff.this, addStaff.class);
                intent.putExtra("rid",rid);
                intent.putExtra("staffUid",crnt.getUid());
                startActivity(intent);
                finish();
            }
        });



        newId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto new id activity
                getAlertforExit();
            }
        });


        //------------------------------------------------------------------------------------

        AlertDialog dialog=builder.create();
        builder.setTitle("New Staff");
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                //check in the firebase

                final String idAdded=nameEntered.getText().toString();
                staffRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                        if(dataSnapshot.getKey().equals(idAdded)){
                            Log.e("this", "onChildAdded: " );
                            //enter into another activity with the data from the user
                            Intent intent=new Intent(AllStaff.this, addStaff.class);
                            intent.putExtra("rid",rid);
                            intent.putExtra("staffUid",dataSnapshot.getKey());
                            startActivity(intent);
                            dialog.dismiss();
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
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }private void getAlertforExit() {
        AlertDialog.Builder newid=new AlertDialog.Builder(AllStaff.this);
        newid.setTitle("Log out").setMessage("This Id will be Logged out. Are you sure you want to Continue?").setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //goto new id
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(AllStaff.this, Register.class);
                startActivity(intent);
            }
        }).setNegativeButton("cancel",null);
        newid.create().show();
    }
    private void populateListForStaffs(final String nameEntered) {
        listForNewStaff.clear();
        FirebaseDatabase.getInstance().getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                users usrs=dataSnapshot.getValue(users.class);
                if (usrs != null ) {
                    String nm=usrs.getName();
                    if((usrs.getName()!=null&&usrs.getName().contains(nameEntered))||usrs.getUid().contains(nameEntered)){
                        if(!listForNewStaff.contains(usrs))
                        {listForNewStaff.add(usrs);
                            adapterForNewStaff.notifyDataSetChanged();
                            //                           adapterForNewStaff.add(usrs);
                        }
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
