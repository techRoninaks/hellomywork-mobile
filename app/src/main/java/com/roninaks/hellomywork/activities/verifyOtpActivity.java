package com.roninaks.hellomywork.activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverApi;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.helpers.EmailHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SmsListener;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import org.json.JSONObject;

import androidx.fragment.app.Fragment;
import es.dmoral.toasty.Toasty;

public class verifyOtpActivity extends AppCompatActivity implements SqlDelegate {

    private static final int SMS_CONSENT_REQUEST = 209;

    private OtpView otpView;
    String verificationOtp;
    Activity activity;
    boolean password_change;
    SharedPreferences sharedPreferences ;
    private String name, phone, type, empId;
    ImageView backBtn;
    TextView resendOtp;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verfy_otp);
        try {
            Bundle bundle = getIntent().getBundleExtra("bundle");
            name = bundle.getString("name");
            phone = bundle.getString("phone");
            verificationOtp = bundle.getString("otp");
        }
        catch (Exception e){}
        activity = this;
        password_change = false;
        otpView = findViewById(R.id.otp_view);
        backBtn = findViewById(R.id.imageViewBackOtp);
        resendOtp = findViewById(R.id.textView_ResendOtp);
        sharedPreferences = verifyOtpActivity.this.getSharedPreferences("hmw", 0);
//        verificationOtp = sharedPreferences.getString("otp","");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });

        resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "Your OTP for verification is "+verificationOtp;
                resendOtp(verificationOtp,msg,phone);
                count++;
                if(count>1){
                    resendOtp.setVisibility(View.INVISIBLE);
                }
            }
        });

//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                otpView.setText(messageText);
//            }
//        });
        try {
            password_change = getIntent().getBooleanExtra("change_pass", false);
        }catch (Exception e){}

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsVerificationReceiver, intentFilter);

        //Start listening for SMS User Consent broadcasts from senderPhoneNumber
        if((name.equals("$%5login5%$")))
        smsTask();

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

    private void smsTask() {
        Task<Void> task = SmsRetriever.getClient(this).startSmsUserConsent(null);
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> listener) {
                if (listener.isSuccessful()) {
                    // Task completed successfully
                    String msg = "Your OTP for verification is "+verificationOtp;
                    resendOtp(verificationOtp,msg,phone);
                    Log.d("", "Success");
                } else {
                    // Task failed with an exception
                    Exception exception = listener.getException();
                    exception.printStackTrace();
                }
            }
        });
    }

    private void resendOtp(String otp,String message, String phone){
        try {
            SqlHelper sqlHelper = new SqlHelper(verifyOtpActivity.this, verifyOtpActivity.this);
            sqlHelper.setExecutePath("test1.php");
            sqlHelper.setActionString("resend");
            ContentValues contentValues = new ContentValues();
            contentValues.put("message",message);
            contentValues.put("number",phone);
            contentValues.put("OTP",otp);
            sqlHelper.setParams(contentValues);
            sqlHelper.setMethod(getString(R.string.method_post));
            sqlHelper.executeUrl(true);
        }catch (Exception e){
            EmailHelper emailHelper = new EmailHelper(verifyOtpActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }

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


    // Set to an unused request code
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status smsRetrieverStatus = (Status)extras.get(SmsRetriever.EXTRA_STATUS);

                switch (smsRetrieverStatus.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        // Get consent intent
                        Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                        try {
                            /*Start activity to show consent dialog to user within
                             *5 minutes, otherwise you'll receive another TIMEOUT intent
                             */
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        }
                        catch (ActivityNotFoundException e) {
                            // Handle the exception
                        }
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Time out occurred, handle the error.
                        break;
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SMS_CONSENT_REQUEST:
                if (resultCode == RESULT_OK) {
                    // Get SMS message content
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    // Extract one-time code from the message and complete verification
                    String oneTimeCode = parseOneTimeCode(message);
                    //for this demo we will display it instead
                    otpView.setText(oneTimeCode);
                } else {
                    // Consent canceled, handle the error
                }
                break;
        }
    }

    private String parseOneTimeCode(String message) {
        //simple number extractor
        return message.replaceAll("[^0-9]", "");
    }


    @Override
    public void onBackPressed()
    {
        sharedPreferences.edit().putBoolean("back_pressed",true).commit();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //to prevent IntentReceiver leakage unregister
        unregisterReceiver(smsVerificationReceiver);
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
                        bundle.putString("param_1", sqlHelper.getStringResponse().split("~")[1]);
                        bundle.putString("param_2", "");
                        intent.putExtra("bundle", bundle);
                        startActivity(intent);
                        finish();
                    }
                    break;
                }
                case "resend":{
                    //TODO Add functionality if required

                }
            }
        }catch (Exception e){

        }
    }
}