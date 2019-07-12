package com.example.drestrau.Activities.User;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.drestrau.Adapters.MyOrderAdapter;
import com.example.drestrau.R;
import com.example.drestrau.RoomRelated.MyOrderObject;
import com.example.drestrau.RoomRelated.OrderDatabase;

import java.util.ArrayList;
import java.util.List;


public class GeneralMyOrdersFragment extends Fragment {
    private RecyclerView rv;
    private MyOrderAdapter adapter;
    private ArrayList<MyOrderObject> list;

    private OrderDatabase mInstance;

    public GeneralMyOrdersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_my_orders, container, false);
        Log.e("Fragment", "onCreateView: My Order Fragment Populated" );
        rv=view.findViewById(R.id.my_order_fragment_rv);
        list=new ArrayList<>();

        adapter=new MyOrderAdapter(getContext(),list);
//        Thread thread=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                mInstance=OrderDatabase.getInstance(getContext());
//                List<MyOrderObject> lst=mInstance.objectDao().loadAllObjects();
//                list.addAll(lst);
//                adapter.setList(list);
//               // adapter.notifyDataSetChanged();
//            }
//        });
//        thread.start();
//
//
        myCustomAsync newasnc=new myCustomAsync();
        newasnc.execute("String");
        rv.setHasFixedSize(true);

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
    public class myCustomAsync extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            mInstance=OrderDatabase.getInstance(getContext());
                List<MyOrderObject> lst=mInstance.objectDao().loadAllObjects();
                list.addAll(lst);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }
}