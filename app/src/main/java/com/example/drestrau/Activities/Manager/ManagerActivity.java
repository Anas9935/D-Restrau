package com.example.drestrau.Activities.Manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.ProfileActivity;
import com.example.drestrau.Activities.QrCodeActivity;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManagerActivity extends AppCompatActivity {
    private String rid;
    private String uid;
    private ConstraintLayout viewStaff;
    private ConstraintLayout payment;
    private ConstraintLayout food;
    private ImageView spec1;
    private ImageView spec2;
    private ImageView spec3;
    private TextView name1;
    private TextView name2;
    private TextView name3;
    private TextView ProfileName;
    private TextView ProfileViewBtn;
    private RelativeLayout item1;
    private RelativeLayout item2,item3;
    private ImageView profileImg;
    private String staffId;



private DatabaseReference staffRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        initialiseViews();
        staffRef= FirebaseDatabase.getInstance().getReference("users");
        uid=FirebaseAuth.getInstance().getUid();
        rid=getIntent().getStringExtra("rid");
        setSpeciality();
        viewStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this, AllStaff.class);
                intent.putExtra("rid",rid);
                startActivity(intent);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this,PaymentForManagerActivity.class);
                intent.putExtra("rid",rid);
                startActivity(intent);
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //goto Menu Activity
                Intent intent=new Intent(ManagerActivity.this, EditMenuActivity.class);
                intent.putExtra("rid",rid);
                startActivity(intent);
            }
        });
        populateDrawer();

    }

    private void initialiseViews() {
        viewStaff=findViewById(R.id.manager_viewStaff);
        payment=findViewById(R.id.manager_payment);
        food=findViewById(R.id.manager_food_menu);
        spec1=findViewById(R.id.manager_spec1);
        spec2=findViewById(R.id.manager_spec2);
        spec3=findViewById(R.id.manager_spec3);
        name1=findViewById(R.id.manager_specName1);
        name2=findViewById(R.id.manager_specName2);
        name3=findViewById(R.id.manager_specName3);

        //for the drawer
        ProfileName=findViewById(R.id.userName);
        ProfileViewBtn=findViewById(R.id.desc);
        item1=findViewById(R.id.simple_staffRVItem1);
        item2=findViewById(R.id.simple_staffRVItem2);
        item3 =findViewById(R.id.simple_staffRVItem3);
        profileImg=findViewById(R.id.avatar);
    }
    private void setSpeciality(){

        final Intent intent=new Intent(ManagerActivity.this, ManagerChooseSpecial.class);
        intent.putExtra("rid",rid);
        spec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("pos",1);
                startActivity(intent);
            }
        });
        spec2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("pos",2);
                startActivity(intent);
            }
        });
        spec3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("pos",3);
                startActivity(intent);
            }
        });
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                switch (dataSnapshot.getKey()){
                    case "spec1":{
                                getFood(dataSnapshot.getValue(String.class),1);
                        break;
                    }
                    case "spec2":{
                        getFood(dataSnapshot.getValue(String.class),2);
                        break;
                    }
                    case "spec3":{
                        getFood(dataSnapshot.getValue(String.class),3);
                        break;
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
    private void getFood(String id, final int pos){
        FirebaseDatabase.getInstance().getReference("menus").child(rid).child(id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("picUrl")){
                    switch (pos){
                        case 1:{Glide.with(ManagerActivity.this)
                                .load(dataSnapshot.getValue(String.class))
                                .into(spec1);
                            break;
                        }
                        case 2:{Glide.with(ManagerActivity.this)
                                .load(dataSnapshot.getValue(String.class))
                                .into(spec2);
                            break;
                        }
                        case 3:{Glide.with(ManagerActivity.this)
                                .load(dataSnapshot.getValue(String.class))
                                .into(spec3);
                            break;
                        }
                    }
                }
                if(dataSnapshot.getKey().equals("name")){
                    switch (pos){
                        case 1:{name1.setText(dataSnapshot.getValue(String.class));
                            break;
                        }
                        case 2:{name2.setText(dataSnapshot.getValue(String.class));
                            break;
                        }
                        case 3:{name3.setText(dataSnapshot.getValue(String.class));
                            break;
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


    private void populateDrawer(){

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nm=dataSnapshot.getValue(String.class);
                if(nm!=null){
                    ProfileName.setText(nm);
                }else{
                    ProfileName.setText("NAME");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this, ProfileActivity.class);
                intent.putExtra("isStaff",5);
                startActivity(intent);

            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staffId!=null) {
                    Intent intent = new Intent(ManagerActivity.this, QrCodeActivity.class);
                    intent.putExtra("rid", rid);
                    intent.putExtra("staffId",staffId );
                    startActivity(intent);
                }
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ManagerActivity.this,AboutRestActivity.class);
                intent.putExtra("rid",rid);
                startActivity(intent);
            }
        });
        item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

}

