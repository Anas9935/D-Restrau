package com.example.drestrau.Activities.User;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.drestrau.Adapters.SearchAdapter;
import com.example.drestrau.Objects.RestObject;
import com.example.drestrau.Objects.menuObject;
import com.example.drestrau.R;
import com.example.drestrau.RoomRelated.MyOrderObject;
import com.example.drestrau.RoomRelated.OrderDatabase;
import com.example.drestrau.RoomRelated.searchObject;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class GeneralSearchFragment extends Fragment {
SearchView sv;
ListView lv;
TextView searchText;
ArrayList<searchObject> list;
SearchAdapter adapter;
int tabPosition=0;


    public GeneralSearchFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_general_search, container, false);
        initializeViews(view);
        setupTabLayout(view);

        list=new ArrayList<>();
        adapter=new SearchAdapter(getContext(),list);
        lv.setAdapter(adapter);
        setupSearchSettings();
        setupListViewSettings();
        getPastListItems();
        return view;
    }



    private void initializeViews(View view) {
        sv=view.findViewById(R.id.search_view);
        lv=view.findViewById(R.id.search_lv);
        searchText=view.findViewById(R.id.search_searchText);
    }
    private void setupListViewSettings(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Long tm=System.currentTimeMillis()/1000;
            searchObject obj=list.get(position);

            obj.setTimestamp(tm);       //update the time
                saveItemInDatabase(obj);
                Intent intent=new Intent(getContext(),MenuActivity.class);
                intent.putExtra("rid",obj.getRid());
                startActivity(intent);
            }
        });
    }
    private void getPastListItems(){
        list.clear();
            CustomAsyncTask task=new CustomAsyncTask();
            task.execute(tabPosition);
    }

    private void setupSearchSettings() {
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e(TAG, "onQueryTextSubmit: "+query );

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG, "onQueryTextChange: "+newText );
                if(newText==""){
                    searchText.setText("Recent Searches");
                    getPastListItems();
                }
                else{
                    searchText.setText("Search Results");
                }
                getListItems(newText);
                return false;
            }
        });
    }
    private  void getListItems(final String string){
        list.clear();
        if(tabPosition==1)
        {
            FirebaseDatabase.getInstance().getReference("menus").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    for(DataSnapshot child:dataSnapshot.getChildren()){
                        //  Log.e(TAG, "onChildAdded: "+child.getKey() );
                        menuObject mitem=child.getValue(menuObject.class);
                        if(mitem.getName().contains(string)){
                            Log.e(TAG, "onChildAdded: "+mitem.getName() );

                            long time=System.currentTimeMillis()/1000;
                            searchObject object=new searchObject(dataSnapshot.getKey(),mitem.getName(),1,time);
                            adapter.add(object);
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
        }else{
            FirebaseDatabase.getInstance().getReference("restaurants").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    RestObject obj=dataSnapshot.getValue(RestObject.class);
                    if(obj!=null){
                        if(obj.getName().contains(string)){
                            Log.e(TAG, "onChildAdded: "+obj.getName() );
                            long time=System.currentTimeMillis()/1000;
                            searchObject object=new searchObject(obj.getRid(),obj.getName(),0,time);
                            adapter.add(object);
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
        }
    }
    private void setupTabLayout(View view) {
        TabLayout tabLayout=view.findViewById(R.id.search_tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                    onTabTapped(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabTapped(tab.getPosition());
            }
        });
    }

    private void onTabTapped(int position) {
        switch (position){
            case 0:{                //restaurant
                tabPosition=0;
                getPastListItems();
                break;
            }
            case 1:{                //foodItems
                tabPosition=1;
                getPastListItems();
                break;
            }
        }
    }


    private void saveItemInDatabase(final searchObject obj){
        final OrderDatabase db=OrderDatabase.getInstance(getContext());
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                db.searchDao().insertObject(obj);
                Log.e(TAG, "run: data is inserted in room" );
            }
        });
        thread.start();
    }

    public class CustomAsyncTask extends AsyncTask<Integer,Void,String>{
        @Override
        protected String doInBackground(Integer... integers) {
            int type = integers[0];
            OrderDatabase mInstance = OrderDatabase.getInstance(getContext());
            List<searchObject> lst = mInstance.searchDao().loadAllObjects();
            // list.addAll(lst);
            if (type == 0) {
                for (searchObject obj : lst) {
                    if (obj.getType() == 0) {
                        list.add(obj);
                    }
                }
            } else {
                for (searchObject obj : lst) {
                    if (obj.getType() == 1) {
                        list.add(obj);
                    }
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
        }
    }
}
