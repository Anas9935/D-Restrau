package com.example.drestrau.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Adapters.ChefAdapter;
import com.example.drestrau.Objects.selectionForChefObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChefActivity extends AppCompatActivity {
private String rid;
private ListView lv;
private ChefAdapter adapter;
private ArrayList<selectionForChefObject> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
    
        rid=getIntent().getStringExtra("rid");
        lv=findViewById(R.id.chef_lv);
        list=new ArrayList<>();
        adapter=new ChefAdapter(this,list,rid);
        lv.setAdapter(adapter);
        getListItems();

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
