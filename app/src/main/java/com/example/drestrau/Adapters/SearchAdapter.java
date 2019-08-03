package com.example.drestrau.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.drestrau.R;
import com.example.drestrau.RoomRelated.searchObject;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<searchObject> {
    ArrayList<searchObject> list;
    public SearchAdapter(Context context,ArrayList<searchObject> ls){
        super(context,0,ls);
        list=ls;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=convertView;
        if(view==null){
            view= LayoutInflater.from(getContext()).inflate(R.layout.search_todo,parent,false);
        }
        TextView name=view.findViewById(R.id.searchTodoName);
        name.setText(list.get(position).getName());
        return  view;
    }
}
