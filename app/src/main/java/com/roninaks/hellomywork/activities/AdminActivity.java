package com.roninaks.hellomywork.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.barnettwong.dragfloatactionbuttonlibrary.view.DragFloatActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.AdminLogin;
import com.roninaks.hellomywork.fragments.DashboardFragment;
import com.roninaks.hellomywork.fragments.CareersFragment;
import com.roninaks.hellomywork.fragments.HomeFragment;
import com.roninaks.hellomywork.fragments.ManageCustomerFragment;
import com.roninaks.hellomywork.fragments.ManageEmployeesFragment;
import com.roninaks.hellomywork.fragments.ManageRolesFragment;
import com.roninaks.hellomywork.fragments.MoreOptionsFragment;
import com.roninaks.hellomywork.fragments.PremiumSignupFragment;
import com.roninaks.hellomywork.fragments.SearchLanding;
import com.roninaks.hellomywork.fragments.UnionsFragment;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationViewEx navigation;
    private MenuItem PreviousMenuItem;
    private String args[];
    private Bundle bundle;

    private FloatingActionButton floatActionAddUser;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            try {
                if(PreviousMenuItem != null)
                    setDefaultIcon(PreviousMenuItem);
                PreviousMenuItem = item;
                switch (item.getItemId()) {
                    //fragments are selected based on the item clicked
                    case R.id.navigation_home: //dashboard fragment
                    {
                        item.setIcon(R.drawable.ic_home_fill);
                        if(AdminActivity.this.isLoggedIn().isEmpty()){
                            Fragment fragment = AdminLogin.newInstance("", "");
                            initFragment(fragment);
                        }else {
                            DashboardFragment dashboardFragment = DashboardFragment.newInstance("", "");
                            initFragment(dashboardFragment, "Dashboard");
                        }
                    }
                    return true;
                    case R.id.navigation_search://add customer fragment
                    {
                        item.setIcon(R.drawable.manage_customer_filled);
                        ManageCustomerFragment manageCustomerFragment = ManageCustomerFragment.newInstance("", "");
                        initFragment(manageCustomerFragment);
                    }
                    return true;
                    case R.id.navigation_union: //employee fragment
                    {
                        item.setIcon(R.drawable.manage_employee_filled);
                        ManageEmployeesFragment manageEmployeesFragment = ManageEmployeesFragment.newInstance("","");
                        initFragment(manageEmployeesFragment);
                    }
                    return true;
                    case R.id.navigation_bookmark: //help fragment
                    {
                        item.setIcon(R.drawable.ic_bookmark_fill);
                        MoreOptionsFragment moreOptionsFragment = MoreOptionsFragment.newInstance("","");
                        initFragment(moreOptionsFragment);
                    }
                    return true;
                    case R.id.navigation_careers: //help fragment
                    {
                        item.setIcon(R.drawable.manage_roles_filled);
                        ManageRolesFragment manageRolesFragment = ManageRolesFragment.newInstance("","");
                        initFragment(manageRolesFragment);
                    }
                    return true;
                }
            }catch (Exception e) {
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        try {
            bundle = getIntent().getExtras().getBundle("bundle");
        } catch (Exception e) {
        }

        navigation = findViewById(R.id.navigation_bar_admin);
        floatActionAddUser = findViewById(R.id.add_user_fab);
        //Floating Action Button Onclick
        floatActionAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //Set Bottom navigation properties
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        setFirstPage();
    }

    public void initFragment(Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, tag).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void hideFab(){
        floatActionAddUser.hide();
    }

    public void initFragment(Fragment fragment){
        initFragment(fragment, "");
    }

    public void initFragment(DialogFragment fragment){
        fragment.show(getSupportFragmentManager(), "PostAdsFragment");
    }

    public void logoutAdmin(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("hmw", 0);
        sharedPreferences.edit().putString("emp_id", "")
                .commit();
        sharedPreferences.edit().putBoolean("is_loggedin_admin",false).commit();

        Fragment fragment = AdminLogin.newInstance("", "");
        initFragment(fragment);
        floatActionAddUser.hide();
    }

    public String isLoggedIn(){
        boolean loggedIn = true;
        String userId = "";
        SharedPreferences sharedPreferences = AdminActivity.this.getSharedPreferences("hmw", 0);
        loggedIn = sharedPreferences.getBoolean("is_loggedin_admin", false);
        if(loggedIn)
            userId = sharedPreferences.getString("emp_id", "");
            floatActionAddUser.show();
        if(userId.isEmpty())
            sharedPreferences.edit().putBoolean("is_loggedin_admin", false).commit();
        return userId;
    }

    private void setDefaultIcon(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.navigation_home: //dashboard fragment
            {
                menuItem.setIcon(R.drawable.ic_home);
            }
            break;
            case R.id.navigation_search://manage customer fragment
            {
                menuItem.setIcon(R.drawable.manage_customer);
            }
            break;
            case R.id.navigation_union: //manage employees fragment
            {
                menuItem.setIcon(R.drawable.manage_employee);
            }
            break;
            case R.id.navigation_careers: //manage roles fragment
            {
                menuItem.setIcon(R.drawable.manage_roles);
            }
            break;
            case R.id.navigation_bookmark: //bookmark fragment
            {
                menuItem.setIcon(R.drawable.ic_bookmark);
            }
            break;
        }
    }

    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void setFirstPage(){
        if(bundle != null){
            int argumentSize = bundle.getInt("arg_count");
            args = new String[argumentSize];
            if(argumentSize > 0){
                for(int i = 0; i < argumentSize; i++){
                    args[i] = bundle.getString("param_" + i);
                }
                String path = bundle.getString("return_path");
                switch (path){
                    case "premium_signup":{
                        Fragment fragment = PremiumSignupFragment.newInstance(args[0], args[1], args[2]);
                        initFragment(fragment);
                    }
                }
                return;
            }
        }
        navigation.setSelectedItemId(R.id.navigation_home);
//        Fragment fragment = DashboardFragment.newInstance("", "");
//        initFragment(fragment, "Dashboard");
    }

}
