package com.example.drestrau.Activities.Manager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.drestrau.Objects.attendanceObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addStaff extends AppCompatActivity {
    private static final String TAG ="add Staff" ;
    private EditText name;
    private EditText name2;
    private EditText name3;
    private EditText add1;
    private EditText add2;
    private EditText add3;
    private EditText con1;
    private EditText con2;
    private EditText salary;
    private EditText email;
    private EditText pincode;

private TextView doj;
    private TextView dob;
private RadioButton male;
    private RadioButton female;
private ImageView datepick;
    private ImageView pic;
    private ImageView cam;
private Spinner desig;

private String rid;
private String staffUid;
private DatePicker pickAdate;
private int designation=0;
private long mdoj;
    private long mdob;
private int gender;
private Bitmap bitmapImage;
    private static final int CAMERA_REQUEST =1000 ;
    private static final int PICK_IMAGE_FROM_GALLERY =1100 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        initializeViews();

        //helper=new dbHelper(this);
        Intent intent=getIntent();
        rid=intent.getStringExtra("rid");
        staffUid=intent.getStringExtra("staffUid");


        setupCalenders();
        setupRadioButtons();
        setupSpinner();
        populateform();
        cam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(addStaff.this);
              //  final AlertDialog dialog=builder.create();
                View view= LayoutInflater.from(addStaff.this).inflate(R.layout.image_choose_dialog,null);
                builder.setTitle("Choose the source");
                builder.setView(view);
                final AlertDialog dialog=builder.create();
                ConstraintLayout cam,gal;
                cam=view.findViewById(R.id.image_choose_camera);
                gal=view.findViewById(R.id.image_choose_gallery);
                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //choose image from cam
                        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,CAMERA_REQUEST);
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
        });
    }

    private void initializeViews(){
        name=(EditText)findViewById(R.id.staff_name1XML);
        name2=(EditText)findViewById(R.id.staff_name2XML);
        name3=(EditText)findViewById(R.id.staff_name3XML);
        pincode=findViewById(R.id.staff_pinCode);
        email=(EditText)findViewById(R.id.staff_email);
        dob=findViewById(R.id.staff_dobText);
        pickAdate=findViewById(R.id.staff_dobCal);
        male=findViewById(R.id.staffMaleRadio);
        female=findViewById(R.id.staffFemaleRadio);
        add1=(EditText)findViewById(R.id.staff_add1);
        add2=(EditText)findViewById(R.id.staff_add2);
        add3=findViewById(R.id.staff_add3);
        con1=(EditText)findViewById(R.id.staff_no1);
        con2=(EditText)findViewById(R.id.staff_no2);
        salary=(EditText)findViewById(R.id.staff_salary);
        doj=findViewById(R.id.staff_doj);
        desig=findViewById(R.id.add_staff_spinner);
        datepick=findViewById(R.id.addstaffDatePicker);
        pic=findViewById(R.id.staff_add_img);
        cam=findViewById(R.id.staff_add_camera);

    }
    private void setupRadioButtons() {
        if (male.isChecked()) {
            female.setChecked(false);
            gender = 0;
        } else
        {       male.setChecked(false);
                gender=1;
        }
    }
    private void setupCalenders(){
        datepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(addStaff.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                doj.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                DateFormat date= new SimpleDateFormat("dd-mm-yyyy");
                                try {
                                    Date date1=date.parse(doj.getText().toString());
                                    mdoj=date1.getTime()/1000;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        /*
        pickAdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(addStaff.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dob.setText("DOB : "+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                DateFormat date= new SimpleDateFormat("dd-mm-yyyy");
                                try {
                                    String d=dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                                    Date date1=date.parse(d);
                                    mdob=date1.getTime()/1000;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        */
    }
    private void setupSpinner(){
        final ArrayAdapter<CharSequence> des=ArrayAdapter.createFromResource(this,R.array.designation,android.R.layout.simple_spinner_item);
        des.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        desig.setAdapter(des);
        desig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                designation=position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                designation=-1;
            }
        });
    }
    private void populateform(){

        //TODO extract the data of the user with _uid=uid
        FirebaseDatabase.getInstance().getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(staffUid)){
                    users usr=dataSnapshot.getValue(users.class);
                     if(usr.getName()!=null)
                     {      String[] split=usr.getName().split("\\s+");
                            if(split.length>=3) {
                               name.setText(split[0]);
                               name2.setText(split[1]);
                               name3.setText(split[2]);
                            }
                          else if(split.length==2){
                                name.setText(split[0]);
                                name2.setText(split[1]);
                            }
                            else if(split.length==1){
                                 name.setText(split[0]);
                            }
                     }

                     String adr1=usr.getAddress1();
                     String adr2=usr.getAddress2();
                     add1.setText(adr1);
                     add3.setText(adr2);
                     long ph1=usr.getPhno1();
                     long ph2=usr.getPhno2();
                     con1.setText(String.valueOf(ph1));
                     con2.setText(String.valueOf(ph2));
                     email.setText(usr.getEmail());

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
    private void save(){
        String nm1=name.getText().toString();
        String nm2=name2.getText().toString();
        String nm3=name3.getText().toString();


        int mYear=pickAdate.getYear();
        int mMonth=pickAdate.getMonth();
        int mDay=pickAdate.getDayOfMonth();
        String dayob=mDay+"/"+mMonth+1+"/"+mYear;
        DateFormat format=new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date date=format.parse(dayob);
            mdob=date.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //gender
        //mdob
        //uid is already here
        long ph1=Long.parseLong(con1.getText().toString());
        long ph2=Long.parseLong(con2.getText().toString());
        String ad1=add1.getText().toString();
        String ad2=add2.getText().toString();
        String ad3=add3.getText().toString();
        long pinc=Long.parseLong(pincode.getText().toString());
        String mail=email.getText().toString();
        //mdoj
        //designation
        int sl=Integer.parseInt(salary.getText().toString());


        final staffObject newStaff=new staffObject(nm1,nm2,nm3,gender,sl,mdob,mdoj,ad1,ad2,ad3,pinc,mail,ph1,ph2,designation);
        final String staffKey=FirebaseDatabase.getInstance().getReference("staffs").child(rid).push().getKey();
        newStaff.setSid(staffKey);
        newStaff.setUid(staffUid);
        newStaff.setRid(rid);
        if(staffKey!=null) {
            FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(staffKey).setValue(newStaff);
            if(bitmapImage!=null)

            {
                FirebaseStorage storage=FirebaseStorage.getInstance();
                final StorageReference storeRef=storage.getReference().child("staff_pics").child(rid).child(staffKey+".jpg");
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
                                    newStaff.setPicUrl(downUri.toString());
                                    FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(staffKey).setValue(newStaff);
                                    Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: "+"failed" );
                        Toast.makeText(addStaff.this, "Adding staff Failed", Toast.LENGTH_SHORT).show();
                        }
                });
            }
            Toast.makeText(addStaff.this, "New Staff Added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(addStaff.this, "Some error has occurred. Retry", Toast.LENGTH_SHORT).show();
        }
        //TODO insert in the Stafftable with values (_uid,fullname,contact1,contact2,add1,add2,DOJ,salary)

//    Toast.makeText(this, "Staff entered", Toast.LENGTH_SHORT).show();
//TODO update the user table with desig =designamtion and the rid of user and the name

        FirebaseDatabase.getInstance().getReference("users").child(staffUid).child("desig").setValue(designation);
        FirebaseDatabase.getInstance().getReference("users").child(staffUid).child("rid").setValue(rid);
        String fulllName=nm1+" "+nm2+" "+nm3;
        FirebaseDatabase.getInstance().getReference("users").child(staffUid).child("name").setValue(fulllName);
//TODO inserting in the attendence table with data (uid,presence(0),salary_uptonow(0),absent(0))
        int maxday=Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        attendanceObject object=new attendanceObject(0,0,maxday,mdoj,0);
        FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(staffKey).setValue(object);
//after the attendence will be made..
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        bitmapImage=(Bitmap)data.getExtras().get("data");
                        pic.setImageBitmap(bitmapImage);
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
                            pic.setImageBitmap(bitmapImage);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_staff,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_new_staff_save:{
                save();
                finish();
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
}
