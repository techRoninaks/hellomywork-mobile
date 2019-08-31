package com.roninaks.hellomywork.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.HomeFragment;
import com.roninaks.hellomywork.fragments.PremiumSignupFragment;
import com.roninaks.hellomywork.fragments.SearchResults;

import java.net.URLEncoder;

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
}
