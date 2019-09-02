package com.roninaks.hellomywork.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.helpers.EmailHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import org.json.JSONObject;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity implements SqlDelegate{

    EditText editTextPhoneNumberRegister;
    EditText editTextNameRegister;
    TextView textViewLogIn;
    Activity activity;
    String userName,userPhone,message;
    Button btnSendOtpRegister;
    int verifyOtp;

    int errorCount = 0;
    public final int ERROR_THRESHOLD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = this;

        btnSendOtpRegister = (Button) findViewById(R.id.button_SendOtpReg);
        editTextPhoneNumberRegister = (EditText) findViewById(R.id.editText_Phone);
        editTextNameRegister = (EditText) findViewById(R.id.editText_UserName);
        textViewLogIn = (TextView) findViewById(R.id.textView_SignUp);

        View.OnClickListener onButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    switch (view.getId()) {

                        case R.id.button_SendOtpReg:
                            if(validCred()) {
                                verifyOtp = generateOtp();
                                message = "Your OTP for verification is " + verifyOtp;
                                userPhone = editTextPhoneNumberRegister.getText().toString();
                                userName = editTextNameRegister.getText().toString();
                                verifyUser(message, userPhone, verifyOtp);

                            }
                            break;

                        case R.id.textView_SignUp:
                            onBackPressed();
                            finish();
                            break;
                    }

                }
                catch (Exception e){
                        EmailHelper emailHelper = new EmailHelper(RegisterActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
                        emailHelper.sendEmail();
                }
            }
        };
        btnSendOtpRegister.setOnClickListener(onButtonClickListener);
        textViewLogIn.setOnClickListener(onButtonClickListener);
    }


    private boolean validCred() {
        String phoneNumber, username;
        boolean logIn = true;
        phoneNumber = editTextPhoneNumberRegister.getText().toString();
        username = editTextNameRegister.getText().toString();
        if(phoneNumber.isEmpty()){
            editTextPhoneNumberRegister.setError(getString(R.string.required_field));
            logIn = false;
        }
        if(username.isEmpty()){
            editTextNameRegister.setError(getString(R.string.required_field));
            logIn = false;

        }
        else if(!phoneNumber.isEmpty() || !username.isEmpty()){
            if(!isValidPhoneNumber(phoneNumber)){
                editTextNameRegister.setText("");
                editTextPhoneNumberRegister.setText("");
                Toasty.error(RegisterActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                logIn = false;
            }
            if(!isValidUserName(username)){
                editTextNameRegister.setText("");
                editTextPhoneNumberRegister.setText("");
                Toasty.error(RegisterActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                logIn = false;
            }
        }
        return logIn;
    }




    /*
    The function validates the entered phone number.
    Returns isValid true if phone number is valid
     */
    public static boolean isValidPhoneNumber(String phoneNumber){
        boolean isValid = false;
        if(!Pattern.matches("[a-zA-Z]+", phoneNumber))
            isValid = true;
        return isValid;
    }



    public static boolean isValidUserName(String user){
        int length = user.length();
        if(length < 3)
            return false;
        else
            return true;
    }


    public static int generateOtp(){
        double randomOtp;
        randomOtp = Math.floor(100000 + Math.random() * 900000);
        int x = (int) randomOtp;
        return x;
    }





    private void verifyUser(String message,String userPhone,int verifyOtp){
        try {
            SqlHelper sqlHelper = new SqlHelper(RegisterActivity.this, RegisterActivity.this);
            sqlHelper.setExecutePath("test.php");
//            sqlHelper.setActionString("login");
            String verOtp = String.valueOf(verifyOtp);
            ContentValues contentValues = new ContentValues();
            contentValues.put("message",message);
            contentValues.put("number",userPhone);
            contentValues.put("OTP",verOtp);
            //contentValues.put("userPassword", password);
            sqlHelper.setParams(contentValues);
            sqlHelper.setMethod(getString(R.string.method_post));
            sqlHelper.executeUrl(true);
        }catch (Exception e){
            EmailHelper emailHelper = new EmailHelper(RegisterActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }
    }


    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try {
            JSONObject jsonObject = sqlHelper.getJSONResponse();
            String response = jsonObject.getString("userId");
//                    String response = sqlHelper.getStringResponse();
            if (!(response.isEmpty())) {
                SharedPreferences sharedPreferences = this.getSharedPreferences("HelloMyWork", 0);
                sharedPreferences.edit().putString("otp", String.valueOf(verifyOtp)).apply();
                Intent intent = new Intent(RegisterActivity.this, verifyOtpActivity.class);
                startActivity(intent);
                finish();
            } else if (jsonObject.isNull("0")) {
//                editTextPassword.setError(getString(R.string.invalid_cred));
//                editTextUsername.setError(getString(R.string.invalid_cred));
                Toasty.error(RegisterActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                if (errorCount % ERROR_THRESHOLD == 0) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialog.setTitle(getString(R.string.new_user));
                    alertDialog.setMessage(getString(R.string.new_user_prompt));
                    alertDialog.setPositiveButton(R.string.confirmation_yes, dialogClickListener);
                    alertDialog.setNegativeButton(R.string.confirmation_no, dialogClickListener);
                    alertDialog.show();
                    errorCount++;
                } else {
                    errorCount++;
                    Toasty.error(RegisterActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                }
            } else if (response.equals(getString(R.string.exception))) {
                Toast.makeText(RegisterActivity.this, getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
            }



        } catch (Exception e) {
            EmailHelper emailHelper = new EmailHelper(RegisterActivity.this, EmailHelper.TECH_SUPPORT, "Error: RegisterActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
            Toast.makeText(RegisterActivity.this, getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
        }

    }


}


