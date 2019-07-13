package com.example.drestrau.Activities.Manager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.drestrau.Adapters.SimpleMenuAdapter;
import com.example.drestrau.Objects.menuObject;
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
import java.util.ArrayList;

public class EditMenuActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST =1001 ;
    private static final int PICK_IMAGE_FROM_GALLERY =1002 ;
    private static final String TAG ="EditMenuActivity" ;
    private String rid;
private ListView lv;
private ArrayList<menuObject> list;
private Button add;
    private Button set;
    private Button fullOffer;
private EditText offer;
private SimpleMenuAdapter adapter;
private LinearLayout linear;
private boolean ofr=false;
private Bitmap bitmapImage;

private ImageView picForFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        rid=getIntent().getStringExtra("rid");

        lv=findViewById(R.id.edit_menu_Lv);
        add=findViewById(R.id.edit_menu_add_btn);
        set=findViewById(R.id.edit_menu_setBtn);
        fullOffer=findViewById(R.id.edit_offer_btn);
        offer=findViewById(R.id.edit_menu_offer);
        linear=findViewById(R.id.edit_fullOffer_lv);

        list=new ArrayList<>();
        adapter=new SimpleMenuAdapter(this,list);
        lv.setAdapter(adapter);
        getMenuItems();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFragment(0,0);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialogFragment(1,position);
            }
        });
        fullOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ofr){
                    ofr=false;
                    linear.setVisibility(View.INVISIBLE);
                    fullOffer.setVisibility(View.VISIBLE);
                }else{
                    ofr=true;
                    linear.setVisibility(View.VISIBLE);
                    fullOffer.setVisibility(View.INVISIBLE);
                }
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOfferForFull();
            }
        });
    }
    private void setOfferForFull(){
        int off=Integer.parseInt(offer.getText().toString());
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("offer").setValue(off);
    }
    private void openDialogFragment(final int mode, final int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.dialog_new_food_item,null);
        builder.setView(view);

        final EditText name,info,price,offer;
        final ImageView cam,typ;
        name=view.findViewById(R.id.new_food_name);
        info=view.findViewById(R.id.new_food_info);
        price=view.findViewById(R.id.new_food_price);
        picForFood=view.findViewById(R.id.new_food_img);
        cam=view.findViewById(R.id.new_food_camera);
        typ=view.findViewById(R.id.new_food_type);
        offer=view.findViewById(R.id.new_food_offer);

        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        final int[] type = new int[]{0}; //0-veg 1-nonveg
        if(mode==1){
            type[0]=list.get(position).getType();
        }
        typ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(type[0]==0){
                   typ.setImageResource(R.drawable.nonveg);
                   type[0]=1;
               }else
                if(type[0]==1){
                    typ.setImageResource(R.drawable.veg);
                    type[0]=0;
                }
            }
        });
        if(list.get(position).getPicUrl()!=null){
            Glide.with(EditMenuActivity.this)
                    .load(list.get(position).getPicUrl())
                    .into(picForFood);
        }
        if(mode==1){
            final menuObject current=list.get(position);
            name.setText(current.getName());
            info.setText(current.getInfo());
            price.setText(String.valueOf((int) current.getPrice()));
            offer.setText(String.valueOf(current.getOffer()));
            if(current.getType()==0){
                typ.setImageResource(R.drawable.veg);
                type[0]=0;
            }else{
                typ.setImageResource(R.drawable.nonveg);
                type[0]=1;
            }
        }


        if(mode==0) {       //for new menu item

            builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String nm = name.getText().toString();
                    String inf = info.getText().toString();
                    int prc = Integer.parseInt(price.getText().toString());
                    int off=Integer.parseInt(offer.getText().toString());

                    final menuObject object = new menuObject(nm, inf, prc, 0, off, null, type[0]);
                    String key = FirebaseDatabase.getInstance().getReference("menus").child(rid).push().getKey();
                    object.setFid(key);
                    if (key != null) {
                        saveNewFood(object,key);

                    } else {
                        Toast.makeText(EditMenuActivity.this, "Some Error Has Occurred. Retry", Toast.LENGTH_SHORT).show();
                    }

                }
            }).setNegativeButton("Cancel", null);

        }else if(mode==1){      //for editing existing menu item
            builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    menuObject current=list.get(position);
                    String nm = name.getText().toString();
                    String inf = info.getText().toString();
                    int prc = Integer.parseInt(price.getText().toString());
                    int off=Integer.parseInt(offer.getText().toString());
                    current.setName(nm);
                    current.setInfo(inf);
                    current.setPrice(prc);
                    current.setOffer(off);
                    current.setType(type[0]);
                    updateFoodItem(current);

                }
            }).setNeutralButton("delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final menuObject current=list.get(position);
                    FirebaseDatabase.getInstance().getReference("menus").child(rid).child(current.getFid()).removeValue();
                    Toast.makeText(EditMenuActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    adapter.remove(current);
                }
            })
                    .setNegativeButton("cancel",null);
        }
        builder.create().show();
 }

    private void updateFoodItem(final menuObject current) {
        if(bitmapImage!=null)

        {
            FirebaseStorage storage=FirebaseStorage.getInstance();
            final StorageReference storeRef=storage.getReference().child("menu_item_pics").child(rid).child(current.getFid()+".jpg");
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
                                current.setPicUrl(downUri.toString());
                                Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                                FirebaseDatabase.getInstance().getReference("menus").child(rid).child(current.getFid()).setValue(current);
                                Toast.makeText(EditMenuActivity.this,"Updated",Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: "+"failed" );
                    Toast.makeText(EditMenuActivity.this, "Adding staff Failed", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }
        FirebaseDatabase.getInstance().getReference("menus").child(rid).child(current.getFid()).setValue(current);
        Toast.makeText(EditMenuActivity.this,"Updated",Toast.LENGTH_SHORT).show();
    }
    private void saveNewFood(final menuObject object,final String key) {
        if(bitmapImage!=null)
        {
            FirebaseStorage storage=FirebaseStorage.getInstance();
            final StorageReference storeRef=storage.getReference().child("menu_item_pics").child(rid).child(key+".jpg");
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
                                FirebaseDatabase.getInstance().getReference("menus").child(rid).child(key).setValue(object);
                                Toast.makeText(EditMenuActivity.this, "New Food Item is Successfully Added", Toast.LENGTH_SHORT).show();
                                Log.e("Final URL", "onComplete: Url: " + downUri.toString());
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "onFailure: "+"failed" );
                    Toast.makeText(EditMenuActivity.this, "Adding staff Failed", Toast.LENGTH_SHORT).show();
                }
            });
        return;
        }
        FirebaseDatabase.getInstance().getReference("menus").child(rid).child(key).setValue(object);
        Toast.makeText(EditMenuActivity.this, "New Food Item is Successfully Added", Toast.LENGTH_SHORT).show();
    }

    private void SelectImage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(EditMenuActivity.this);
        View view= LayoutInflater.from(EditMenuActivity.this).inflate(R.layout.image_choose_dialog,null);
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

    private void getMenuItems(){
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject object=dataSnapshot.getValue(menuObject.class);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if(resultCode==RESULT_OK){
                    if (data != null) {
                        bitmapImage=(Bitmap)data.getExtras().get("data");
                        picForFood.setImageBitmap(bitmapImage);
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
                            picForFood.setImageBitmap(bitmapImage);
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
}
