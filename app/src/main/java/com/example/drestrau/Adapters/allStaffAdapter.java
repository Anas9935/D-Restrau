package com.example.drestrau.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.drestrau.Activities.Manager.MessageActivity;
import com.example.drestrau.Objects.staffForListObject;
import com.example.drestrau.R;

import java.util.ArrayList;

import static android.Manifest.permission.CALL_PHONE;

public class allStaffAdapter extends ArrayAdapter<staffForListObject> {
    private final ArrayList<staffForListObject> list;
    private int designation=-1;
    public allStaffAdapter(Context context, ArrayList<staffForListObject> lt,int desig) {
        super(context,0,lt);
        list=lt;
        designation=desig;
    }
    public allStaffAdapter(Context context, ArrayList<staffForListObject> lt) {
        super(context,0,lt);
        list=lt;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        View view=convertView;
        if (view == null) {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_todo,parent,false);

        }
        final staffForListObject current=list.get(position);

        ImageView img,callBtn,messageBtn;
        TextView name,id,desig;
        name=view.findViewById(R.id.staff_list_name);
        id=view.findViewById(R.id.staff_list_id);
        img=view.findViewById(R.id.staff_list_image);
        callBtn=view.findViewById(R.id.staff_list_callBtn);
        desig=view.findViewById(R.id.staff_list_desig);
        messageBtn=view.findViewById(R.id.staff_list_messageBtn);

        if(designation==0){
            callBtn.setVisibility(View.GONE);
            messageBtn.setVisibility(View.GONE);
        }


        if(current.getPicUrl()!=null){
            Glide.with(getContext())
                    .load(current.getPicUrl())
                    .into(img);
        }
        switch (current.getDesignaton()){
            case 0:{                desig.setText("Receptionist");
                break;
            }
            case 1:{                desig.setText("Chef");
                break;
            }
            case 2:{                desig.setText("Cook");
                break;
            }
            case 3:{                desig.setText("DishWasher");
                break;
            }
            case 4:{                desig.setText("Waiter");
                break;
            }
            case 5:{                desig.setText("Cleaner");
                break;
            }
            case 6:{                desig.setText("Bartender");
                break;
            }
            case 7:{                desig.setText("Guard");
                break;
            }

        }
        name.setText(current.getName());
        id.setText(current.getStaffId());
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+ current.getContact()));
                if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    getContext().startActivity(intent);
                } else {
                    ActivityCompat.requestPermissions((Activity) getContext(),new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), MessageActivity.class);
                intent.putExtra("suid",current.getStaffUid());
             //   Log.e(TAG, "onClick: "+current.getStaffId() );
                getContext().startActivity(intent);
            }
        });
        if(current.getPaydue()==1){
            //need to be paid
            ConstraintLayout ll=view.findViewById(R.id.staff_todo_root_layout);
            ll.setBackgroundResource(R.drawable.alert_view_gradient);
        }

        return view;
    }
}
