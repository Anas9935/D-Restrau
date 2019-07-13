package com.example.drestrau.Activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.R;
import com.example.drestrau.Adapters.menuAdapter;
import com.example.drestrau.Objects.menuObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
private String rid;
private ListView listView;
private ArrayList<menuObject> list;
private menuAdapter adapter;
private LinearLayout cart;
private String choice;
private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listView=findViewById(R.id.menu_Lv);
        cart=findViewById(R.id.menu_cartBtn);
        Intent i=getIntent();
        rid=i.getStringExtra("rid");

        list=new ArrayList<>();
        adapter=new menuAdapter(this,list);

        listView.setAdapter(adapter);

        mRef= FirebaseDatabase.getInstance().getReference("menus").child(String.valueOf(rid));
        populateList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //opening window for full info of the menu
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice=adapter.generateString();
               Intent intent=new Intent(MenuActivity.this, Reservations.class);
                intent.putExtra("choice",choice);
                intent.putExtra("rid",rid);
               startActivity(intent);
            }
        });

    }


    private void populateList(){
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject current=dataSnapshot.getValue(menuObject.class);
                adapter.add(current);
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
