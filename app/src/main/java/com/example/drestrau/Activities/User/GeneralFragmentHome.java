package com.example.drestrau.Activities.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.drestrau.Adapters.GeneralFoodRecyclerView;
import com.example.drestrau.Adapters.resAdapter;
import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.Objects.menuObjForFood;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;


public class GeneralFragmentHome extends Fragment {
    private String uid;

    private DatabaseReference ref;
    private resAdapter adapter;

    private TextView address,pincode,signoutBtn;

    private RecyclerView rview;
    private ArrayList<menuObjForFood> foodList;
    private GeneralFoodRecyclerView rv;

    private ListView listView;
    private ArrayList<RestObject> list;

    private LinearLayout pincodeEditLL;
    private EditText pincodeEdit;
    private ImageView pincode_save;

    public GeneralFragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_general_fragment_home,container,false);

        initialize(view);

        list=new ArrayList<>();
        adapter=new resAdapter(getContext(),list);
        listView.setAdapter(adapter);
        //taking bundle as intent


        uid=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        ref= db.getReference("restaurants");

        getDetails();
        populateList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RestObject current=list.get(position);
                Intent intent=new Intent(getContext(), MenuActivity.class);
                intent.putExtra("rid",current.getRid());
                startActivity(intent);
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                address.setVisibility(View.INVISIBLE);
            }
        });
        pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pincode.setVisibility(View.GONE);
                pincodeEditLL.setVisibility(View.VISIBLE);

            }
        });
        pincode_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pincode.setVisibility(View.VISIBLE);
                pincodeEditLL.setVisibility(View.GONE);
                String newPin=pincodeEdit.getText().toString();
                String oldPin=pincode.getText().toString();
                if(!oldPin.equals(newPin))
                {
                    FirebaseDatabase.getInstance().getReference("users").child(uid).child("pincode").setValue(Long.parseLong(newPin));
                    pincode.setText(newPin);
                }
            }
        });
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.setTitle("Exit");
                builder.setMessage("Do you want to Sign Out from this app");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                    }
                }).setNegativeButton("No",null);
                builder.create().show();
            }
        });
        return view;
    }
    private void getDetails(){
        String uid= FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            FirebaseDatabase.getInstance().getReference("users").child(uid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    switch (dataSnapshot.getKey()){
                        case "address1":{
                            address.setText(dataSnapshot.getValue(String.class));
                            break;
                        }
                        case "address2":{
                            address.append(" "+dataSnapshot.getValue(String.class));
                            break;
                        }
                        case "pincode":{
                            pincode.setText(String.valueOf(dataSnapshot.getValue(Long.class)));
                            pincodeEdit.setText(String.valueOf(dataSnapshot.getValue(Long.class)));
                            //  populateList(1);
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
    }
    private void initialize(View parent){
        listView=parent.findViewById(R.id.general_res_list);
        address=parent.findViewById(R.id.general_address);
        pincode=parent.findViewById(R.id.general_pincode);
        rview=parent.findViewById(R.id.general_Rv1);
        pincode_save=parent.findViewById(R.id.general_pincode_save);
        pincodeEdit=parent.findViewById(R.id.general_pincode_edit);
        pincodeEditLL=parent.findViewById(R.id.general_pincode_editLL);
        signoutBtn=parent.findViewById(R.id.general_signout);
        }
    private void populateList(){

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestObject object=dataSnapshot.getValue(RestObject.class);
                adapter.add(object);

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
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                popRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void popRecyclerView(){
        foodList=new ArrayList<>();
        rview.setHasFixedSize(true);
        AdapterView.OnItemClickListener listener=new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                menuObjForFood forFood=foodList.get(position);
                Intent intent=new Intent(getContext(),MenuActivity.class);
                intent.putExtra("rid",forFood.getRid());
                startActivity(intent);
            }
        };
        rv=new GeneralFoodRecyclerView(getContext(),foodList,listener);
        rview.setAdapter(rv);
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        int count=0;
        for(int i=0;i<list.size();i++){
            count++;
            final int random=new Random().nextInt(3);
            final String currRid=list.get(i).getRid();
            String currFid = null;
            switch (random){
                case 0:{
                    currFid=list.get(i).getSpec1();
                    break;
                }
                case 1:{
                    currFid=list.get(i).getSpec2();
                    break;
                }
                case 2:{
                    currFid=list.get(i).getSpec3();
                    break;
                }
            }
            if(currFid!=null){
                final menuObject object = new menuObject();
                FirebaseDatabase.getInstance().getReference("menus").child(currRid).child(currFid).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        switch (dataSnapshot.getKey()){
                            case "fid":{
                                object.setFid(dataSnapshot.getValue(String.class));
                                break;
                            }
                            case "offer":{
                                Integer val=dataSnapshot.getValue(int.class);
                                if(val!=null) {
                                    object.setOffer(val);
                                }else{
                                    object.setOffer(0);
                                }
                                break;
                            }
                            case "picUrl":{
                                object.setPicUrl(dataSnapshot.getValue(String.class));
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
                FirebaseDatabase.getInstance().getReference("menus").child(currRid).child(currFid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        menuObjForFood obj=new menuObjForFood(object,currRid);
                        foodList.add(obj);
                        rv.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            if(count>=10){
                break;
            }
        }


    }
}
