package com.example.drestrau.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.drestrau.Objects.menuObjForFood;
import com.example.drestrau.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class GeneralFoodRecyclerView extends RecyclerView.Adapter<GeneralFoodRecyclerView.ViewHolder> {
    private final ArrayList<menuObjForFood> list;
    private final LayoutInflater inflater;
    private final Context context;
    private final AdapterView.OnItemClickListener onItemClickListener;

    public GeneralFoodRecyclerView(Context context, ArrayList<menuObjForFood> ls, AdapterView.OnItemClickListener onItemClickListener){
        list=ls;
        inflater=LayoutInflater.from(context);
        this.context=context;
        this.onItemClickListener = onItemClickListener;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final ImageView img;
        final TextView off;
        final TextView name;
        ViewHolder(View view){
            super(view);

            img=view.findViewById(R.id.general_food_img);
            off=view.findViewById(R.id.general_food_offer);
            name=view.findViewById(R.id.general_food_hotel);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(null, v, getAdapterPosition(), v.getId());
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {
        View view=inflater.inflate(R.layout.general_food_rv_todo,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        menuObjForFood current=list.get(i);
        if(current.getObject().getPicUrl()!=null){
            Glide.with(context)
                    .load(current.getObject().getPicUrl())
                    .into(viewHolder.img);
        }
        viewHolder.off.setText(current.getObject().getOffer() +"% Off");
        setHotel(viewHolder.name,current.getRid());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    private  void setHotel(final TextView view, String rid){
        FirebaseDatabase.getInstance().getReference("restaurants").child(rid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getKey().equals("name")){
                    view.setText(dataSnapshot.getValue(String.class));
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


}
