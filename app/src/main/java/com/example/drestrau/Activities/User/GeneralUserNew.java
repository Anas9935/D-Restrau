package com.example.drestrau.Activities.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.example.drestrau.R;

import com.example.drestrau.TestingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
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
                    Intent intent=new Intent(GeneralUserNew.this,TestingActivity.class);
                    startActivity(intent);
                    selectedFragment=new GeneralSearchFragment();
                    break;
                }
                case R.id.action_general_profile:{
                    Intent intent=new Intent(GeneralUserNew.this,ProfileActivity.class);
                    startActivity(intent);
                    return true;
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
