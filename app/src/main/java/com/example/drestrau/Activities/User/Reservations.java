package com.example.drestrau.Activities.User;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drestrau.Adapters.billItemAdapter;
import com.example.drestrau.Objects.bill_item_object;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.example.drestrau.Objects.selection;
import com.example.drestrau.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Reservations extends AppCompatActivity {
    private static final String TAG = "Reservations";
    private String choice,rid;
private TextView resName,totAmt,dateSel,status,discount;
//TextView food,qty,amt;

private ImageView calender;
private Spinner timepick;
private EditText nop;
private Button pay;
private Switch res;

private int timeindex;
   // private int nos;
    private int noRems;
private String selKey;
    int seats=0;

private long datestamp;

private RelativeLayout spclDisc;
private TextView spclDiscamt;

private int baseamt=0;
    private int disc=0;
private DatabaseReference menuRef;
    private DatabaseReference resRef;
private ArrayList<menuObject> list;

private int fullOffer;
private boolean dataRec=false;
//private boolean isAvailable=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);
        Log.e(TAG, "onCreate: " );
        initializeViews();
        choice=getIntent().getStringExtra("choice");
        rid=getIntent().getStringExtra("rid");

        list=new ArrayList<>();
        menuRef= FirebaseDatabase.getInstance().getReference("menus").child(String.valueOf(rid));
        resRef=FirebaseDatabase.getInstance().getReference("restaurants");

        //-----------------------------------------
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        String todayDate=day+"-"+month+"-"+year;
        DateFormat format=new SimpleDateFormat("dd-mm-yyyy");
        try {
            Date date=format.parse(todayDate);
            dateSel.setText(todayDate);
            datestamp=date.getTime()/1000;
           // Log.e(TAG, "onCreate: "+datestamp );
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //-----------------------------------------
        getResDetails();

        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate();
            }
        });
        setupSpinner();

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataRec){
                   // gotoPayment();
                    checkAvailable();
                }
            }
        });

    }


    private void initializeViews(){
        discount=findViewById(R.id.res_discAmt);
        resName=findViewById(R.id.res_text1);
      //  food=findViewById(R.id.res_sel_food);
       // qty=findViewById(R.id.res_sel_qty);
       // amt=findViewById(R.id.res_sel_amt);
        totAmt=findViewById(R.id.res_finalAmt);
        dateSel=findViewById(R.id.res_datesel);
        status=findViewById(R.id.res_time_available);
        calender=findViewById(R.id.res_datePicker);
        timepick=findViewById(R.id.res_time_spinner);
        nop=findViewById(R.id.res_nop);
        pay=findViewById(R.id.res_pay_btn);
        spclDisc=findViewById(R.id.res_disc_special);
        spclDiscamt=findViewById(R.id.res_discAmt_sptl);
        res=findViewById(R.id.res_reserve_switch);

    }

    private void getResDetails(){
        Log.e(TAG, "getResDetails: " );
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                boolean[] allChecked={false,false,false};

                switch (dataSnapshot.getKey()){
                    case "name":{
                        resName.setText(dataSnapshot.getValue(String.class));
                        break;
                    }
                    case "offer":{
                        Integer off=dataSnapshot.getValue(Integer.class);
                        if(off!=null){
                            spclDisc.setVisibility(View.VISIBLE);
                        }else{
                            spclDisc.setVisibility(View.INVISIBLE);
                        }
                        break;
                    }
                    case "seats2":{
                        Integer seat=dataSnapshot.getValue(Integer.class);
                        if(seat!=null){
                            seats+=seat;
                            allChecked[0]=true;
                            int flag=0;
                            for(boolean check:allChecked){
                                if(!check){
                                    flag=1;
                                }
                            }
                            if(flag==0)
                            {
                                //return seats
                            }
                        }
                        break;
                    }
                    case "seats4":{
                        Integer seat=dataSnapshot.getValue(Integer.class);
                        if(seat!=null){
                            seats+=seat;
                            allChecked[1]=true;
                            int flag=0;
                            for(boolean check:allChecked){
                                if(!check){
                                    flag=1;
                                }
                            }
                            if(flag==0)
                            {
                               //return seats
                            }
                        }
                        break;
                    }
                    case "seats6":{
                        Integer seat=dataSnapshot.getValue(Integer.class);
                        if(seat!=null){
                            seats+=seat;
                            allChecked[2]=true;
                            int flag=0;
                            for(boolean check:allChecked){
                                if(!check){
                                    flag=1;
                                }
                            }
                            if(flag==0)
                            {
                               //return seats
                            }
                        }
                        break;
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
        /*
        resRef.child(rid).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    switch (dataSnapshot.getKey()){
                        case "name":{
                                resName.setText(dataSnapshot.getValue(String.class));
                            break;
                        }
                        case "offer":{
                                Integer off=dataSnapshot.getValue(Integer.class);
                                if(off!=null){
                                    spclDisc.setVisibility(View.VISIBLE);
                                }else{
                                    spclDisc.setVisibility(View.INVISIBLE);
                                }
                            break;
                        }
                        case "seats":{

                            break;
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
        */
        /*
        resRef.child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        getmenu();
    }
          private void getmenu(){
              Log.e(TAG, "getmenu: " );
        menuRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                list.add(dataSnapshot.getValue(menuObject.class));
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

        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fillViews();
                dataRec=true;
     //           Log.e(TAG, "onDataChange: "+"in single listener" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
                private void fillViews(){
                    Log.e(TAG, "fillViews: " );
        //-----------------------------------------------------
                    //the list for food items
                    ListView bill_lv=findViewById(R.id.res_food_item);
                    ArrayList<bill_item_object> billList;
                    billList=new ArrayList<>();
                    billItemAdapter billAdapter=new billItemAdapter(this,billList,1);
                    bill_lv.setAdapter(billAdapter);
        //------------------------------------------------------
        //picking items from list through string choice and calculating the total values
    ArrayList<quantatySelected> itemsSel=new ArrayList<>();

        String[] items=choice.split(" ");
        for(int i=0;i<items.length;i++)
        {
            CharSequence quan=items[i].subSequence(items[i].indexOf("(")+1,items[i].indexOf(")"));
            //quantity.add(Integer.parseInt((String) quan));

            String food=items[i].substring(0,items[i].indexOf("("));
            quantatySelected object=new quantatySelected(food,Integer.parseInt((String)quan));
            itemsSel.add(object);
        }
        //now we have the separated lists
    //    food.setText("");
     //   amt.setText("");
    //    qty.setText("");
        for( int i=0;i<itemsSel.size();i++){
           quantatySelected current=itemsSel.get(i);

           //finding the menu item in the list
            for(menuObject object:list){
                if(object.getFid().equals(current.getFoodId())){
                    //for the new type of adapter
                    billAdapter.add(new bill_item_object(object.getName(),current.getQuantity(),(int)object.getPrice()));
                    //------------------------------------
//
//                    food.append(object.getName()+"\n");
//                    amt.append(object.getPrice()*current.getQuantity()+"\n");
//                    qty.append(current.getQuantity()+"\n");
//
                    baseamt+=object.getPrice()*current.getQuantity();
                    int dis=(int)object.getPrice()*current.getQuantity()*object.getOffer()/100;
                    disc+=dis;
                    Log.e(TAG, "fillViews: "+dis );
                    break;
                }
            }

        }
        int finalamount=baseamt-disc;
        float discOnTotal=0;

        if(spclDisc.getVisibility()==View.VISIBLE){
            Log.e(TAG, "fillViews: Visible view" );
            discOnTotal=baseamt*((float)fullOffer/100);
            spclDiscamt.setText(String.valueOf(discOnTotal));
        }
        discount.setText(String.valueOf(disc));
        finalamount-=discOnTotal;
        totAmt.setText(String.valueOf(finalamount));
    }

    private void setupSpinner(){
        Log.e(TAG, "setupSpinner: ");
        final ArrayAdapter<CharSequence> des=ArrayAdapter.createFromResource(this,R.array.timeSlot,android.R.layout.simple_spinner_item);
        des.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timepick.setAdapter(des);
        timepick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeindex=position-1;
              //  checkAvailable();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //set default
                timeindex=-1;
            }
        });
    }



    private void getdate(){
        Log.e(TAG, "getdate: " );
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(Reservations.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateSel.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                        try {
                            DateFormat date= new SimpleDateFormat("dd-MM-yyyy");
                            Date date1=date.parse(dateSel.getText().toString());
                            //date is wrong
                            Log.e(TAG, "onDateSet: "+date1.toString()+"  "+date1.getTime() );
                            datestamp=date1.getTime()/1000;

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void checkAvailable(){
        Log.e(TAG, "checkAvailable: " );
        //get the time occupied on that day and time
        if(res.isChecked()){
            //takeaway Always available unless Shop's not Open
            status.setText("Available");
            gotoPayment(0);
            return;
        }
        final int[] count=new int[1];
        FirebaseDatabase.getInstance().getReference("selections").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                selection obj=dataSnapshot.getValue(selection.class);
                //datestamp
                if(obj!=null&&obj.getDate()==datestamp&&obj.getTimeslot()==timeindex){
                    count[0]++;
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

        FirebaseDatabase.getInstance().getReference("selections").child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                noRems=seats-count[0];
                Log.e(TAG, "onDataChange: "+noRems+"="+seats+"-"+count[0] );
                if(noRems>0){
                    status.setText("Available");
                    gotoPayment(1);
                }else{
                    status.setText("Seats Full. Try Another Time slot");
                   // isAvailable=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void gotoPayment(int reserve){
        Log.e(TAG, "gotoPayment: " );
        //two objwcts one is selection and one is payment
        //object is send to selection

        String numberop=nop.getText().toString();

      //  if(isAvailable){
        selection selection=new selection(FirebaseAuth.getInstance().getUid(),choice,timeindex,reserve,datestamp,Integer.parseInt(numberop));
        selKey=FirebaseDatabase.getInstance().getReference("selections").child(rid).push().getKey();
        if(selKey!=null) {
            FirebaseDatabase.getInstance().getReference("selections").child(rid).child(selKey).setValue(selection);

            Intent intent = new Intent(Reservations.this, PaymentForFoodActivity.class);
            intent.putExtra("rid", rid);
            Log.e(TAG, "gotoPayment: B-D"+baseamt+" "+disc );
            intent.putExtra("totalAmt", baseamt - disc);
            intent.putExtra("selKey", selKey);
            startActivity(intent);
            finish();
        }

       // }
    }
}