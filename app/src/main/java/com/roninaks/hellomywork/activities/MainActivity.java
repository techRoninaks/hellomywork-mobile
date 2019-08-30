package com.roninaks.hellomywork.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.roninaks.hellomywork.R;

public class MainActivity extends AppCompatActivity {

    Button btnTest;

    private BottomNavigationViewEx navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            try {
                switch (item.getItemId()) {
                    //fragments are selected based on the item clicked
                    case R.id.navigation_dashboard: //dashboard fragment
                    {

                    }
                    return true;
                    case R.id.navigation_add_client://add customer fragment
                    {

                    }
                    return true;
                    case R.id.navigation_users: //employee fragment
                    {

                    }
                    return true;
                    case R.id.navigation_help: //help fragment
                    {

                    }
                    return true;
                }
            }catch (Exception e) {
//                Log.e("Main:BottomSheet", e.getMessage());
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = (Button) findViewById(R.id.buttonTest);

        btnTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(myIntent);
            }
        });

//        navigation = findViewById(R.id.navigation);
//
//        //Set Bottom navigation properties
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.enableAnimation(false);
//        navigation.enableShiftingMode(false);
//        navigation.enableItemShiftingMode(false);
//        navigation.setTextVisibility(false);
//        navigation.setSelectedItemId(R.id.navigation_dashboard);
//        navigation.setItemIconTintList(null);
    }
}
