package com.example.drestrau;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drestrau.Adapters.MyOrderAdapter;
import com.example.drestrau.Objects.attendanceObject;
import com.example.drestrau.RoomRelated.MyOrderObject;
import com.example.drestrau.RoomRelated.OrderDatabase;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestingActivity extends AppCompatActivity {
Button rest,food;
String key;
ArrayList<MyOrderObject> list;
MyOrderAdapter adapter;
RecyclerView rv;
    private String TAG="TestingActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);


        rest=findViewById(R.id.test_rest_btn);
        food=findViewById(R.id.test_food_btn);
        rv=findViewById(R.id.testingRecycle);

        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }


    private void updateAttendance(String sid) {
        Calendar present=Calendar.getInstance();
        int year=present.get(Calendar.YEAR);
        int month=present.get(Calendar.MONTH)+1;
        int day=present.get(Calendar.DAY_OF_MONTH);
        int maxDay=present.getActualMaximum(Calendar.MONTH);
        FirebaseDatabase.getInstance().getReference("attendance").child("restId").child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.getKey());
                attendanceObject obj = dataSnapshot.getValue(attendanceObject.class);
                Log.e(TAG, "onDataChange: " + obj.getMaxDayOfMonth());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void uploadImage(){
        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference storeRef=storage.getReference().child("staff_pics").child("restId").child("staffId"+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.check_pastel);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
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
                          //  newStaff.setPicUrl(downUri.toString());
                            Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: "+"failed" );
            }
        });
    }

    private void pickImage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(TestingActivity.this);

        View view= LayoutInflater.from(TestingActivity.this).inflate(R.layout.image_choose_dialog,null);
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
                Toast.makeText(TestingActivity.this, "Camera", Toast.LENGTH_SHORT).show();
               dialog.dismiss();
            }
        });
        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //choose imaeg from gallery
                Toast.makeText(TestingActivity.this, "Gallery", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog.show();
    }


}
