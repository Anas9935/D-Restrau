package com.example.drestrau.Activities.User;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.pres_rest;
import com.example.drestrau.Objects.selection;
import com.example.drestrau.Objects.selectionForChefObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class OnDinerFragment extends Fragment {
    private TextView table,food,quantity;
private String uid;
    private String rid;
private Button scan;
private RelativeLayout rl;
private IntentIntegrator qrcode;
    public OnDinerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_on_diner, container, false);
           uid= FirebaseAuth.getInstance().getUid();
            initializeViews(view);
            if(uid!=null)
            {qrcode=new IntentIntegrator(getActivity());
                scan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qrcode.initiateScan();
                    }
                });
            }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "No Proper Data Found Found", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject obj=new JSONObject(result.getContents());
                    rid=obj.getString("rid");
                    String selChefKey=obj.getString("selForChefKey");
                    inTheRest(rid,selChefKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void inTheRest(final String rid, final String selChefKey) {
        //check if the sel Key is there in rid or not . if yes then hide the overview relative layout
        FirebaseDatabase.getInstance().getReference("selectionForChef").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(selChefKey)) {
                    rl.setVisibility(View.INVISIBLE);
                    selectionForChefObject obj=dataSnapshot.getValue(selectionForChefObject.class);
                    //now we have the object
                    if(obj!=null)
                    {
                        utilityClass.populateTableObject(rid,obj.getChoice(),food,quantity);
                        table.setText(String.valueOf(obj.getTable()));
                    }
                return;
                }
                Toast.makeText(getContext(),"Your Reservation is Not Found", Toast.LENGTH_SHORT).show();
                rl.setVisibility(View.VISIBLE);
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

    private void initializeViews(View view){
        Button order = view.findViewById(R.id.diner_order_btn);
        Button extra = view.findViewById(R.id.diner_extras_btn);
        Button drinks = view.findViewById(R.id.diner_drinks_btn);
        Button waiter = view.findViewById(R.id.diner_waiter_btn2);
        Button cleaner = view.findViewById(R.id.diner_cleaner_btn);
        Button done = view.findViewById(R.id.diner_done_btn);
        TextView complaint = view.findViewById(R.id.diner_complaint);
        TextView rest_name = view.findViewById(R.id.diner_name_rest);
        TextView rest_about = view.findViewById(R.id.diner_about_tv);
        ImageView img = view.findViewById(R.id.diner_img);
        rl=view.findViewById(R.id.diner_relLayout);
        View includeView = view.findViewById(R.id.diner_include);
        table= includeView.findViewById(R.id.simple_staff_tableNo);
        food= includeView.findViewById(R.id.simple_staff_foodItem);
        quantity= includeView.findViewById(R.id.simple_staff_quantity);
        scan=view.findViewById(R.id.diner_rl_scan);
    }


}
