package com.roninaks.hellomywork.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.PremiumSignupFragment;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SmsListener;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import org.json.JSONObject;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class verifyOtpActivity extends AppCompatActivity implements SqlDelegate {

    private OtpView otpView;
    String verificationOtp;
    Activity activity;
    boolean password_change;
    SharedPreferences sharedPreferences ;
    private String name, phone, type, empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfy_otp);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        activity = this;
        password_change = false;
        otpView = findViewById(R.id.otp_view);
        sharedPreferences = verifyOtpActivity.this.getSharedPreferences("hmw", 0);


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                otpView.setText(messageText);
            }
        });
        try {
            password_change = getIntent().getBooleanExtra("change_pass", false);
        }catch (Exception e){}
        verificationOtp = sharedPreferences.getString("otp", "");
//        verificationOtp = "123123";
        Log.d("onOtpCompleted=>", verificationOtp);

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {

                // do Stuff
                if(!password_change) {
                    if (otp.equals(verificationOtp)) {

                        signUp();

                    } else {
                        otpView.setText("");
                        Toasty.error(verifyOtpActivity.this, "Entered OTP is incorrect", Toast.LENGTH_SHORT, false).show();
                    }
                }
                else{
                    if (otp.equals(verificationOtp)) {
                        sharedPreferences.edit().putBoolean("otp_verified",true).commit();
                        onBackPressed();
                    } else {
                        otpView.setText("");
                        Toasty.error(verifyOtpActivity.this, "Entered OTP is incorrect", Toast.LENGTH_SHORT, false).show();
                    }
                }
            }
        });
    }

    private void signUp() {
        try {
            SqlHelper sqlHelper = new SqlHelper(verifyOtpActivity.this, verifyOtpActivity.this);
            sqlHelper.setExecutePath("signUp.php");
            sqlHelper.setMethod("POST");
            sqlHelper.setActionString("signup");
            ContentValues params = new ContentValues();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("newName", name);
            jsonObject.put("newPhone", phone);
            jsonObject.put("userType", "premium");
            jsonObject.put("emId", "null");
            params.put("jsonObj", jsonObject.toString());
            sqlHelper.setParams(params);
            sqlHelper.executeUrl(true);
        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed()
    {
        sharedPreferences.edit().putBoolean("back_pressed",true).commit();
        super.onBackPressed();
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try{
            switch (sqlHelper.getActionString()){
                case "signup":{
                    if(sqlHelper.getStringResponse().startsWith("1~")){
                        Intent intent = new Intent(verifyOtpActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("return_path", "premium_signup");
                        bundle.putInt("arg_count", 3);
                        bundle.putString("param_0", "new");
                        bundle.putString("param_1", sqlHelper.getStringResponse().split("~")[0]);
                        bundle.putString("param_2", "");
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        finish();
                    }
                    break;
                }
            }
        }catch (Exception e){

        }
    }
}
