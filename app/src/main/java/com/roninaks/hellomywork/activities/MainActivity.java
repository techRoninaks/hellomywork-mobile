package com.roninaks.hellomywork.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragment.CareersFragment;
import com.roninaks.hellomywork.fragment.ProfileFragment;

//import android.app.Fragment;
//import android.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

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
        navigation = findViewById(R.id.navigation);

        //Set Bottom navigation properties
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
//        navigation.setItemIconTintList(null);
    }
}
