package com.example.drestrau.Activities.User;

import android.Manifest;
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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class addRestrau extends AppCompatActivity {
    private static final int CAMERA_REQUEST =150 ;
    private static final int PICK_IMAGE_FROM_GALLERY =151 ;
    private static final String TAG ="add Restaurant" ;
    private String manId;
private EditText name;
    private EditText managerId;
    private EditText add1;
    private EditText add2;
    private EditText add3;
    private EditText pin;
    private EditText con;
    private EditText email;
    private EditText nos2;
    private EditText nos4;
    private EditText nos6;
    private EditText open;
    private EditText close;
private TextView idStatus;
private RadioButton  yes;
    private RadioButton no;
private ImageView imgOfRest;
    private ImageView cam;
private Button payBtn;
private boolean isApproved=false;
private Bitmap bitmapImage;
private LinearLayout managerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restrau);

        initialiseView();
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setupRadio();
            }
        });
    }
    private void initialiseView(){
        name=findViewById(R.id.newrest_name);
        managerId=findViewById(R.id.newrest_userId);
        add1=findViewById(R.id.newrest_add1);
        add2=findViewById(R.id.newrest_add2);
        add3=findViewById(R.id.newrest_add3);
        pin=findViewById(R.id.newrest_pincode);
        con=findViewById(R.id.newrest_contact);
        email=findViewById(R.id.newrest_qEmail);
        nos2=findViewById(R.id.newrest_no2Seats);
        nos4=findViewById(R.id.newrest_no4Seats);
        nos6=findViewById(R.id.newrest_no6Seats);
        open=findViewById(R.id.newrest_openTime);
        close=findViewById(R.id.newrest_closeTime);
        yes=findViewById(R.id.newrest_yesRadio);
        no=findViewById(R.id.newrest_noRadio);
        imgOfRest=findViewById(R.id.newrest_pic);
        payBtn=findViewById(R.id.newrest_pay_btn);
        idStatus=findViewById(R.id.newrest_statusId);
        cam=findViewById(R.id.newrest_camera);
        managerLayout=findViewById(R.id.add_rest_manager_layout);

    }
    private void pickImage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(addRestrau.this);
        View view= LayoutInflater.from(addRestrau.this).inflate(R.layout.image_choose_dialog,null);
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

    private void setupRadio(){
        if(yes.isChecked()){
            no.setChecked(false);
            managerLayout.setVisibility(View.GONE);
            manId= FirebaseAuth.getInstance().getUid();
            isApproved=true;
        }else{
            managerLayout.setVisibility(View.VISIBLE);
            yes.setChecked(false);
            manId=managerId.getText().toString();
            if(isAUser(managerId.getText().toString())){
                if(isNotAManager(managerId.getText().toString())){
                        idStatus.setText("Approved");
                        isApproved=true;
                }else{
                    idStatus.setText("Denied");
                    Toast.makeText(this, "Can't be a manager of more than one restaurant", Toast.LENGTH_SHORT).show();
                }
            }else{
                idStatus.setText("Denied");
                Toast.makeText(this, "This manager Doesn't exist. Make sure you have written the id correct", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private boolean isNotAManager(final String id){
        final boolean[] notaAManager=new boolean[]{true};
        FirebaseDatabase.getInstance().getReference("restaurants").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                RestObject obj=dataSnapshot.getValue(RestObject.class);
                if(obj.getManUid().equals(id)){
                    notaAManager[0]=false;
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
        return notaAManager[0];
    }
    private boolean isAUser(final String id){
        final boolean[] useris = new boolean[1];
        FirebaseDatabase.getInstance().getReference("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals(id)){
                    useris[0] =true;
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
        return useris[0];
    }

   private void save(){
        if(isApproved){
            String nm=name.getText().toString();
            //manId
            String a1=add1.getText().toString();
            String a2=add2.getText().toString();
            String a3=add3.getText().toString();
            long pincode=Long.parseLong(pin.getText().toString());
            long contact=Long.parseLong(con.getText().toString());
            String mail=email.getText().toString();
            int no2seat=Integer.parseInt(nos2.getText().toString());
            int no4seat=Integer.parseInt(nos4.getText().toString());
            int no6seat=Integer.parseInt(nos6.getText().toString());
            int op=Integer.parseInt(open.getText().toString());
            int cl=Integer.parseInt(close.getText().toString());

            RestObject object=new RestObject(nm,manId,a1,a2,a3,pincode,0,contact,no2seat,no4seat,no6seat,op,cl,mail);
            String key=FirebaseDatabase.getInstance().getReference("restaurants").push().getKey();
            object.setRid(key);
            if(key!=null) {
                FirebaseDatabase.getInstance().getReference("restaurants").child(object.getRid()).setValue(object);
                Toast.makeText(addRestrau.this,"New Restaurant is added",Toast.LENGTH_SHORT).show();
                saveImageAndSave(object);
                updateUserId(key);
                FirebaseAuth.getInstance().signOut();
                finish();
            }else{
                Toast.makeText(addRestrau.this, "Failed. Try Again", Toast.LENGTH_SHORT).show();
            }
        }
   }

    private void saveImageAndSave(final RestObject object) {
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
                                object.setPivtureUrl(downUri.toString());
                                FirebaseDatabase.getInstance().getReference("restaurants").child(object.getRid()).setValue(object);
                                Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                               // Toast.makeText(addRestrau.this,"Imag",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: "+"failed" );
                    Toast.makeText(addRestrau.this, "Saving image Failed", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
      //  FirebaseDatabase.getInstance().getReference("restaurants").child(object.getRid()).setValue(object);
       // Toast.makeText(addRestrau.this,"New Restaurant is added",Toast.LENGTH_SHORT).show();
        finish();

    }

    private void updateUserId(String key){
        //update the desig of the manager in the users in firebase with manId
       FirebaseDatabase.getInstance().getReference("users").child(manId).child("desig").setValue(8);
       FirebaseDatabase.getInstance().getReference("users").child(manId).child("rid").setValue(key);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        bitmapImage=(Bitmap)data.getExtras().get("data");
                        imgOfRest.setImageBitmap(bitmapImage);
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
                            imgOfRest.setImageBitmap(bitmapImage);
                            //post it  to server

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //bitmapImage=(Bitmap)data.getExtras().get("data");
                        //imgOfRest.setImageBitmap(bitmapImage);
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
