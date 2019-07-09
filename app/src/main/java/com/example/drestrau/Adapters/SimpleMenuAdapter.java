package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.R;

import java.util.ArrayList;

public class SimpleMenuAdapter extends ArrayAdapter<menuObject> {

    ArrayList<menuObject> list;

    public SimpleMenuAdapter(Context context, ArrayList<menuObject> lv) {
        super(context, 0,lv);
        list=lv;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.menu_todo,parent,false);
        }
        menuObject current=list.get(position);
        TextView menuadd=view.findViewById(R.id.menuadd);
        menuadd.setVisibility(View.INVISIBLE);

        ImageView imv,veg;
        final TextView name,info,price,offer;
        RatingBar bar;
        imv=view.findViewById(R.id.menuimg);
        veg=view.findViewById(R.id.menuveg);
        name=view.findViewById(R.id.menuname);
        info=view.findViewById(R.id.menuInfo);
        price=view.findViewById(R.id.menuprice);
        offer=view.findViewById(R.id.menuoffer);
        bar=view.findViewById(R.id.menu_rating);

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
        offer.setText(current.getOffer()+"% Off");
        bar.setRating(current.getRating());

        return view;
    }
}
