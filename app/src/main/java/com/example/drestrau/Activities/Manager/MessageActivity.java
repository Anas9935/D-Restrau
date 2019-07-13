package com.example.drestrau.Activities.Manager;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Adapters.MessageAdapter;
import com.example.drestrau.Objects.messageObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private String toId;
    private String uid;
private ListView lv;
private EditText message;
private ImageView sendBtn;
private ArrayList<messageObject> list;
private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toId=getIntent().getStringExtra("suid");
        uid= FirebaseAuth.getInstance().getUid();

        initializeViews();
        list=new ArrayList<>();
        adapter=new MessageAdapter(this,list,uid);
        lv.setAdapter(adapter);

        populateList();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess=message.getText().toString();
                if(!mess.equals("")){
                    send(mess);
                }
            }
        });
    }

    private void populateList() {
        FirebaseDatabase.getInstance().getReference("messages").child(uid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageObject object=dataSnapshot.getValue(messageObject.class);
                if(object!=null){
                    if(object.getToUid().equals(toId)){
                        adapter.add(object);
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
        FirebaseDatabase.getInstance().getReference("messages").child(toId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageObject object=dataSnapshot.getValue(messageObject.class);
                if(object!=null){
                    if(object.getToUid().equals(uid)){
                        adapter.add(object);
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

    private void initializeViews() {
    lv=findViewById(R.id.manager_message_Lv);
    message=findViewById(R.id.manager_message_edit);
    sendBtn=findViewById(R.id.manager_message_send_btn);

    }
    private void send(String mess){
        message.setText("");
        long time=System.currentTimeMillis()/1000;
        messageObject object=new messageObject(mess,time,uid,toId);
       // adapter.add(object);
        FirebaseDatabase.getInstance().getReference("messages").child(uid).push().setValue(object);


    }
}
