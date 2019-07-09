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
String rid;
ListView lv;
ChefAdapter adapter;
ArrayList<selectionForChefObject> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
    
        rid=getIntent().getStringExtra("rid");
        lv=findViewById(R.id.chef_lv);
        list=new ArrayList<>();
        adapter=new ChefAdapter(this,list,rid);
        lv.setAdapter(adapter);
  //      createSwipeMenu();
        getListItems();

    }
/*
    private void createSwipeMenu() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {


            @Override
            public void create(SwipeMenu menu) {
                // create "Call" item
                SwipeMenuItem callItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                callItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                callItem.setWidth(170);
                // set a icon
              //  callItem.setIcon(R.drawable.ic_delete);
                callItem.setTitle("Call");
                callItem.setTitleSize(18);
                callItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(callItem);
            }
        };

// set creator
        lv.setMenuCreator(creator);
        lv.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        {callWaiter();
                        break;
                    }
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }
*/
    private void callWaiter() {
    }

    private void getListItems(){
        FirebaseDatabase.getInstance().getReference("selectionForChef").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {
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
