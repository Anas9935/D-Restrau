package com.example.drestrau.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.Objects.quantatySelected;
import com.example.drestrau.R;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class menuAdapter extends ArrayAdapter<menuObject> {
    ArrayList<menuObject> list;


    ArrayList<quantatySelected> quantity;
    public menuAdapter( Context context, ArrayList<menuObject> l) {
        super(context, 0,l);
        list=l;

        quantity=new ArrayList<>();

    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=convertView;

        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.menu_todo,parent,false);
        }
        ImageView imv,veg;
        final TextView name,info,price,ad,min,quan,offer;
        //final Button add;
        final TextView add;
        RatingBar bar;
        final LinearLayout menuadd;
        imv=view.findViewById(R.id.menuimg);
        veg=view.findViewById(R.id.menuveg);
        name=view.findViewById(R.id.menuname);
        info=view.findViewById(R.id.menuInfo);
        price=view.findViewById(R.id.menuprice);
        ad=view.findViewById(R.id.menuaddplus);
        min=view.findViewById(R.id.menuaddminus);
        quan=view.findViewById(R.id.menuqty);
        add=view.findViewById(R.id.menuadd);
        offer=view.findViewById(R.id.menuoffer);
        menuadd=view.findViewById(R.id.menuaddlayout);
        bar=view.findViewById(R.id.menu_rating);

        final menuObject current=list.get(position);


        if(current.getPicUrl()!=null){
            Glide.with(getContext()).load(current.getPicUrl())
                    .into(imv);
        }
        int type=current.getType();
        if(type==0){
            veg.setImageResource(R.drawable.veg);
        }else{
            veg.setImageResource(R.drawable.nonveg);
        }
        name.setText(current.getName());
        info.setText(current.getInfo());
        price.setText(String.valueOf(current.getPrice()));
        if(current.getOffer()!=0)
        {
            offer.setText(current.getOffer()+"% Off");
        }else{
            offer.setText("");
        }
        bar.setRating(current.getRating());

        add.setVisibility(View.VISIBLE);
        menuadd.setVisibility(View.INVISIBLE);

        for(quantatySelected obj:quantity){
            if(obj.getFoodId().equals(current.getFid())){
                add.setVisibility(View.INVISIBLE);
                menuadd.setVisibility(View.VISIBLE);
                quan.setText(String.valueOf(obj.getQuantity()));
                break;
            }
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            menuadd.setVisibility(View.VISIBLE);
            add.setVisibility(View.INVISIBLE);
            quan.setText("1");
            quantatySelected obj=new quantatySelected(current.getFid(),1);
            quantity.add(obj);
            }
        });
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=findPosInQuan(current.getFid());
                if(pos!=-1)
                {
                    quantatySelected currItem=quantity.get(pos);
                    int currQuan=currItem.getQuantity();
                    if(currQuan==10){
                        Toast.makeText(getContext(), "Max Value Reached", Toast.LENGTH_SHORT).show();
                    }else{
                    currQuan++;
                    quan.setText(String.valueOf(currQuan));
                    quantity.get(pos).setQuantity(currQuan);
                    }
                }

            }
        });
       min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=findPosInQuan(current.getFid());
                if(pos!=-1){
                    quantatySelected currItem=quantity.get(pos);
                    if(currItem.getQuantity()==1){
                        menuadd.setVisibility(View.INVISIBLE);
                        add.setVisibility(View.VISIBLE);
                        quantity.remove(currItem);
                    }else{
                        int quanInt=currItem.getQuantity();
                        quanInt--;
                        quan.setText(String.valueOf(quanInt));
                        quantity.get(pos).setQuantity(quanInt);
                    }
                }

            }

        });

        return view;
    }

    private int findPosInQuan(String fid) {
        for(int i=0;i<quantity.size();i++){
            if(quantity.get(i).getFoodId().equals(fid)){
                return i;
            }
        }
        return -1;
        }

    public String generateString(){
        String ch;
        StringBuilder s=new StringBuilder(500);
        for(int i=0;i<quantity.size();i++){

                s.append(quantity.get(i).getFoodId());
                s.append("("+quantity.get(i).getQuantity()+") ");


        }
        Log.e(TAG, "generateString: "+s.toString() );
        ch=s.toString();
        return ch;
    }

}
