package com.example.drestrau.Activities.Manager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.R;
import com.google.firebase.database.FirebaseDatabase;


public class EditRestFragment extends Fragment {
private EditText name,add1,add2,add3,pincode,email,contact,seat2,seat4,seat6,opening,closing;
private Button updateBtn;
private String rid;
    public EditRestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_rest, container, false);
        if(getArguments()!=null){
            rid=getArguments().getString("rid");
        }
        initializeViews(view);
        populateViews();
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfos();
                AboutRestActivity activity=(AboutRestActivity)getActivity();
                activity.frameForFragment.setVisibility(View.GONE);
                getActivity().getSupportFragmentManager().beginTransaction().remove(EditRestFragment.this).commit();

            }
        });
        return view;
    }



    private void initializeViews(View view) {
        name=view.findViewById(R.id.RestEditName);
        add1 =view.findViewById(R.id.RestEditAdd1);
        add2=view.findViewById(R.id.RestEditAdd2);
        add3=view.findViewById(R.id.RestEditAdd3);
        pincode=view.findViewById(R.id.RestEditPin);
        email=view.findViewById(R.id.RestEditEmail);
        contact=view.findViewById(R.id.RestEditCon);
        seat2=view.findViewById(R.id.RestEdit2Seat);
        seat4=view.findViewById(R.id.RestEdit4Seat);
        seat6=view.findViewById(R.id.RestEdit6Seat);
        opening=view.findViewById(R.id.RestEditOpening);
        closing=view.findViewById(R.id.RestEditClosing);
        updateBtn=view.findViewById(R.id.RestEditUpdate);

    }
    private void populateViews(){
        AboutRestActivity activity=(AboutRestActivity)getActivity();
        RestObject obj=activity.myrestObj;
        name.setText(obj.getName());
        add1.setText(obj.getAdd1());
        add2.setText(obj.getAdd2());
        add3.setText(obj.getAdd3());
        pincode.setText(String.valueOf(obj.getPin()));
        email.setText(obj.getEmail());
        contact.setText(String.valueOf(obj.getPhno()));
        seat2.setText(String.valueOf(obj.getSeats2()));
        seat4.setText(String.valueOf(obj.getSeats4()));
        seat6.setText(String.valueOf(obj.getSeats6()));
        opening.setText(String.valueOf(obj.getOpening()));
        closing.setText(String.valueOf(obj.getClosing()));
    }
    private void saveInfos() {
        AboutRestActivity activity=(AboutRestActivity)getActivity();
        RestObject obj=activity.myrestObj;
        String nameS=name.getText().toString();
        if(!nameS.equals(obj.getName())){
            //update the firabase
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("name").setValue(nameS);
            Log.e("Fragment", "saveInfos: "+"name Changed" );
        }
        String add1S=add1.getText().toString();
        if(!add1S.equals(obj.getAdd1())){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("add1").setValue(add1S);
            Log.e("Fragment", "saveInfos: "+"add1 Changed" );
        }
        String add2S=add2.getText().toString();
        if(!add2S.equals(obj.getAdd2())){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("add2").setValue(add2S);
            Log.e("Fragment", "saveInfos: "+"add2 Changed" );
        }
        String add3S=add3.getText().toString();
        if(!add3S.equals(obj.getAdd3())){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("add3").setValue(add3S);
            Log.e("Fragment", "saveInfos: "+"add3 Changed" );
        }
        long pinL=Long.parseLong(pincode.getText().toString());
        if(pinL!=obj.getPin()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("pin").setValue(pinL);
            Log.e("Fragment", "saveInfos: "+"pin Changed" );
        }
        String emailS=email.getText().toString();
        if(!emailS.equals(obj.getEmail())){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("email").setValue(emailS);
            Log.e("Fragment", "saveInfos: "+"email Changed" );
        }
        long phno=Long.parseLong(contact.getText().toString());
        if(phno!=obj.getPhno()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("phno").setValue(phno);
            Log.e("Fragment", "saveInfos: "+"phno Changed" );
        }
        int seat2I=Integer.parseInt(seat2.getText().toString());
        if(seat2I!=obj.getSeats2()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("seats2").setValue(seat2I);
            Log.e("Fragment", "saveInfos: "+"seat2 Changed" );
        }
        int seat4I=Integer.parseInt(seat4.getText().toString());
        if(seat4I!=obj.getSeats4()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("seats4").setValue(seat4I);
            Log.e("Fragment", "saveInfos: "+"seat4 Changed" );
        }
        int seat6I=Integer.parseInt(seat6.getText().toString());
        if(seat6I!=obj.getSeats6()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("seats6").setValue(seat6I);
            Log.e("Fragment", "saveInfos: "+"seat6 Changed" );
        }
        int openI=Integer.parseInt(opening.getText().toString());
        if(openI!=obj.getOpening()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("opening").setValue(openI);
            Log.e("Fragment", "saveInfos: "+"opening Changed" );
        }
        int closeI=Integer.parseInt(closing.getText().toString());
        if(closeI!=obj.getClosing()){
            FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("closing").setValue(closeI);
            Log.e("Fragment", "saveInfos: "+"closing Changed" );
        }
    }
}
