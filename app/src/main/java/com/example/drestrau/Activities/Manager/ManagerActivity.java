package com.example.drestrau.Activities.Manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.Authentication.Register;
import com.example.drestrau.Adapters.AdapterForNewStaff;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ManagerActivity extends AppCompatActivity {
    String rid,uid;
    ImageView viewStaff,payment,food;
    ImageView spec1,spec2,spec3;
    TextView name1,name2,name3;


DatabaseReference staffRef;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_manager_log_out:{
     //                   uploadImage();
                    FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
            default:{
                return  super.onOptionsItemSelected(item);
            }
        }
    }
}

