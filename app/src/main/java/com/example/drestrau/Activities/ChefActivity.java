package com.example.drestrau.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.drestrau.Adapters.ChefAdapter;
import com.example.drestrau.Objects.selectionForChefObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChefActivity extends AppCompatActivity {
private String rid;
private ListView lv;
private ChefAdapter adapter;
private ArrayList<selectionForChefObject> list;
//for drawer
private TextView ProfileName;
    private TextView ProfileViewBtn;
    private RelativeLayout item1;
    private RelativeLayout item2;
    private ImageView profileImg;
    String staffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
    
        rid=getIntent().getStringExtra("rid");
        lv=findViewById(R.id.chef_lv);
        initializeViews();
        list=new ArrayList<>();
        adapter=new ChefAdapter(this,list,rid);
        lv.setAdapter(adapter);
        getListItems();
        populateDrawer();

    }
    private void initializeViews(){
        ProfileName=findViewById(R.id.userName);
        ProfileViewBtn=findViewById(R.id.desc);
        item1=findViewById(R.id.simple_staffRVItem1);
        item2=findViewById(R.id.simple_staffRVItem2);
      //  cl=findViewById(R.id.simple_staff_order);
        profileImg=findViewById(R.id.avatar);

    }

    private void callWaiter() {

    }

    private void getListItems(){
        FirebaseDatabase.getInstance().getReference("selectionForChef").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot,  String s) {
                selectionForChefObject obj=dataSnapshot.getValue(selectionForChefObject.class);
                adapter.add(obj);
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
        final String uid=FirebaseAuth.getInstance().getUid();
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
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject obj=dataSnapshot.getValue(staffObject.class);
                if(obj!=null&&obj.getUid().equals(uid)&&obj.getPicUrl()!=null){
                    Glide.with(ChefActivity.this)
                            .load(obj.getPicUrl())
                            .into(profileImg);
                    //here we take image url and staff id
                    staffId=dataSnapshot.getKey();
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

        ProfileViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the profile of the worker
                Intent intent=new Intent(ChefActivity.this, ProfileActivity.class);
                intent.putExtra("isStaff",1);
                intent.putExtra("rid",rid);
                startActivity(intent);
                //    getSidAndContinue();

            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staffId!=null) {
                    Intent intent = new Intent(ChefActivity.this, QrCodeActivity.class);
                    intent.putExtra("rid", rid);
                    intent.putExtra("staffId",staffId);
                    startActivity(intent);
                }
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
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
