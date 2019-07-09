package com.example.drestrau.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.drestrau.Adapters.allStaffAdapter;
import com.example.drestrau.Objects.attendanceObject;
import com.example.drestrau.Objects.staffForListObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ManualAttendenceActivity extends AppCompatActivity {
String rid,uid;
ListView lv;
ArrayList<staffForListObject> list;
allStaffAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_attendence);

        rid=getIntent().getStringExtra("rid");
        uid= FirebaseAuth.getInstance().getUid();
        lv=findViewById(R.id.manual_att_listView);

        list=new ArrayList<>();
        adapter=new allStaffAdapter(this,list,0);
        lv.setAdapter(adapter);
        populateList();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final staffForListObject object=list.get(position);
                FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(object.getStaffUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        attendanceObject obj=dataSnapshot.getValue(attendanceObject.class);
                        int dc;
                        if(obj!=null){
                            dc = obj.getDayCount();
                            dc+=1;
                            obj.setDayCount(dc);
                            if(dc>=obj.getMaxDayOfMonth()){
                                obj.setPayDue(1);
                            }
                            obj.setAttendenceToday(1);
                            FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(object.getStaffUid()).setValue(obj);
                            Toast.makeText(ManualAttendenceActivity.this, "Attendance done", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void populateList() {
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject object=dataSnapshot.getValue(staffObject.class);
                if(object!=null){
                    String name=object.getName1()+" "+object.getName2()+" "+object.getName3();
                    staffForListObject obj=new staffForListObject(name,object.getPicUrl(),object.getSid(),object.getUid(),object.getContact1(),object.getDesignation());
                    adapter.add(obj);
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
