package com.example.drestrau.Activities.User;

import android.content.Intent;
import android.os.Bundle;

import com.example.drestrau.Activities.ProfileActivity;
import com.example.drestrau.R;

import com.example.drestrau.TestingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.MenuItem;

public class GeneralUserNew extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment=null;
            switch (item.getItemId()) {
                case R.id.action_general_home:{
                    selectedFragment=new GeneralFragmentHome();
                    break;
                }
                case R.id.action_general_PastOrder:{
                    selectedFragment=new GeneralMyOrdersFragment();
                    break;
                }
                case R.id.action_general_search:{
                    selectedFragment=new GeneralSearchFragment();
                    break;
                }
                case R.id.action_general_profile:{
                   selectedFragment=new profileFragment();
                   break;
                 }
                case R.id.action_general_dinner:{
                    selectedFragment=new OnDinerFragment();
                    break;
                }

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.general_fragment_container,selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_user_new);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.action_general_home);
    }

}
