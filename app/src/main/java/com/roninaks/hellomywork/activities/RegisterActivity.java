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
import android.widget.ImageView;
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
    String verifyOtp;
    ImageView ivClose;
    boolean from_home;

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
        ivClose = (ImageView) findViewById(R.id.imageViewBackRegister);

        from_home = getIntent().getBooleanExtra("from_home",false);

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
                            if(!from_home) {
                                onBackPressed();
                                finish();
                            }
                            else {
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                finish();
                            }
                            break;

                        case R.id.imageViewBackRegister:
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
        ivClose.setOnClickListener(onButtonClickListener);
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
        if(!Pattern.matches("[a-zA-Z]+", phoneNumber) && phoneNumber.length() <9)
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


    public static String generateOtp(){
        double randomOtp;
        randomOtp = Math.floor(100000 + Math.random() * 900000);
        int x = (int) randomOtp;
        String genOtp = String.valueOf(x);
        return genOtp;
    }





    private void verifyUser(String message,String userPhone,String verifyOtp){
        try {
            SqlHelper sqlHelper = new SqlHelper(RegisterActivity.this, RegisterActivity.this);
            sqlHelper.setExecutePath("test.php");
//            sqlHelper.setActionString("login");
//            String verOtp = String.valueOf(verifyOtp);
            ContentValues contentValues = new ContentValues();
            contentValues.put("message",message);
            contentValues.put("number",userPhone);
            contentValues.put("OTP",verifyOtp);
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
            String response_id = "";
            String response_number="";
            String response_isActive="";
            String response = "";
            try {
                response_id = jsonObject.getString("userId");
                response_isActive = jsonObject.getString("isActive");
                response_number = jsonObject.getString("number");
            }
            catch (Exception e){
                response = jsonObject.getString("message");
            }

            //user not in database
            if (!response.equals("")) {
                SharedPreferences sharedPreferences = this.getSharedPreferences("hmw", 0);
                sharedPreferences.edit().putString("otp", String.valueOf(verifyOtp)).commit();
                sharedPreferences.edit().putBoolean("change_pass",false).commit();
                Intent intent = new Intent(RegisterActivity.this, verifyOtpActivity.class);
                startActivity(intent);
                finish();


            }

            //user verified by otp, go to premium sign up
            else if(!response_number.equals("") && response_isActive.equals("0")){
                Intent intent = new Intent(this,MainActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("return_path", "premium_signup");
                bundle.putInt("arg_count",3);
                bundle.putString("param_0", "");
                bundle.putString("param_1", response_id);
                bundle.putString("param_2", "");
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
            else if (response_isActive.equals("1")) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    dialog.dismiss();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialog.setTitle(getString(R.string.existing_user));
                    alertDialog.setMessage(getString(R.string.existing_user_prompt));
                    alertDialog.setPositiveButton(R.string.confirmation_yes, dialogClickListener);
                    alertDialog.setNegativeButton(R.string.confirmation_no, dialogClickListener);
                    alertDialog.show();
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


