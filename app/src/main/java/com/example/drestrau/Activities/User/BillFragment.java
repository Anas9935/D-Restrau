package com.example.drestrau.Activities.User;

import
        android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.example.drestrau.R;
import com.example.drestrau.RoomRelated.MyOrderObject;
import com.example.drestrau.RoomRelated.OrderDatabase;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class BillFragment extends Fragment {


    private static final int WIDTH =300 ;
    private String rid,selKey,payKey,choice,uid;
    private int totAmt;
//    private int totalAmount;
    private TextView bill_foodList;
    private TextView bill_priceList;
    private TextView dateTv;
    private TextView timeTv;
    private TextView bill_disc;
    private TextView bill_rest_name;
    private TextView amountPaid;
    private TextView orderType;
    private ImageView bill_qrcode;
    private long datestamp;
    private int time;
    private int type,offer;
    private ArrayList<menuObject> list;
    public BillFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getting values from bundle
        Bundle obj=getArguments();
        if(obj!=null) {
             rid = obj.getString("rid");
             selKey = obj.getString("selKey");
             payKey = obj.getString("payKey");
             String amt=obj.getString("totAmount");
            if (amt != null) {
                totAmt=Integer.parseInt(amt);
            }
            Log.e(TAG, "onCreateView: Amount"+totAmt );

        }
        View root=LayoutInflater.from(getContext()).inflate(R.layout.bill_layout,container,false);

        bill_foodList=root.findViewById(R.id.bill_food_list);
        bill_priceList=root.findViewById(R.id.bill_price_list);
        TextView bill_tot_amt = root.findViewById(R.id.bill_tot_amount);
        bill_qrcode=root.findViewById(R.id.bill_qrCode);
        dateTv=root.findViewById(R.id.bill_date);
        timeTv=root.findViewById(R.id.bill_timeslot);
        Button finish = root.findViewById(R.id.bill_finishBtn);
        bill_disc=root.findViewById(R.id.bill_Disc_amount);
        bill_rest_name=root.findViewById(R.id.bill_rest_name);
        amountPaid=root.findViewById(R.id.bill_amountPayable);
        orderType=root.findViewById(R.id.bill_type);

        populateFood();
        bill_tot_amt.setText(String.valueOf(totAmt));
        populatePayment();
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().finish();
             //   getActivity().getParent().finish();
            }
        });

        return root;
    }
    private void populatePayment(){
        FirebaseDatabase.getInstance().getReference("payments").child(rid).child(payKey).child("mode").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer md=dataSnapshot.getValue(Integer.class);
                if(md!=null){
                    if(md==1){
                        int partial=(int)(0.4*totAmt);
                        amountPaid.setText(String.valueOf(partial));
                    }else if(md==2){
                        amountPaid.setText(String.valueOf(totAmt));
                    }
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void populateFood(){
        FirebaseDatabase.getInstance().getReference("selections").child(rid).child(selKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                switch(dataSnapshot.getKey()){
                    case "choice":{
                            choice=dataSnapshot.getValue(String.class);
                            if(choice!=null){
                                getRestDetails();

                              //  getMenu();
                            }
                            break;
                    }
                    case "date":{
                            Long dates=dataSnapshot.getValue(Long.class);
                            if(dates!=null){
                                datestamp=dates;
                                dateTv.setText(utilityClass.getDate(datestamp));
                            }
                        break;
                    }
                    case "timeslot":{
                        Integer times=dataSnapshot.getValue(Integer.class);
                        if(times!=null){
                            String[] list = getResources().getStringArray(R.array.timeSlot);
                            time=times;
                            timeTv.setText(list[times]);
                        }
                        break;
                    }
                    case "type":{
                        Integer tp=dataSnapshot.getValue(Integer.class);
                        if(tp!=null){
                            type=tp;
                            if(tp==0){
                                orderType.setText("Take away");
                            }else if(tp==1){
                                orderType.setText("Reservation");
                            }
                        }
                        break;

                    }
                    case "uid":{
                        uid=dataSnapshot.getValue(String.class);
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
        FirebaseDatabase.getInstance().getReference("selections").child(rid).child(selKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                MyOrderObject obj=new MyOrderObject(rid,choice,totAmt,time,datestamp,selKey,payKey);
                saveDataInRoom(obj);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveDataInRoom(final MyOrderObject obj) {
        final OrderDatabase db=OrderDatabase.getInstance(getContext());
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                db.objectDao().insertObject(obj);
                Log.e(TAG, "run: data is inserted in room" );
            }
        });
        thread.start();
    }

    private void getMenu(){
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
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
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                populatefooditems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void populatefooditems(){
        int amt=0,disc=0;
        ArrayList<quantatySelected> itemsSel=new ArrayList<>();

        String[] items=choice.split(" ");
        for(int i=0;i<items.length;i++)
        {
            CharSequence quan=items[i].subSequence(items[i].indexOf("(")+1,items[i].indexOf(")"));
            //quantity.add(Integer.parseInt((String) quan));

            String food=items[i].substring(0,items[i].indexOf("("));
            //  foodlist.add(Integer.parseInt(food));
            quantatySelected object=new quantatySelected(food,Integer.parseInt((String)quan));
            itemsSel.add(object);
        }
        for( int i=0;i<itemsSel.size();i++){
            quantatySelected current=itemsSel.get(i);

            //finding the menu item in the list
            for(menuObject object:list){
                if(object.getFid().equals(current.getFoodId())){
                    bill_foodList.append(object.getName()+"\n");
                    bill_priceList.append(object.getPrice()+"\n");
                    amt+=object.getPrice();
                    disc+=(int)(object.getPrice()*current.getQuantity()*object.getOffer()/100);
                    break;
                }
            }

        }
        if(offer!=0){
            disc+=amt*offer/100;
        }
        bill_disc.setText("-");
        bill_disc.append(String.valueOf(disc));
      //  bill_tot_amt.setText(String.valueOf(amt-disc));
        //bill_Disc
        String jsonString="{\"uid\":\""+uid+"\",\"rid\":\""+rid+"\",\"selKey\":\""+selKey+"\"," +
                "\"payKey\":\""+payKey+"\",\"date\":\""+datestamp+"\",\"time\":\""+time+"\"}";
        utilityClass.generateQr(bill_qrcode,jsonString);
    //    generateQr();

    }
    private void getRestDetails(){
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("offer").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer off=dataSnapshot.getValue(Integer.class);
                if(off!=null&&off!=0){
                    offer=off;
                }else{
                    offer=0;
                }
                getMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String n=dataSnapshot.getValue(String.class);
                bill_rest_name.setText(n);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
  /*
    private void generateQr(){
        String uid= FirebaseAuth.getInstance().getUid();
        String JsonStaff="{\"uid\":\""+uid+"\",\"rid\":\""+rid+"\",\"selKey\":\""+selKey+"\"," +
                "\"payKey\":\""+payKey+"\",\"date\":\""+datestamp+"\",\"time\":\""+time+"\"}";
        try{
            Bitmap bitmap=encodeAsBitmap(JsonStaff);
            bill_qrcode.setImageBitmap(bitmap);
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
*/
}
