package com.roninaks.hellomywork.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import es.dmoral.toasty.Toasty;

import com.barnettwong.dragfloatactionbuttonlibrary.view.DragFloatActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.razorpay.PaymentResultListener;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.BookmarkFragment;
import com.roninaks.hellomywork.fragments.CareersFragment;
import com.roninaks.hellomywork.fragments.ContactFragment;
import com.roninaks.hellomywork.fragments.HomeFragment;

import com.roninaks.hellomywork.fragments.PlansFragment;
import com.roninaks.hellomywork.fragments.PostAdFragment;
import com.roninaks.hellomywork.fragments.PremiumSignupFragment;
import com.roninaks.hellomywork.fragments.ProfileFragment;
import com.roninaks.hellomywork.fragments.SearchLanding;
import com.roninaks.hellomywork.fragments.UnionsFragment;
import com.roninaks.hellomywork.helpers.SqlHelper;

import java.net.URLEncoder;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements PaymentResultListener {

    private BottomNavigationViewEx navigation;
    private MenuItem PreviousMenuItem;
    private String args[];
    private Bundle bundle;

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
                    case R.id.navigation_home: //dashboard fragment
                    {
                        item.setIcon(R.drawable.ic_home_fill);
                        HomeFragment homeFragment = HomeFragment.newInstance("","");
                        initFragment(homeFragment);
                    }
                    return true;
                    case R.id.navigation_search://add customer fragment
                    {
                        item.setIcon(R.drawable.ic_search_fill);
                        SearchLanding searchLanding = SearchLanding.newInstance("", "");
                        initFragment(searchLanding);
                    }
                    return true;
                    case R.id.navigation_union: //employee fragment
                    {
                        item.setIcon(R.drawable.ic_unions_fill);
                        UnionsFragment unionsFragment = UnionsFragment.newInstance("","");
                        initFragment(unionsFragment);
                    }
                    return true;
                    case R.id.navigation_bookmark: //help fragment
                    {
                        if(!isLoggedIn().isEmpty()) {
                            item.setIcon(R.drawable.ic_bookmark_fill);
                            BookmarkFragment bookmarkFragment = BookmarkFragment.newInstance("", "");
                            initFragment(bookmarkFragment);
                        }else{
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                                            startActivity(myIntent);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                                            break;
                                        default:
                                            Toast.makeText(MainActivity.this, "Nothing", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                }
                            };

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Oho!, You are not Logged In");
                            builder.setMessage("You need to login to view this page").setPositiveButton("Go to login?", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
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
        try {
            bundle = getIntent().getExtras().getBundle("bundle");
        }catch (Exception e){}

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
                initFragment(fragment, "PostAdFragment");
            }
        });
        //Set Bottom navigation properties
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.enableAnimation(false);
        navigation.enableShiftingMode(false);
        navigation.enableItemShiftingMode(false);
        navigation.setTextVisibility(false);
        setFirstPage();
//        navigation.setSelectedItemId(R.id.navigation_dashboard);
//        navigation.setItemIconTintList(null);

//        Fragment fragment = PlansFragment.newInstance("ca", "1");
//        initFragment(fragment);
//        fragmentTransaction.replace(R.id.content, fragment, "PremiumSignup");
//        fragmentTransaction.commit();
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
        initFragment(fragment, "");
    }

    public void initFragment(DialogFragment fragment, String tag){
        fragment.show(getSupportFragmentManager(), tag);
    }

    private void setDefaultIcon(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.navigation_home: //home fragment
            {
                menuItem.setIcon(R.drawable.ic_home);
            }
            break;
            case R.id.navigation_search://search fragment
            {
                menuItem.setIcon(R.drawable.ic_search);
            }
            break;
            case R.id.navigation_union: //union fragment
            {
                menuItem.setIcon(R.drawable.ic_unions);
            }
            break;
            case R.id.navigation_careers: //careers fragment
            {
                menuItem.setIcon(R.drawable.ic_careers_);
            }
            break;
            case R.id.navigation_bookmark: //bookmark fragment
            {
                menuItem.setIcon(R.drawable.ic_bookmark);
            }
            break;
        }
    }

    /***
     * This function allows direct messaging to the whatsapp number
     * @param phone - The whatsapp number to send message to
     */
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

    /***
     * This function opens up the dial pad with the number filled in
     * @param phone - The phone number to call
     */
    public void callPhone(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);

    }

    /***
     * This function allows sending email to the desingated email id
     * @param email - Email id to send mails to
     */
    public void sendMail(String email){
        Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("mailto:" + email));
        //intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Hellomywork: Service");
        intent.putExtra(Intent.EXTRA_TEXT, "Greetings from Hellomywork.");
        startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public String isLoggedIn(){
        boolean loggedIn = true;
        String userId = "";
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("hmw", 0);
        loggedIn = sharedPreferences.getBoolean("is_loggedin", false);
        if(loggedIn)
            userId = sharedPreferences.getString("user_id", "");
        if(userId.isEmpty())
            sharedPreferences.edit().putBoolean("is_loggedin", false).commit();
        return userId;
    }

    public String isAdminLoggedIn(){
        boolean loggedIn = true;
        String userId = "";
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("hmw", 0);
        loggedIn = sharedPreferences.getBoolean("is_loggedin_admin", false);
        if(loggedIn)
            userId = sharedPreferences.getString("emp_id", "");
        if(userId.isEmpty())
            sharedPreferences.edit().putBoolean("is_loggedin_admin", false).commit();
        return userId;
    }

    public String getDefaultLocation(){
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences("hmw", 0);
        return sharedPreferences.getString("location", "1");

    }

    public void logout(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("hmw", 0);
        sharedPreferences.edit().putString("user_id", "")
                .commit();
        sharedPreferences.edit().putBoolean("is_loggedin",false).commit();
        Fragment fragment = HomeFragment.newInstance("", "");
        initFragment(fragment);
        floatActionButton.setVisibility(View.GONE);
    }

    public Geocoder getLocation(){
        return new Geocoder(MainActivity.this);
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
                        break;
                    }
                    case "profile":{
                        Fragment fragment = ProfileFragment.newInstance(args[0], args[1]);
                        initFragment(fragment);
                        break;
                    }
                }
                return;
            }
        }
        navigation.setSelectedItemId(R.id.navigation_home);
        Fragment fragment = HomeFragment.newInstance("", "");
        initFragment(fragment);
    }

    @Override
    public void onPaymentSuccess(String s) {
        PlansFragment fragment = (PlansFragment) getSupportFragmentManager().findFragmentByTag("plans");
        if (fragment != null && fragment.isVisible()) {
            fragment.saveUserPlan();
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toasty.error(MainActivity.this, getString(R.string.payment_failed), Toasty.LENGTH_SHORT).show();
    }
}
