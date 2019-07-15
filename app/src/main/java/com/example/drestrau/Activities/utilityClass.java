package com.example.drestrau.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.example.drestrau.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class utilityClass {

    public static String getTime(long timestamp){
        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
        return formatter.format(new Date(timestamp*1000));
    }
    public static String getDate(long timestamp){
        SimpleDateFormat formatter=new SimpleDateFormat("dd/mm/yyyy");
        return formatter.format(timestamp*1000);//(new Date(timestamp*1000));
    }
    public static void populateTableObject(String rid, final String choice, final TextView foods, final TextView qunatities){
        final ArrayList<menuObject> list;
        list=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("menus").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                menuObject obj=dataSnapshot.getValue(menuObject.class);
                if(obj!=null){
                    list.add(obj);
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
               // listInterface.getMenuList(list);
                //Here ihave the list
                populateViews(choice,list,foods,qunatities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private static void populateViews(String choice, ArrayList<menuObject> list, TextView foodItems,TextView quantity){
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
                    foodItems.append(object.getName()+"\n");
                    quantity.append(current.getQuantity()+"\n");
                    break;
                }
            }

        }
    }


    public static  void generateQr( ImageView img,String jsonString){
       // String JsonStaff="{\"rid\":\""+rid+"\",\"selForChefKey\":\""+selChefKey+"\"}";
        try{
            Bitmap bitmap=encodeAsBitmap(jsonString);
            img.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    private static Bitmap encodeAsBitmap(String json) throws WriterException {
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
}
