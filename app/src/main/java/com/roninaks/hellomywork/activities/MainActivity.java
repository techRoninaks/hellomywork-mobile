package com.roninaks.hellomywork.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.barnettwong.dragfloatactionbuttonlibrary.view.DragFloatActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.CareersFragment;
import com.roninaks.hellomywork.fragments.ContactFragment;
import com.roninaks.hellomywork.fragments.HomeFragment;
import com.roninaks.hellomywork.fragments.PostAdFragment;
import com.roninaks.hellomywork.fragments.PremiumSignupFragment;
import com.roninaks.hellomywork.fragments.SearchLanding;
import com.roninaks.hellomywork.fragments.UnionsFragment;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationViewEx navigation;
    private MenuItem PreviousMenuItem;
    private DragFloatActionButton floatActionButton;

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
                    case R.id.navigation_dashboard: //dashboard fragment
                    {
                        item.setIcon(R.drawable.ic_home_fill);
                        HomeFragment homeFragment = HomeFragment.newInstance("","");
                        initFragment(homeFragment);
                    }
                    return true;
                    case R.id.navigation_add_client://add customer fragment
                    {
                        item.setIcon(R.drawable.ic_search_fill);
                        SearchLanding searchLanding = SearchLanding.newInstance("", "");
                        initFragment(searchLanding);
                    }
                    return true;
                    case R.id.navigation_users: //employee fragment
                    {
                        item.setIcon(R.drawable.ic_unions_fill);
                        UnionsFragment unionsFragment = UnionsFragment.newInstance("","");
                        initFragment(unionsFragment);
                    }
                    return true;
                    case R.id.navigation_help: //help fragment
                    {
                        item.setIcon(R.drawable.ic_bookmark_fill);
                    }
                    return true;
                    case R.id.navigation_careers: //help fragment
                    {
                        item.setIcon(R.drawable.ic_careers_fill);
                        CareersFragment careersFragment = new CareersFragment();
                        initFragment(careersFragment);
                    }
                    return true;
                }
            }catch (Exception e) {
//                Log.e("Main:BottomSheet", e.getMessage());
            }
            return false;
        }
    };

//    private void initFragment(Fragment fragment) {
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.content, fragment, "FragmentName");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        floatActionButton = findViewById(R.id.fab_post_ad);

        //Set Defaults
        if(isLoggedIn().equals("")){
            floatActionButton.setVisibility(View.GONE);
        }

        //Floating Action Button Onclick
        floatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = PostAdFragment.newInstance("", "");
                initFragment(fragment);
            }
        });
        //Set Bottom navigation properties
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        navigation.setSelectedItemId(R.id.navigation_dashboard);
//        navigation.setItemIconTintList(null);

        Fragment fragment = HomeFragment.newInstance("ca", "1");
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "PremiumSignup");
        fragmentTransaction.commit();
    }

    public void initFragment(Fragment fragment, String tag){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, tag).addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void initFragment(Fragment fragment){
        initFragment(fragment, "");
    }

    public void initFragment(DialogFragment fragment){
        fragment.show(getSupportFragmentManager(), "PostAdsFragment");
    }

    private void setDefaultIcon(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.navigation_dashboard: //home fragment
            {
                menuItem.setIcon(R.drawable.ic_home);
            }
            break;
            case R.id.navigation_add_client://movies fragment
            {
                menuItem.setIcon(R.drawable.ic_search);
            }
            break;
            case R.id.navigation_users: //post status fragment
            {
                menuItem.setIcon(R.drawable.ic_unions);
            }
            break;
            case R.id.navigation_careers: //favourites fragment
            {
                menuItem.setIcon(R.drawable.ic_careers_);
            }
            break;
            case R.id.navigation_help: //profile fragment
            {
                menuItem.setIcon(R.drawable.ic_bookmark);
            }
            break;
        }
    }

    public void sendWhatsapp(String phone){
        PackageManager packageManager = this.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode("Greetings. I would like to know your rates.", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                this.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void callPhone(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);

    }

    public void sendMail(String email){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hellomywork: Service");
        intent.putExtra(Intent.EXTRA_TEXT, "Greetings from Hellomywork.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public String isLoggedIn(){
        boolean loggedIn = true;
        String userId = "";
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("hwm", 0);
        loggedIn = sharedPreferences.getBoolean("is_loggedin", false);
        if(loggedIn)
            userId = sharedPreferences.getString("user_id", "");
        if(userId.isEmpty())
            sharedPreferences.edit().putBoolean("is_loggedin", false).commit();
        return userId;
    }
}
