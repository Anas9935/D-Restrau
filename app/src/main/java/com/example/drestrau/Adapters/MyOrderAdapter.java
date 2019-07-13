package com.example.drestrau.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drestrau.Activities.User.GeneralMyOrdersFragment;
import com.example.drestrau.Activities.User.GeneralUserNew;
import com.example.drestrau.Activities.User.Reservations;
import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.BillFragment;
import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.example.drestrau.R;
import com.example.drestrau.RoomRelated.MyOrderObject;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.CustomViewHolder> {
    private static final int WIDTH = 300;
    private ArrayList<MyOrderObject> list;
private final Context context;
private final ArrayList<menuObject> menuObjects;
public MyOrderAdapter(Context context, ArrayList<MyOrderObject> ls){
    list=ls;
    this.context=context;
    menuObjects=new ArrayList<>();
}

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        final TextView name;
        final TextView address;
        final TextView price;
        final TextView choice;
        final TextView date;
        final TextView time;
        final Button reOrder;
        final ImageView qrCode;
        CustomViewHolder(View itemView){
            super(itemView);
            name=itemView.findViewById(R.id.my_order_todo_name);
            address=itemView.findViewById(R.id.my_order_todo_address);
            price=itemView.findViewById(R.id.my_order_todo_price);
            choice=itemView.findViewById(R.id.my_order_todo_choice);
            date=itemView.findViewById(R.id.my_order_todo_date);
            time=itemView.findViewById(R.id.my_order_todo_time);
            reOrder=itemView.findViewById(R.id.my_order_todo_reorder);
            qrCode=itemView.findViewById(R.id.order_todo_qrCode);

        }
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_rv_todo,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final MyOrderObject current=list.get(position);

        setNameAndAddress(holder.name,holder.address,current.getRid());
        holder.price.setText(String.valueOf(current.getAmount()));
        getMenu(holder.choice,current.getChoice(),current.getRid());
        holder.date.setText(utilityClass.getDate(current.getDateStamp()));
        String[] timesChoice=context.getResources().getStringArray(R.array.timeSlot);
        holder.time.setText(timesChoice[current.getTimeIndex()]);
        holder.reOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //going to reservation
                Intent intent=new Intent(context, Reservations.class);
                intent.putExtra("rid",current.getRid());
                intent.putExtra("choice",current.getChoice());
                context.startActivity(intent);
            }
        });

        holder.qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///opening the order
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("My Order");
                View view=LayoutInflater.from(context).inflate(R.layout.alert_fragment_for_myorders,null);
                builder.setView(view);
                ImageView img=view.findViewById(R.id.my_order_qr_img);
                TextView txt=view.findViewById(R.id.my_order_qr_text);
                generateQr(current.getRid(),img,current.getSid(),current.getPid(),current.getDateStamp(),current.getTimeIndex());
                long dateToday=System.currentTimeMillis();

                String dateSel=utilityClass.getDate(current.getDateStamp());
                String[] indices=context.getResources().getStringArray(R.array.timeSlot);
                String[] subString=indices[current.getTimeIndex()].split(" ");

                dateSel+=" "+subString[2];
                Log.e("tag", "onClick: selDate"+dateSel );
                DateFormat formatter=new SimpleDateFormat("dd/mm/yyyy HH:mm");
                try {
                    Date date=formatter.parse(dateSel);
                    long selTimeStamp=date.getTime();
                    if(selTimeStamp<dateToday){
                        txt.setVisibility(View.VISIBLE);
                    }else{
                        txt.setVisibility(View.INVISIBLE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                builder.setPositiveButton("Finish",null);
                builder.create().show();
            }
        });

    }
    private void generateQr(String rid,ImageView img,String selKey,String payKey,long datestamp,int time){
        String uid= FirebaseAuth.getInstance().getUid();
        String JsonStaff="{\"uid\":\""+uid+"\",\"rid\":\""+rid+"\",\"selKey\":\""+selKey+"\",\"payKey\":\""+payKey+"\",\"date\":\""+datestamp+"\",\"time\":\""+time+"\"}";
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
    private void setNameAndAddress(final TextView name, final TextView address, String rid) {
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                RestObject obj=dataSnapshot.getValue(RestObject.class);
                if(obj!=null){
                    name.setText(obj.getName());
                    address.setText(obj.getAdd1());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getMenu(final TextView choice, final String choice1, String rid) {
    FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
         menuObject obj=dataSnapshot.getValue(menuObject.class);
         if(obj!=null){
             menuObjects.add(obj);
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
    FirebaseDatabase.getInstance().getReference("menus").child(rid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<quantatySelected> itemsSel=new ArrayList<>();

            String[] items=choice1.split(" ");
            for(int i=0;i<items.length;i++)
            {
                CharSequence quan=items[i].subSequence(items[i].indexOf("(")+1,items[i].indexOf(")"));
                //quantity.add(Integer.parseInt((String) quan));

                String food=items[i].substring(0,items[i].indexOf("("));
                //  foodlist.add(Integer.parseInt(food));
                quantatySelected object=new quantatySelected(food,Integer.parseInt((String)quan));
                itemsSel.add(object);
            }
            StringBuilder builder=new StringBuilder();
            for( int i=0;i<itemsSel.size();i++){
                quantatySelected current=itemsSel.get(i);

                //finding the menu item in the list
                for(menuObject object:menuObjects){
                    if(object.getFid().equals(current.getFoodId())){
                        builder.append(object.getName()).append(" ×").append(current.getQuantity()).append(", ");
                        break;
                    }
                }
                choice.setText(builder.toString());

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setList(ArrayList<MyOrderObject> ls){
    list=ls;

    }
}
