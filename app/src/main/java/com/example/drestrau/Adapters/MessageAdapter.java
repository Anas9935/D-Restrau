package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.drestrau.Activities.utilityClass;
import com.example.drestrau.Objects.messageObject;
import com.example.drestrau.Objects.users;
import com.example.drestrau.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessageAdapter extends ArrayAdapter<messageObject> {
    ArrayList<messageObject> list;
    String myUid;
    public MessageAdapter(Context context,ArrayList<messageObject> ls,String id) {
        super(context,0,ls);
        list=ls;
        myUid=id;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View view=convertView;
        messageObject current=list.get(position);
        if(view==null){
            if(!current.getFromUid().equals(myUid))
            {
                view= LayoutInflater.from(getContext()).inflate(R.layout.message_todo_left,parent,false);
            }else if(current.getFromUid().equals(myUid)){
                view= LayoutInflater.from(getContext()).inflate(R.layout.message_todo_right,parent,false);
            }
        }

        TextView name,message,time;
        ImageView desigIcon;

        name=view.findViewById(R.id.message_name);
        message=view.findViewById(R.id.message_mess);
        time=view.findViewById(R.id.message_time);
        desigIcon=view.findViewById(R.id.message_img);

        setNameAndIcon(name,desigIcon,current.getFromUid());
        message.setText(current.getMessage());
        time.setText(utilityClass.getDate(current.getTimestamp())+" "+utilityClass.getTime(current.getTimestamp()));

        return view;
    }
    private void setNameAndIcon(final TextView name, final ImageView icon, String uid){
        //Set name and icon, if name not present set it according to the designation
        FirebaseDatabase.getInstance().getReference("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users user=dataSnapshot.getValue(users.class);
                if(user!=null){
                    if(user.getName()!=null){
                        name.setText(user.getName());
                    }else{
                        name.setText("Staff");
                    }
                    switch (user.getDesig()){
                        case 0:{
                                icon.setImageResource(R.drawable.receptionist);
                            break;
                        }
                        case 1:{                                icon.setImageResource(R.drawable.chef);
                            break;
                        }
                        case 2:{                                icon.setImageResource(R.drawable.cook);
                            break;
                        }
                        case 3:{                                icon.setImageResource(R.drawable.dishwasher);
                            break;
                        }
                        case 4:{                                icon.setImageResource(R.drawable.waiter);
                            break;
                        }
                        case 5:{                                icon.setImageResource(R.drawable.cleaner);
                            break;
                        }
                        case 6:{                                icon.setImageResource(R.drawable.bartender);
                            break;
                        }
                        case 7:{                                icon.setImageResource(R.drawable.guard);
                            break;
                        }
                        case 8:{                                icon.setImageResource(R.drawable.manager);
                            break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
