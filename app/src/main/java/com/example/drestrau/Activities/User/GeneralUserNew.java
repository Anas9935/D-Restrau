package com.example.drestrau.Activities.User;

import android.content.Intent;
import android.os.Bundle;

import com.example.drestrau.Activities.ProfileActivity;
import com.example.drestrau.R;

import com.example.drestrau.TestingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneralUserNew extends AppCompatActivity {


    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.general_fragment_container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}
