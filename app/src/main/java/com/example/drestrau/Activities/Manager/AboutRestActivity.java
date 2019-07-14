package com.example.drestrau.Activities.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drestrau.Objects.RestObject;
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

public class AboutRestActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST =1111 ;
    private static final int PICK_IMAGE_FROM_GALLERY = 1121;
    String rid;
TextView name,about,ridTv,manager,address,contact,email,_2seat,_4seat,_6seat,time;
ImageView img,dropImg,aboutEdit,aboutSave,aboutClear,aboutUndo,imgCam,editFull;
EditText aboutEv;
String newAbout;
public  FrameLayout frameForFragment;
boolean isDropOpen=false;
public  RestObject myrestObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_rest);

        rid=getIntent().getStringExtra("rid");
        initializeViews();
        fillViews();
        aboutFunctions();
        imgCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageAndSave();
            }
        });
        editFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogForEdit();
            }
        });
    }



    private void initializeViews() {
        name=findViewById(R.id.RestName);
        about=findViewById(R.id.RestAbout);
        ridTv=findViewById(R.id.RestRid);
        manager=findViewById(R.id.RestManName);
        address=findViewById(R.id.RestAddress);
        contact=findViewById(R.id.RestContactNo);
        email=findViewById(R.id.RestEmail);
        _2seat=findViewById(R.id.Rest2Seaters);
        _4seat=findViewById(R.id.Rest4Seaters);
        _6seat=findViewById(R.id.Rest6Seaters);
        time=findViewById(R.id.RestTime);
        img=findViewById(R.id.RestImg);
        dropImg=findViewById(R.id.RestDropdownBtn);
        aboutEdit=findViewById(R.id.RestAbouteditBtn);
        aboutSave=findViewById(R.id.RestAboutSaveBtn);
        aboutClear=findViewById(R.id.RestAboutClearAll);
        aboutUndo=findViewById(R.id.RestAboutUndo);
        aboutEv=findViewById(R.id.RestAboutEditView);
        imgCam=findViewById(R.id.RestImgCam);
        editFull=findViewById(R.id.RestEditFull);
        frameForFragment=findViewById(R.id.RestFragment);
    }
    private void aboutFunctions() {
        dropImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDropOpen){
                    isDropOpen=true;
                    dropImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp));
                    about.setMaxLines(500);
                    aboutEdit.setVisibility(View.VISIBLE);
                }else{
                    isDropOpen=false;
                    dropImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp));
                    about.setMaxLines(1);
                    aboutEdit.setVisibility(View.GONE);
                }
            }
        });
        aboutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutEdit.setVisibility(View.GONE);
                aboutSave.setVisibility(View.VISIBLE);
                aboutClear.setVisibility(View.VISIBLE);
                aboutUndo.setVisibility(View.VISIBLE);
                aboutEv.setVisibility(View.VISIBLE);
                newAbout=about.getText().toString();
                aboutEv.setText(newAbout);

            }
        });
        aboutSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutEdit.setVisibility(View.VISIBLE);
                aboutSave.setVisibility(View.GONE);
                aboutClear.setVisibility(View.GONE);
                aboutUndo.setVisibility(View.GONE);
                aboutEv.setVisibility(View.GONE);
                newAbout=aboutEv.getText().toString();
                FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("about").setValue(newAbout);
                Toast.makeText(AboutRestActivity.this,"About Updated",Toast.LENGTH_SHORT).show();
            }
        });
        final String[] tempStr = new String[1];
        aboutClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempStr[0] =aboutEv.getText().toString();
                aboutEv.setText("");
            }
        });
        aboutUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutEv.setText(tempStr[0]);
            }
        });


    }

    private void fillViews() {
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RestObject obj=dataSnapshot.getValue(RestObject.class);
                if(obj!=null){
                    myrestObj=obj;
                    name.setText(obj.getName());
                    if(obj.getAbout()!=null){
                        about.setText(obj.getAbout());
                    }
                    ridTv.setText(rid);
                    setName(obj.getManUid());
                    address.setText(obj.getAdd1()+","+obj.getAdd2()+","+obj.getAdd3()+","+obj.getPin());
                    contact.setText(String.valueOf(obj.getPhno()));
                    email.setText(obj.getEmail());
                    _2seat.setText(String.valueOf(obj.getSeats2()));
                    _4seat.setText(String.valueOf(obj.getSeats4()));
                    _6seat.setText(String.valueOf(obj.getSeats6()));
                    time.setText(obj.getOpening() + ":00-" + obj.getClosing() + ":00");
                    if(obj.getpictureUrl()!=null){
                        Glide.with(getApplicationContext())
                                .load(obj.getpictureUrl())
                                .into(img);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setName(String manUid) {
        FirebaseDatabase.getInstance().getReference("users").child(manUid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nm=dataSnapshot.getValue(String.class);
                if(nm!=null){
                    manager.setText(nm);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialogForEdit() {
        //populate Fragment
        Bundle bundle=new Bundle();
        bundle.putString("rid",rid);
        frameForFragment.setVisibility(View.VISIBLE);
        EditRestFragment fragment=new EditRestFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.RestFragment,fragment).commit();
    }

    private void selectImageAndSave() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AboutRestActivity.this);
        //  final AlertDialog dialog=builder.create();
        View view= LayoutInflater.from(AboutRestActivity.this).inflate(R.layout.image_choose_dialog,null);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        Bitmap bitmapImage=(Bitmap)data.getExtras().get("data");
                        img.setImageBitmap(bitmapImage);
                        saveImage(bitmapImage);
                    }
                }
                break;
            }
            case PICK_IMAGE_FROM_GALLERY:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        Uri uri = data.getData();

                        try {
                            Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                            // Log.d(TAG, String.valueOf(bitmap));
                            img.setImageBitmap(bitmapImage);
                            saveImage( bitmapImage);
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
    private void saveImage(Bitmap image){
        if(image==null){
            Toast.makeText(this, "Image Isn't Picked. HardWare Problem", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseStorage storage=FirebaseStorage.getInstance();
        final StorageReference storeRef=storage.getReference().child("rest_pics").child(rid+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);
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
                            FirebaseDatabase.getInstance().getReference("restaurants")
                                    .child(rid).child("pictureUrl")
                                    .setValue(downUri != null ? downUri.toString() : null);
                            Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("This", "onFailure: "+"failed" );
                Toast.makeText(AboutRestActivity.this, "Adding Rest Image Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


