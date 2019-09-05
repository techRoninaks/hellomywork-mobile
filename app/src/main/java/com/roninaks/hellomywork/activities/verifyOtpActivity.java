package com.roninaks.hellomywork.activities;

import android.app.Activity;
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
import com.roninaks.hellomywork.interfaces.SmsListener;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class verifyOtpActivity extends AppCompatActivity {

    private OtpView otpView;
    String verificationOtp;
    Activity activity;
    boolean password_change;
    SharedPreferences sharedPreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfy_otp);
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
        verificationOtp = "123123";
        Log.d("onOtpCompleted=>", verificationOtp);

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {

                // do Stuff
                if(!password_change) {
                    if (otp.equals(verificationOtp)) {

                        Intent intent = new Intent(verifyOtpActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("return_path", "premium_signup");
                        bundle.putInt("arg_count", 3);
                        bundle.putString("param_1", "");
                        bundle.putString("param_2", "");
                        bundle.putString("param_3", "");
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        finish();
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

    @Override
    public void onBackPressed()
    {
        sharedPreferences.edit().putBoolean("back_pressed",true).commit();
        super.onBackPressed();
    }
}
