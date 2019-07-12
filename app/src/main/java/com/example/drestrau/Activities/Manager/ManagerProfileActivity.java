package com.example.drestrau.Activities.Manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class ManagerProfileActivity extends AppCompatActivity {
String staffId,rid;
ImageView img,datePicker;
TextView name,gender,age,doj,desig,address,pincode,con1,con2,email,sal,uid,sid,dob,edit,save;
//for edit mode
EditText fName,mName,lName,add1,add2,add3,pincodeEv,con1Ev,con2Ev,emailEv,salaryEv;
RadioButton male,female;
Spinner desigSpin;
staffObject myData;
LinearLayout normalMode,editMode;
int desigInt=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);
        staffId=getIntent().getStringExtra("staffId");
        sid.setText(staffId);
        rid=getIntent().getStringExtra("rid");
        initializeViews();
        getDetails();
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myData!=null){
                    editMode();
                }else{
                    Toast.makeText(ManagerProfileActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void initializeViews() {
         img=findViewById(R.id.man_profile_img);
         name=findViewById(R.id.man_profile_nameTv);
         gender=findViewById(R.id.man_profile_genderTv);
         age=findViewById(R.id.man_profile_ageTv);
         doj=findViewById(R.id.man_profile_DOJ_Tv);
         desig=findViewById(R.id.man_profile_desig_tv);
         address=findViewById(R.id.man_profile_addressTv);
         pincode=findViewById(R.id.man_profile_pincodeTv);
         con1=findViewById(R.id.man_profile_contact1);
         con2=findViewById(R.id.man_profile_contact2);
         email=findViewById(R.id.man_profile_emailTv);
         sal=findViewById(R.id.man_profile_salary_Tv);
         uid=findViewById(R.id.man_profile_uidTv);
         sid=findViewById(R.id.man_profile_staffIdTv);

         normalMode=findViewById(R.id.man_pro_normal);
         editMode=findViewById(R.id.man_pro_edit);
//for edit mode
        edit=findViewById(R.id.man_profile_editBtn);
        save=findViewById(R.id.man_profile_saveBtn);
         fName=findViewById(R.id.man_profile_fNameEv);
         mName=findViewById(R.id.man_profile_mNameEv);
         lName=findViewById(R.id.man_profile_lNameEv);
         add1=findViewById(R.id.man_profile_add1_Ev);
         add2=findViewById(R.id.man_profile_add2Ev);
         add3=findViewById(R.id.man_profile_add3_Ev);
         pincodeEv=findViewById(R.id.man_profile_pincodeEv);
         con1Ev=findViewById(R.id.man_profile_contact1Ev);
         con2Ev=findViewById(R.id.man_profile_contact2Ev);
         emailEv=findViewById(R.id.man_profile_emailEv);
         salaryEv=findViewById(R.id.man_profile_salary_Ev);
         male=findViewById(R.id.man_profile_maleRb);
         female=findViewById(R.id.man_profile_femaleRb);
         desigSpin=findViewById(R.id.man_profile_desig_spinner);
        datePicker=findViewById(R.id.man_profile_DOB_DatePicker);
        dob=findViewById(R.id.man_profile_DOBTv);
    }
    private void getDetails(){
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(staffId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staffObject obj=dataSnapshot.getValue(staffObject.class);
                if(obj!=null){
                    myData=new staffObject(obj.getName1(),obj.getName2(),obj.getName3(),obj.getGender(),obj.getSalary(),obj.getDob(),obj.getDoj(),obj.getAddress1(),obj.getAddress2(),obj.getAddress3(),obj.getPincode(),obj.getEmail(),obj.getContact1(),obj.getContact2(),obj.getDesignation());
                    myData.setPicUrl(obj.getPicUrl());
                    name.setText(obj.getName1() + " " + obj.getName2() + " " + obj.getName3());
                    int gen=obj.getGender();
                    if(gen==0){
                        gender.setText("Male");
                    }else if(gen==1){
                        gender.setText("Female");
                    }
                    String dateOfBirth= utilityClass.getDate(obj.getDob());
                    dob.setText(dateOfBirth);
                    String[] date=dateOfBirth.split("/");
                    int dayDob=Integer.parseInt(date[0]);
                    int monthDob=Integer.parseInt(date[1]);
                    int yearDob=Integer.parseInt(date[2]);
                    age.setText(getAge(yearDob,monthDob,dayDob));
                    doj.setText(utilityClass.getDate(obj.getDoj()));
                    String[] desigs=getResources().getStringArray(R.array.designation);
                    desig.setText(desigs[obj.getDesignation()]);
                    address.setText(obj.getAddress1()+","+obj.getAddress2()+","+obj.getAddress3());
                    pincode.setText(String.valueOf(obj.getPincode()));
                    con1.setText(String.valueOf(obj.getContact1()));
                    con2.setText(String.valueOf(obj.getContact2()));
                    email.setText(obj.getEmail());
                    sal.setText(String.valueOf(obj.getSalary()));
                    uid.setText(obj.getUid());
                    sid.setText(obj.getSid());
                    if(obj.getPicUrl()!=null){
                        Glide.with(ManagerProfileActivity.this)
                                .load(obj.getPicUrl())
                                .into(img);
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
    private void editMode() {
        normalMode.setVisibility(View.GONE);
        editMode.setVisibility(View.VISIBLE);
        setPreviousViews();
        setupViews();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        String name1=fName.getText().toString();
        String name2=fName.getText().toString();
        String name3=lName.getText().toString();
        String add1St=add1.getText().toString();
        String add2St=add2.getText().toString();
        String add3St=add3.getText().toString();
        long pinc=Long.parseLong(pincodeEv.getText().toString());
        long ph1=Long.parseLong(con1Ev.getText().toString());
        long ph2=Long.parseLong(con2Ev.getText().toString());
        String email=emailEv.getText().toString();
        int sal=Integer.parseInt(salaryEv.getText().toString());
        int gen;
        if(male.isChecked()){
            gen=0;
        }else{
            gen=1;
        }
        int desg=desigInt;
        staffObject newStaff=new staffObject(name1,name2,name3,gen,sal,,Long.parseLong(doj.getText().toString()),add1St,add2St,add3St,pinc,email,ph1,ph2,desg);
        newStaff.setSid(staffId);
        newStaff.setRid(rid);
        newStaff.setUid(uid.getText().toString());
        newStaff.setPicUrl(myData.getPicUrl());

        FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(staffId).setValue(newStaff);
        Toast.makeText(this, "Staff Data Updated", Toast.LENGTH_SHORT).show();
        editMode.setVisibility(View.GONE);
        normalMode.setVisibility(View.VISIBLE);
        recreate();
    }

    private void setupViews() {
        final ArrayAdapter<CharSequence> des=ArrayAdapter.createFromResource(this,R.array.designation,android.R.layout.simple_spinner_item);
        des.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desigSpin.setAdapter(des);
        desigSpin.setSelection(desigInt);
        desigSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                desigInt=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //desigInt=-1;
            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    male.setChecked(true);
                    female.setChecked(false);
                }else{
                    male.setChecked(false);
                    female.setChecked(true);
                }
            }
        });
    }
    private void setPreviousViews() {
        fName.setText(myData.getName1());
        mName.setText(myData.getName2());
        lName.setText(myData.getName3());
        add1.setText(myData.getAddress1());
        add2.setText(myData.getAddress2());
        add3.setText(myData.getAddress3());
        pincodeEv.setText(String.valueOf(myData.getPincode()));
        con1Ev.setText(String.valueOf(myData.getContact1()));
        con2Ev.setText(String.valueOf(myData.getContact2()));
        emailEv.setText(myData.getEmail());
        salaryEv.setText(String.valueOf(myData.getSalary()));
        desigInt=myData.getDesignation();
        int gen=myData.getGender();
        if(gen==0){
            male.setChecked(true);
            female.setChecked(false);
        }
        else{
            male.setChecked(false);
            female.setChecked(true);
        }

    }
}
