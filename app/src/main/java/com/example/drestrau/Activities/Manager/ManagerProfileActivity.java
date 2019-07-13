package com.example.drestrau.Activities.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.User.Reservations;
import com.example.drestrau.Activities.User.addRestrau;
import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class ManagerProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST =1100 ;
    private static final int PICK_IMAGE_FROM_GALLERY =1101 ;
    private String staffId;
    private String rid;
private ImageView img;
    private ImageView datePicker;
    private ImageView cam;
private TextView name;
    private TextView gender;
    private TextView age;
    private TextView doj;
    private TextView desig;
    private TextView address;
    private TextView pincode;
    private TextView con1;
    private TextView con2;
    private TextView email;
    private TextView sal;
    private TextView uid;
    private TextView sid;
    private TextView dob;
    private TextView edit;
    private TextView save;
//for edit mode
private EditText fName;
    private EditText mName;
    private EditText lName;
    private EditText add1;
    private EditText add2;
    private EditText add3;
    private EditText pincodeEv;
    private EditText con1Ev;
    private EditText con2Ev;
    private EditText emailEv;
    private EditText salaryEv;
private RadioButton male;
    private RadioButton female;
private Spinner desigSpin;
private staffObject myData;
private LinearLayout normalMode;
    private LinearLayout editMode;
private int desigInt=-1;
private long newDOB;
private Bitmap bitmapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_profile);
        staffId=getIntent().getStringExtra("staffId");
        rid=getIntent().getStringExtra("rid");
        initializeViews();
        sid.setText(staffId);
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
        cam=findViewById(R.id.man_profile_cam);
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

        Integer ageInt = age;

            return ageInt.toString();
    }
    private void editMode() {
        normalMode.setVisibility(View.GONE);
        editMode.setVisibility(View.VISIBLE);
        cam.setVisibility(View.VISIBLE);
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
        String name2=mName.getText().toString();
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
        final staffObject newStaff=new staffObject(name1,name2,name3,gen,sal,newDOB,myData.getDoj(),add1St,add2St,add3St,pinc,email,ph1,ph2,desg);
        newStaff.setSid(staffId);
        newStaff.setRid(rid);
        newStaff.setUid(uid.getText().toString());
        newStaff.setPicUrl(myData.getPicUrl());

        FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(staffId).setValue(newStaff).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ManagerProfileActivity.this, "Staff Data Updated", Toast.LENGTH_SHORT).show();
                saveImageAndSave(newStaff);
                editMode.setVisibility(View.GONE);
                normalMode.setVisibility(View.VISIBLE);
                cam.setVisibility(View.GONE);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


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

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ManagerProfileActivity.this,android.R.style.Theme_DeviceDefault_Dialog,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dob.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                                try {
                                    DateFormat date= new SimpleDateFormat("dd-MM-yyyy");
                                    Date date1=date.parse(dob.getText().toString());
                                    //date is wrong
                                    Log.e("this", "onDateSet: "+date1.toString()+"  "+date1.getTime() );
                                    newDOB=date1.getTime()/1000;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
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

        try {
            DateFormat date= new SimpleDateFormat("dd/MM/yyyy");
            Date date1=date.parse(dob.getText().toString());
            newDOB=date1.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void pickImage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ManagerProfileActivity.this);
        View view= LayoutInflater.from(ManagerProfileActivity.this).inflate(R.layout.image_choose_dialog,null);
        builder.setTitle("Choose the source");
        builder.setView(view);
        final AlertDialog dialog=builder.create();
        ConstraintLayout cam,gal;
        cam=view.findViewById(R.id.image_choose_camera);
        gal=view.findViewById(R.id.image_choose_gallery);
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //permissions
                String[] CAMERA_PERMISSION = {android.Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(CAMERA_PERMISSION, 001);
                }
                //choose image from cam
                else{
                    Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMERA_REQUEST);

                }
                dialog.dismiss();
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose imaeg from gallery
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_FROM_GALLERY);
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog.show();
    }
    private void saveImageAndSave(final staffObject object) {
        if(bitmapImage!=null)

        {
            FirebaseStorage storage=FirebaseStorage.getInstance();
            final StorageReference storeRef=storage.getReference().child("rest_pics").child(object.getRid()+".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            byte[] data = baos.toByteArray();

            final UploadTask uploadTask = storeRef.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return storeRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downUri = task.getResult();
                                object.setPicUrl(downUri.toString());
                                FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(staffId).child("picUrl").setValue(downUri.toString());
                                Log.e("Final URL", "onComplete: Url: " + downUri.toString());

                              //  Toast.makeText(ManagerProfileActivity.this,"New Restaurant is added",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("this", "onFailure: "+"failed" );
                    Toast.makeText(ManagerProfileActivity.this, "Adding image Failed", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        finish();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        bitmapImage=(Bitmap)data.getExtras().get("data");
                        img.setImageBitmap(bitmapImage);
                    }
                }
                break;
            }
            case PICK_IMAGE_FROM_GALLERY:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        Uri uri = data.getData();

                        try {
                            bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            // Log.d(TAG, String.valueOf(bitmap));
                            img.setImageBitmap(bitmapImage);
                            //post it  to server

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==001&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent,CAMERA_REQUEST);
            //dialog.dismiss();
        }
    }
}
