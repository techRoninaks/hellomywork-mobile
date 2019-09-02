package com.roninaks.hellomywork.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.roninaks.hellomywork.R;

import es.dmoral.toasty.Toasty;

public class verifyOtpActivity extends AppCompatActivity {

    private OtpView otpView;
    String verificationOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfy_otp);

        SharedPreferences sharedPreferences = this.getSharedPreferences("HelloMyWork", 0);
        verificationOtp = sharedPreferences.getString("otp", "");

        Log.d("onOtpCompleted=>", verificationOtp);

        otpView = findViewById(R.id.otp_view);
        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override public void onOtpCompleted(String otp) {

                // do Stuff
                if (otp.equals(verificationOtp)) {
                    Log.d("onOtpCompleted=>", otp);
                    Log.d("onOtpCompleted=>", verificationOtp);
                    Intent intent = new Intent(verifyOtpActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    otpView.setText("");
                    Toasty.error(verifyOtpActivity.this,"Entered OTP is incorrect", Toast.LENGTH_SHORT, false).show();
                }
            }
        });
    }
}
