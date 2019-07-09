package com.example.drestrau.Activities;

import android.graphics.Bitmap;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class QrCodeActivity extends AppCompatActivity {
    private static final int WIDTH = 275;
    String rid,staffId;
TextView name,sid;
ImageView qrcode,qrStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        rid=getIntent().getStringExtra("rid");
        staffId=getIntent().getStringExtra("staffId");
        name=findViewById(R.id.qr_name);
        sid=findViewById(R.id.qr_sid);
        qrcode=findViewById(R.id.qr_imgCode);
        qrStatus=findViewById(R.id.qr_status);
        Log.e("rid", "onCreate: "+rid );
           getSid();
        getAttendenceDetail();
    }
    private void getAttendenceDetail(){
        //TODO get the details of the staff from attendence table
        FirebaseDatabase.getInstance().getReference("attendance").child(staffId).child("attendanceToday").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer att=dataSnapshot.getValue(Integer.class);
                if(att!=null){
                    if (att == 0) {
                        qrStatus.setImageResource(R.drawable.cross_pastel);
                    } else {
                        qrStatus.setImageResource(R.drawable.check_pastel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void getSid(){
        final String uid=FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject object=dataSnapshot.getValue(staffObject.class);
                if(object!=null&&object.getUid().equals(uid)){
                    name.setText(object.getName1());
                    name.append(" "+object.getName2());
                    name.append(" "+object.getName3());
                    sid.setText(object.getSid());
                    generateQr(object.getSid());
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
    private void generateQr(String id){
        String uid= FirebaseAuth.getInstance().getUid();
        String JsonStaff="{\"uid\":\""+uid+"\",\"sid\":\""+id+"\"}";
        try{
            Bitmap bitmap=encodeAsBitmap(JsonStaff);
            qrcode.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    private Bitmap encodeAsBitmap(String json) throws WriterException {
        BitMatrix result;
        try{
            result=new MultiFormatWriter().encode(json
                    , BarcodeFormat.QR_CODE,WIDTH,WIDTH,null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        } int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
