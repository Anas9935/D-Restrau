package com.example.drestrau.Activities.Manager;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Adapters.SimpleMenuAdapter;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ManagerChooseSpecial extends AppCompatActivity {

    String rid;
    int pos;

    ArrayList<menuObject> list;
    SimpleMenuAdapter adapter;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_choose_special);

        rid=getIntent().getStringExtra("rid");
        pos=getIntent().getIntExtra("pos",-1);

        lv=findViewById(R.id.manager_spec_lv);
        list=new ArrayList<>();
        adapter=new SimpleMenuAdapter(this,list);
        lv.setAdapter(adapter);
        getList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //here update the rid with new id
                FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("spec"+String.valueOf(pos)).setValue(list.get(position).getFid());
                Toast.makeText(ManagerChooseSpecial.this, "Speciality Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
    private void getList(){
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject obj=dataSnapshot.getValue(menuObject.class);
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
}
