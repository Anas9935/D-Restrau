package com.example.drestrau.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.drestrau.Adapters.RecepAdapter;
import com.example.drestrau.Objects.RecepObject;
import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.Objects.attendanceObject;
import com.example.drestrau.Objects.paymentObject;
import com.example.drestrau.Objects.pres_rest;
import com.example.drestrau.Objects.selectionForChefObject;
import com.example.drestrau.Objects.staffObject;
import com.example.drestrau.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ReceptionistActivity extends AppCompatActivity {
private String rid;
    private String uid;
    private String staffId;
private ListView lv;
private FloatingActionButton fab;
    private FloatingActionButton scanAtt;
    private FloatingActionButton newRes;
//TextView seatNumber;
private int seats;
private int tableSel;
private RecepAdapter adapter;
private ArrayList<RecepObject> list;

private IntentIntegrator qrcode;

private TextView ProfileName;
    private TextView ProfileViewBtn;
    private RelativeLayout item1;
    private RelativeLayout item2;
    private ImageView profileImg;
    private final String TAG="Recep";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist);

        rid=getIntent().getStringExtra("rid");
        uid=FirebaseAuth.getInstance().getUid();
        initialiseViews();

        list=new ArrayList<>();
        adapter=new RecepAdapter(this,list);
        lv.setAdapter(adapter);
        getList();


        qrcode=new IntentIntegrator(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable the background
            }
        });
        scanAtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrcode.initiateScan();
            }
        });
        newRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        populateDrawer();
    }
    private void getRestNameAndSeats(final TextView tv) {
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RestObject obj=dataSnapshot.getValue(RestObject.class);
                if(obj!=null){
                    tv.setText(obj.getName());
                    //seats
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initialiseViews(){
        lv=findViewById(R.id.recep_lv);
        fab=findViewById(R.id.recep_fab);
        scanAtt=findViewById(R.id.recep_scan_atten);
        newRes=findViewById(R.id.recep_newRes);

        //for the drawer
        ProfileName=findViewById(R.id.userName);
        ProfileViewBtn=findViewById(R.id.desc);
        item1=findViewById(R.id.simple_staffRVItem1);
        item2=findViewById(R.id.simple_staffRVItem2);
    }
    private void getList(){
        FirebaseDatabase.getInstance().getReference("payments").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                paymentObject obj=dataSnapshot.getValue(paymentObject.class);
                RecepObject recepObject= null;
                if (obj != null) {
                    recepObject = new RecepObject(obj.getPid(),obj.getUid(),rid,obj.getTotal(),obj.getStatus(),obj.getMode(),1);
                    adapter.add(recepObject);
                  //  Log.e("ths", "onChildAdded: "+list.size() );
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);

        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this, "No Proper Data Found Found", Toast.LENGTH_SHORT).show();
            }else{
                try{
                    JSONObject obj=new JSONObject(result.getContents());
                    if(obj.has("selKey")){
                        String uidObj=obj.getString("uid");
                        String ridObj=obj.getString("rid");
                        String selKeyObj=obj.getString("selKey");
                        String payKey=obj.getString("payKey");
                        long dateObj=obj.getInt("date");
                        int timeObj=obj.getInt("time");
                        if (ridObj.equals(rid)){
                            //preparing views

                            Toast.makeText(this,"Restaurant Id Matched",Toast.LENGTH_SHORT).show();
                            popupDialog(uidObj,ridObj,selKeyObj,payKey,dateObj,timeObj);
                        }
                    }else if(obj.has("uid")){
                        String staffUserid=obj.getString("uid");
                        String staffId=obj.getString("sid");
                        Log.e(TAG, "onActivityResult: "+"you are here" );
                        updateAttendance(staffId);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void updateAttendance(final String sid) {
        FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                attendanceObject obj=dataSnapshot.getValue(attendanceObject.class);
                int dc;
                Log.e(TAG, "onDataChange: "+dataSnapshot.getKey() );
                if (obj != null) {
                    dc = obj.getDayCount();
                    dc+=1;
                    obj.setDayCount(dc);
                    if(dc>=obj.getMaxDayOfMonth()){
                        obj.setPayDue(1);
                    }
                    obj.setAttendenceToday(1);
                    FirebaseDatabase.getInstance().getReference("attendance").child(rid).child(sid).setValue(obj);
                    Toast.makeText(ReceptionistActivity.this, "Attendence done", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void popupDialog(final String ud, final String rd, final String selKey, String payKey, long date, int time){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view=null;
        view= LayoutInflater.from(ReceptionistActivity.this).inflate(R.layout.scan_res_dialog,null);
        builder.setTitle("scanned result");
        builder.setView(view);
        //-------------------------------------------------------------------------------------------
        TextView name,dt,tm,restName;
        Spinner tabSel;
        name=view.findViewById(R.id.booking_name);
        dt=view.findViewById(R.id.booking_date);
        tm=view.findViewById(R.id.booking_time);
        restName=view.findViewById(R.id.booking_name_rest);
        tabSel=view.findViewById(R.id.booking_tables_spin);
        setupTableSpinner(tabSel);
        setName(name,ud);
        //matching the rd and rid
        //
        getRestNameAndSeats(restName);

        dt.setText(utilityClass.getDate(date));
        String[] times=getResources().getStringArray(R.array.timeSlot);
        tm.setText(times[time]);
        //update the list
        for(RecepObject curr:list){
            if(curr.getPid().equals(payKey)){
                curr.setCustStat(0);
                adapter.notifyDataSetChanged();
                break;
            }
        }
        //final int tableSel=getTableSel(tabSel);
        //-------------------------------------------------------------------------------------------
        builder.setPositiveButton("Notify Cook", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                notifyCook(tableSel,selKey);
                //TODO update the user pres_rest with ud and rd
                pres_rest rest=new pres_rest(rd,selKey);
                FirebaseDatabase.getInstance().getReference("users").child(ud).child("pres_data").setValue(rest);

            }
        }).setNegativeButton("Cancel",null);
        builder.create().show();
    }
    private void setupTableSpinner(Spinner tabSel) {
        Integer[] tables=new Integer[seats];
        for(int i=0;i<seats;i++){
            tables[i]=i+1;
        }
        ArrayAdapter<Integer> adapter= new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,tables);
        tabSel.setAdapter(adapter);
        tabSel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tableSel=position+1;
                Toast.makeText(ReceptionistActivity.this, "table selected is"+position+1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            tableSel=0;
            }
        });
    }


    private void notifyCook(final int tabl, final String sid) {
        FirebaseDatabase.getInstance().getReference("selections").child(rid).child(sid).child("choice").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ch=dataSnapshot.getValue(String.class);
                if(ch!=null)
                {selectionForChefObject obj=new selectionForChefObject(tabl,ch,sid);
                    String selChefKey=FirebaseDatabase.getInstance().getReference("selectionForChef").child(rid).push().getKey();
                    if (selChefKey != null) {
                        FirebaseDatabase.getInstance().getReference("selectionForChef").child(rid).child(selChefKey).setValue(obj);
                        Toast.makeText(ReceptionistActivity.this, "Cook Is Notified", Toast.LENGTH_SHORT).show();
                        showQrCode(selChefKey);
                    }
                }
                else{
                    Toast.makeText(ReceptionistActivity.this,"No Such Order Exists",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showQrCode(String selChefKey) {
        AlertDialog.Builder builder=new AlertDialog.Builder(ReceptionistActivity.this);
        View view=LayoutInflater.from(ReceptionistActivity.this).inflate(R.layout.qr_code_img,null);
        builder.setView(view);
        ImageView imgView=view.findViewById(R.id.qr_code_imgView);
        generateQr(selChefKey,imgView);
        builder.setPositiveButton("Finish", null);
        builder.create().show();
    }
        private void generateQr(String selChefKey, ImageView img){
        String JsonStaff="{\"rid\":\""+rid+"\",\"selForChefKey\":\""+selChefKey+"\"}";
        try{
            Bitmap bitmap=encodeAsBitmap(JsonStaff);
            img.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
        private Bitmap encodeAsBitmap(String json) throws WriterException {
        BitMatrix result;
        try{
            int WIDTH = 300;
            result=new MultiFormatWriter().encode(json
                    , BarcodeFormat.QR_CODE, WIDTH, WIDTH,null);
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

    private void setName(final TextView name, String uid){
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n=dataSnapshot.getValue(String.class);
                if(n!=null){
                    name.setText(n);
                }else{
                    name.setText("Customer");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recep,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_recep_log_out:{
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            }
            case R.id.action_recep_manual_att:{
                Intent intent=new Intent(ReceptionistActivity.this,ManualAttendenceActivity.class);
                intent.putExtra("rid",rid);
                startActivity(intent);
                return true;
            }
            default:{
                return  super.onOptionsItemSelected(item);
            }
        }
    }

    private void populateDrawer(){

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nm=dataSnapshot.getValue(String.class);
                if(nm!=null){
                    ProfileName.setText(nm);
                }else{
                    ProfileName.setText("NAME");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("staffs").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                staffObject obj=dataSnapshot.getValue(staffObject.class);
                if(obj!=null&&obj.getUid().equals(uid)&&obj.getPicUrl()!=null){
                    Glide.with(ReceptionistActivity.this)
                            .load(obj.getPicUrl())
                            .into(profileImg);
                    //here we take image url and staff id
                    staffId=dataSnapshot.getKey();
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

        ProfileViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the profile of the worker
                Intent intent=new Intent(ReceptionistActivity.this, ProfileActivity.class);
                intent.putExtra("isStaff",1);
                intent.putExtra("rid",rid);
                startActivity(intent);

            }
        });
        item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(staffId!=null) {
                    Intent intent = new Intent(ReceptionistActivity.this, QrCodeActivity.class);
                    intent.putExtra("rid", rid);
                    intent.putExtra("staffId",staffId );
                    startActivity(intent);
                }
            }
        });
        item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

}
