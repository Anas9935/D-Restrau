package com.example.drestrau.Activities;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.drestrau.Adapters.MessageAdapter;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.messageObject;
import com.example.drestrau.Objects.quantatySelected;
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
import java.util.Collections;

public class SimpleStaffActivity extends AppCompatActivity {
private String rid;
    private String uid;
    private String mangrId;
private int desig;
private TextView name;
    private TextView food;
    private TextView quan;
    private TextView tableNo;
    private TextView ProfileName;
    private TextView ProfileViewBtn;
private RelativeLayout item1;
    private RelativeLayout item2;
private ImageView profileImg;
private Spinner tableSel;
private ListView message;
private MessageAdapter adapter;
private ArrayList<messageObject> list;
private ArrayList<menuObject> menu;

    private String staffId;
private EditText messManager;
private ImageView send;
//LinearLayout ll;
private ConstraintLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_staff);

        rid=getIntent().getStringExtra("rid");
        desig=getIntent().getIntExtra("desig",-1);
        Log.e("SimpleStaff  ", "onCreate: "+rid );
        uid= FirebaseAuth.getInstance().getUid();
        initialiseViews();

        if(desig==3||desig==5||desig==6||desig==7){
            cl.setVisibility(View.GONE);
            tableSel.setVisibility(View.GONE);
        }else{
            cl.setVisibility(View.VISIBLE);
            tableSel.setVisibility(View.VISIBLE);
        }
        getManagerId();
       // setupSpinner();
        if(uid!=null)
        {
            populateDrawer();
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess=messManager.getText().toString();
                if(!mess.equals("")){
                    messManager.setText("");
                    sendMesssage(mess);
                }
            }
        });

    }




    private  void initialiseViews(){
        name=findViewById(R.id.simple_staff_name);
        food=findViewById(R.id.simple_staff_foodItem);
        quan=findViewById(R.id.simple_staff_quantity);
        tableNo=findViewById(R.id.simple_staff_tableNo);
        tableSel=findViewById(R.id.simple_staff_tableSpinner);
        message=findViewById(R.id.simple_staff_listView);
        messManager=findViewById(R.id.simple_staff_messageManager);
        send=findViewById(R.id.simple_staff_send_btn);
        // ll=findViewById(R.id.simple_staff_order);

        //For Drawer Bar
        ProfileName=findViewById(R.id.userName);
        ProfileViewBtn=findViewById(R.id.desc);
        item1=findViewById(R.id.simple_staffRVItem1);
        item2=findViewById(R.id.simple_staffRVItem2);
        cl=findViewById(R.id.simple_staff_order);
        profileImg=findViewById(R.id.avatar);

    }
    private void getManagerId() {
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                boolean[] allChecked={false,false,false};
                int seats=0;
                switch (dataSnapshot.getKey()){
                    case "manUid":{
                        String manId=dataSnapshot.getValue(String.class);
                        mangrId=manId;
                        getMessages(manId);
                        break;
                    }
                    case "seats2":{
                        Integer seat=dataSnapshot.getValue(Integer.class);
                        if(seat!=null){
                           seats+=seat;
                           allChecked[0]=true;
                           int flag=0;
                           for(boolean check:allChecked){
                               if(!check){
                                   flag=1;
                               }
                           }
                           if(flag==0)
                           {
                               setupSpinner(seats);
                           }
                        }
                        break;
                    }
                    case "seats4":{
                        Integer seat=dataSnapshot.getValue(Integer.class);
                        if(seat!=null){
                            seats+=seat;
                            allChecked[1]=true;
                            int flag=0;
                            for(boolean check:allChecked){
                                if(!check){
                                    flag=1;
                                }
                            }
                            if(flag==0)
                            {
                                setupSpinner(seats);
                            }
                        }
                        break;
                    }
                    case "seats6":{
                        Integer seat=dataSnapshot.getValue(Integer.class);
                        if(seat!=null){
                            seats+=seat;
                            allChecked[2]=true;
                            int flag=0;
                            for(boolean check:allChecked){
                                if(!check){
                                    flag=1;
                                }
                            }
                            if(flag==0)
                            {
                                setupSpinner(seats);
                            }
                        }
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

        private void setupSpinner(int seats){
        final Integer[] tables=new Integer[seats];           //array for the spinner
        for(int i=0;i<tables.length;i++){
            tables[i]=i+1;
        }
        ArrayAdapter<Integer> adapter= new ArrayAdapter<>(SimpleStaffActivity.this,android.R.layout.simple_spinner_dropdown_item,tables);
        tableSel.setAdapter(adapter);
        tableSel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             getFooditems(position+1);
                tableNo.setText(String.valueOf(position+1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void getFooditems(final Integer table) {
        FirebaseDatabase.getInstance().getReference("selectionForChef").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                selectionForChefObject object=dataSnapshot.getValue(selectionForChefObject.class);
                if(object!=null){
                    if(object.getTable()==table)
                    {
                        String choice=object.getChoice();
                        tableNo.setText(String.valueOf(object.getTable()));
                        getMenuItems(choice);
                       // fillupChoice(choice);
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
    private void getMenuItems(final String choice) {
        menu=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("menus").child("rid").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject obj=dataSnapshot.getValue(menuObject.class);
                if(obj!=null){
                    menu.add(obj);
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
        FirebaseDatabase.getInstance().getReference("menus").child("rid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fillupChoice(choice);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void fillupChoice(String choice) {
        ArrayList<quantatySelected> itemsSel=new ArrayList<>();

        String[] items=choice.split(" ");
        for(int i=0;i<items.length;i++)
        {
            CharSequence quan=items[i].subSequence(items[i].indexOf("(")+1,items[i].indexOf(")"));

            String food=items[i].substring(0,items[i].indexOf("("));
            quantatySelected object=new quantatySelected(food,Integer.parseInt((String)quan));
            itemsSel.add(object);
        }
        for( int i=0;i<itemsSel.size();i++){
            quantatySelected current=itemsSel.get(i);

            //finding the menu item in the list
            for(menuObject object:menu){
                if(object.getFid().equals(current.getFoodId())){
                    food.append(object.getName()+"\n");
                    quan.append(current.getQuantity()+"\n");
                    break;
                }
            }
        }


    }

        private void getMessages(String managerId){
        //structure main->messages->rid->
        list=new ArrayList<>();
        adapter=new MessageAdapter(this,list,uid);
        message.setAdapter(adapter);

        if(true){       //waiter
            FirebaseDatabase.getInstance().getReference("messages").child(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    messageObject obj=dataSnapshot.getValue(messageObject.class);
                    adapter.add(obj);
                    Collections.sort(list,new messsageSortingComp());
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
            FirebaseDatabase.getInstance().getReference("messages").child(managerId).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    messageObject obj=dataSnapshot.getValue(messageObject.class);
                    if(obj.getToUid().equals(uid))
                    {
                        adapter.add(obj);
                        Collections.sort(list,new messsageSortingComp());
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
    private void sendMesssage(String message) {
        if(mangrId!=null)
        {
            messageObject object=new messageObject(message,System.currentTimeMillis()/1000,uid,mangrId);
            FirebaseDatabase.getInstance().getReference("messages").child(uid).push().setValue(object);
        }
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
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject obj=dataSnapshot.getValue(staffObject.class);
                if(obj!=null&&obj.getUid().equals(uid)&&obj.getPicUrl()!=null){
                    Glide.with(SimpleStaffActivity.this)
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
                Intent intent=new Intent(SimpleStaffActivity.this, ProfileActivity.class);
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
                    Intent intent = new Intent(SimpleStaffActivity.this, QrCodeActivity.class);
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


}
