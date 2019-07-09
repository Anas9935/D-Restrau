package com.example.drestrau.Activities.User;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.Manager.EditMenuActivity;
import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.Objects.attendanceObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST =1058 ;
    private static final int PICK_IMAGE_FROM_GALLERY =1059 ;
    String uid;
String sid,rid,picUrl;
int salary;
TextView name,email,phno1,phno2,address1,address2,id,pin;
Toolbar toolbar;
ImageView img,cam;
TextView pres,abs,sal,register_rest;
LinearLayout attendanceLayout;
Bitmap bitmapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uid= FirebaseAuth.getInstance().getUid();
        sid=getIntent().getStringExtra("sid");
        rid=getIntent().getStringExtra("rid");
        picUrl=getIntent().getStringExtra("picUrl");
        salary=getIntent().getIntExtra("salary",-1);
        initialiseViews();
        getId();


        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_profile_edit:{
                        if(sid!=null){
                            cam.setVisibility(View.VISIBLE);
                        }
                        editDialog();
                        return true;
                    }
                    default:return  ProfileActivity.super.onMenuItemSelected(0,item);
                }
            }
        });

        collapsingToolbar.setTitle("");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Profile");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });




        if(sid!=null){
            attendanceLayout.setVisibility(View.VISIBLE);
            getImageAndAttendance(sid);
            register_rest.setVisibility(View.INVISIBLE);
        }else{
            register_rest.setVisibility(View.VISIBLE);
        }
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        register_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, addRestrau.class);
                startActivity(intent);
            }
        });
    }


    private void getImageAndAttendance(String sid) {
       if(picUrl!=null){
           Glide.with(ProfileActivity.this)
                   .load(picUrl)
                   .into(img);
       }
        FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendanceObject obj=dataSnapshot.getValue(attendanceObject.class);
                if(obj!=null){
                    pres.setText(String.valueOf(obj.getDayCount()));
                    long msDiff = Calendar.getInstance().getTimeInMillis() -obj.getLastPaidDate()*1000;
                    long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);

                    abs.setText(String.valueOf(daysDiff-obj.getDayCount()));
                    float salPerDay=(float)salary/obj.getMaxDayOfMonth();
                    int salnow=(int) salPerDay*obj.getDayCount();
                    sal.setText(String.valueOf(salnow));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void SelectImage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
        View view= LayoutInflater.from(ProfileActivity.this).inflate(R.layout.image_choose_dialog,null);
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

    private void initialiseViews(){
        name=findViewById(R.id.profile_name);
        email=findViewById(R.id.profile_email);
        phno1=findViewById(R.id.profile_ph1);
        phno2=findViewById(R.id.profile_ph2);
        address1=findViewById(R.id.profile_address1);
        address2=findViewById(R.id.profile_address2);
        id=findViewById(R.id.profile_uid);
        pin=findViewById(R.id.profile_address_pin);
        img=findViewById(R.id.profile_pic);

        pres=findViewById(R.id.profile_attendance_present);
        abs=findViewById(R.id.profile_attendance_absent);
        sal=findViewById(R.id.profile_attendance_salary);
        attendanceLayout=findViewById(R.id.profile_attendance_layout);
        cam=findViewById(R.id.profile_cam);
        register_rest=findViewById(R.id.profile_reg_rest);
    }
    private void getId(){
        FirebaseDatabase.getInstance().getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(uid)){
                    users user=dataSnapshot.getValue(users.class);
                    if (user != null) {
                        name.setText(user.getName());
                        email.setText(user.getEmail());
                        phno1.setText(String.valueOf(user.getPhno1()));
                        phno2.setText(String.valueOf(user.getPhno2()));
                        address1.setText(user.getAddress1());
                        address2.setText(user.getAddress2());
                        id.setText(user.getUid());
                        pin.setText(String.valueOf(user.getPincode()));
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
    private void editDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("EDIT PROFILE");
        View view= LayoutInflater.from(this).inflate(R.layout.edit_dialog,null);
        builder.setView(view);
        final EditText nm,ph1,ph2,ad1,ad2,pn;
        Button update;
        TextView message;
        message=view.findViewById(R.id.edit_dialog_message);
        nm=view.findViewById(R.id.edit_dialog_name);
        ph1=view.findViewById(R.id.edit_dialog_ph1);
        ph2=view.findViewById(R.id.edit_dialog_ph2);
        ad1=view.findViewById(R.id.edit_dialog_add1);
        ad2=view.findViewById(R.id.edit_dialog_add2);
        pn=view.findViewById(R.id.edit_dialog_pin);
        update=view.findViewById(R.id.edit_dialog_update);

        if(sid!=null){
            message.setVisibility(View.VISIBLE);
        }else{
            message.setVisibility(View.GONE);
        }

        if (!name.getText().toString().equals("null")) {
            nm.setText(name.getText().toString());
        }
        if(!phno1.getText().toString().equals("0")){
            ph1.setText(phno1.getText().toString());
        }
        if(!phno2.getText().toString().equals("0")){
            ph2.setText(phno2.getText().toString());
        }
        if(!pin.getText().toString().equals("0")){
            pn.setText(pin.getText().toString());
        }
        if(!ad1.getText().toString().equals("null")){
            ad1.setText(address1.getText().toString());
        }
        if(!ad2.getText().toString().equals("null")){
            ad2.setText(address2.getText().toString());
        }

        final AlertDialog dialog=builder.create();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n=nm.getText().toString();
                long p1=Long.parseLong(ph1.getText().toString());
                long p2=Long.parseLong(ph2.getText().toString());
                String a1=ad1.getText().toString();
                String a2=ad2.getText().toString();
                long pin=Long.parseLong(pn.getText().toString());

                    if(bitmapImage!=null)
                    {
                        FirebaseStorage storage=FirebaseStorage.getInstance();
                        final StorageReference storeRef=storage.getReference().child("staff_pics").child(rid).child(sid+".jpg");
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
                                            String url=downUri.toString();
                                            FirebaseDatabase.getInstance().getReference("staffs").child(rid).child(sid).child("picUrl").setValue(url);
                                            Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                             //   Log.e(TAG, "onFailure: "+"failed" );
                                Toast.makeText(ProfileActivity.this, "Image cant be updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                FirebaseDatabase.getInstance().getReference("users").child(uid).child("name").setValue(n);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("phno1").setValue(p1);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("phno2").setValue(p2);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("address1").setValue(a1);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("address2").setValue(a2);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("pincode").setValue(pin);
                Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();

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
                        //  bitmapImage=(Bitmap)data.getExtras().get("data");
                        // picForFood.setImageBitmap(bitmapImage);
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return  true;
    }
}
