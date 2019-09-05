package com.roninaks.hellomywork.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

import static com.roninaks.hellomywork.activities.RegisterActivity.generateOtp;

public class LoginActivity extends AppCompatActivity implements SqlDelegate {

    //This is a test commit
    EditText editTextPhoneNumber;
    EditText editTextPassword;
    Button btnLogIn;
    TextView tvSignUp, tvForgotPassword,textViewError;
    Activity activity;
    String userPhone,userPassword,phoneNumber;
    ImageView imgViewBack;
    //SharedPreferences sharedPreferences;
    boolean pass_verified;
    int errorCount = 0;
    public final int ERROR_THRESHOLD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;

        editTextPhoneNumber = (EditText) findViewById(R.id.editText_Phonenumber);
        editTextPassword = (EditText) findViewById(R.id.editText_Password);
        btnLogIn = (Button) findViewById(R.id.button_LogIn);
        tvSignUp = (TextView) findViewById(R.id.textView_SignUp);
        tvForgotPassword = (TextView) findViewById(R.id.textView_ForgotPassword);
        imgViewBack = (ImageView) findViewById(R.id.imageViewBackLogin);
        editTextPhoneNumber.setText("7907961841");
        editTextPassword.setText("brooklyn");
        textViewError = (TextView) findViewById(R.id.textViewIncorrect);



        btnLogIn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                try {
                if(btnLogIn.getText().equals(getString(R.string.login_button))){
                    if (validateLogIn()) {
                        Toasty.normal(LoginActivity.this, "Success").show();
                        userPhone = editTextPhoneNumber.getText().toString();
                        userPassword = editTextPassword.getText().toString();

                        JSONObject json = new JSONObject();
                        json.put("userPhone", userPhone);
                        json.put("userPassword", userPassword);

                        attemptLogin(json);
                        Toasty.normal(LoginActivity.this, "Send data").show();
                    }
                    }
                else if(btnLogIn.getText().equals(getString(R.string.send_otp))){
                    userPhone = editTextPhoneNumber.getText().toString();
                   try{
                    if (userPhone.length() > 9) {
                        String genOtp =generateOtp();
                        checkNumber(genOtp, "OTP for verification is ", userPhone);
                        SharedPreferences sharedPreferences  = LoginActivity.this.getSharedPreferences("hwm",0);
                        sharedPreferences.edit().putString("otp",genOtp).commit();
                        Intent intent = new Intent(LoginActivity.this, verifyOtpActivity.class);
                        intent.putExtra("change_pass", true);
                        startActivity(intent);
                    }
                    else {Toasty.error(LoginActivity.this, "Entered number not valid", Toast.LENGTH_SHORT, false).show();}
//                    SharedPreferences sharedPreferences  = LoginActivity.this.getSharedPreferences("hwm",0);
//                    pass_verified = sharedPreferences.getBoolean("otp_verified",false);

                   }catch (Exception e){
                       Toasty.error(LoginActivity.this, "error", Toast.LENGTH_SHORT, false).show();
                   }
                }

                else if(btnLogIn.getText().equals(getString(R.string.save_password))){
                    String new_password = editTextPhoneNumber.getText().toString();
                    String reenter_password = editTextPassword.getText().toString();
                    if(new_password.equals("")){editTextPassword.setError(getString(R.string.required_field));}
                    else if(reenter_password.equals("")){editTextPhoneNumber.setError(getString(R.string.required_field));}
                    else if(new_password.equals(reenter_password)){
                        if(isValidPassword(editTextPassword.getText().toString())) {
                            changePassword(phoneNumber, new_password);

                            startActivity(new Intent(LoginActivity.this,LoginActivity.class));
                            finish();
                        }
                        else{Toasty.error(LoginActivity.this, R.string.password_length_err, Toast.LENGTH_SHORT, false).show();}
                    }
                    else {
                        Toasty.error(LoginActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                        editTextPhoneNumber.setText("");
                        editTextPassword.setText("");
                    }
                }
                }catch (Exception e){
                    //Something went wrong
                }
            }
        });


        View.OnClickListener onButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    switch (view.getId()) {
                        case R.id.textView_SignUp:
                            //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                            Toasty.normal(LoginActivity.this, "Hi").show();
                            Intent myIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                            startActivity(myIntent);
                            break;

                        case R.id.textView_ForgotPassword:
                            //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                            Toasty.normal(LoginActivity.this, "forgot").show();
                            editTextPassword.setVisibility(View.INVISIBLE);
                            tvForgotPassword.setVisibility(View.INVISIBLE);
                            btnLogIn.setText(getString(R.string.send_otp));
                            break;

                        case R.id.imageViewBackLogin:
                            onBackPressed();
                            finish();
                            break;
                    }
                }
                catch (Exception e){
//                        EmailHelper emailHelper = new EmailHelper(LoginActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
//                        emailHelper.sendEmail();
                    }
                }
            };

        tvForgotPassword.setOnClickListener(onButtonClickListener);
        tvSignUp.setOnClickListener(onButtonClickListener);
        imgViewBack.setOnClickListener(onButtonClickListener);
        }


    @Override
    public void onResume()
    {

        SharedPreferences sharedPreferences  = LoginActivity.this.getSharedPreferences("hmw",0);
        boolean back_pressed = sharedPreferences.getBoolean("back_pressed",false);
        boolean otp_verified = sharedPreferences.getBoolean("otp_verified",false);
        if(back_pressed && otp_verified) {
            setPasswordInput();
            sharedPreferences.edit().putBoolean("back_pressed",false).commit();
            sharedPreferences.edit().putBoolean("otp_verified",false).commit();
        }
        super.onResume();
    }




    private boolean validateLogIn() {
        String phoneNumber, password;
        boolean logIn = true;
        phoneNumber = editTextPhoneNumber.getText().toString();
        password = editTextPassword.getText().toString();
        if(phoneNumber.isEmpty()){
            editTextPhoneNumber.setError(getString(R.string.required_field));
            logIn = false;
        }
        if(password.isEmpty()){
            editTextPassword.setError(getString(R.string.required_field));
            logIn = false;

        }
        else if(!phoneNumber.isEmpty() || !password.isEmpty()){
            if(!isValidPhoneNumber(phoneNumber)){
                editTextPassword.setText("");
                editTextPhoneNumber.setText("");
                Toasty.error(LoginActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                logIn = false;
            }
            if(!isValidPassword(password)){
                editTextPassword.setText("");
                editTextPhoneNumber.setText("");
                Toasty.error(LoginActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
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



    public static boolean isValidPassword(String password){
        int length = password.length();
        if(length < 6)
            return false;
        return true;
    }



    private void attemptLogin(JSONObject json){
        try {
            SqlHelper sqlHelper = new SqlHelper(LoginActivity.this, LoginActivity.this);
            sqlHelper.setExecutePath("login.php");
            sqlHelper.setActionString("login");
            ContentValues contentValues = new ContentValues();
            contentValues.put("jsonObj",json.toString());
            //contentValues.put("userPassword", password);
            sqlHelper.setParams(contentValues);
            sqlHelper.setMethod(getString(R.string.method_post));
            sqlHelper.executeUrl(true);
        }catch (Exception e){
            EmailHelper emailHelper = new EmailHelper(LoginActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }
    }

    private void changePassword(String phoneNumber,String password){
        try {
            SqlHelper sqlHelper = new SqlHelper(LoginActivity.this, LoginActivity.this);
            sqlHelper.setExecutePath("changePword.php");
            sqlHelper.setActionString("password");
            ContentValues contentValues = new ContentValues();
            contentValues.put("number",phoneNumber);
            contentValues.put("password",password);
            sqlHelper.setParams(contentValues);
            sqlHelper.setMethod(getString(R.string.method_post));
            sqlHelper.executeUrl(true);
        }catch (Exception e){
            EmailHelper emailHelper = new EmailHelper(LoginActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }
    }

    private void checkNumber(String verifyOtp,String message,String userPhone){
        try {
            SqlHelper sqlHelper = new SqlHelper(LoginActivity.this, LoginActivity.this);
            sqlHelper.setExecutePath("test.php");
            sqlHelper.setActionString("otp");
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
            EmailHelper emailHelper = new EmailHelper(LoginActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
        }
    }



    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try {
            switch (sqlHelper.getActionString()){
                case "login": {
                    JSONObject jsonObject = sqlHelper.getJSONResponse();
                    String response = "";
                    try {
                        response = jsonObject.getString("userName");
                    } catch (Exception e) {
                        response = "unsuccessful";
                    }
                    if (!(response.equals("unsuccessful"))) {
                        SharedPreferences sharedPreferences = this.getSharedPreferences("hmw", 0);
                        Toasty.normal(LoginActivity.this, "Login success").show();
                        sharedPreferences.edit().putString("user_id", response).commit();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else if (response.equals("unsuccessful")) {
                        editTextPassword.setText("");
                        editTextPhoneNumber.setText("");
                        textViewError.setVisibility(View.VISIBLE);
                        if (errorCount % ERROR_THRESHOLD == 0) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                            alertDialog.setTitle(getString(R.string.new_user));
                            alertDialog.setMessage(getString(R.string.new_user_prompt));
                            alertDialog.setPositiveButton(R.string.confirmation_yes, dialogClickListener);
                            alertDialog.setNegativeButton(R.string.confirmation_no, dialogClickListener);
                            alertDialog.show();
                            errorCount++;
                        } else {
                            errorCount++;
                            Toasty.error(LoginActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
                        }
                    } else if (response.equals(getString(R.string.exception))) {
                        Toast.makeText(LoginActivity.this, getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case "otp": {
                    JSONObject jsonObject = sqlHelper.getJSONResponse();
                    String response;
                    try {
                        response = jsonObject.getString("number");
                    } catch (Exception e) {
                        response = "null";
                    }

                    if (response.equals("null")) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                        alertDialog.setTitle("Register");
                        alertDialog.setMessage("Number does not exist in database.Do you want to register?");
                        alertDialog.setPositiveButton(R.string.confirmation_yes, dialogClickListener);
                        alertDialog.setNegativeButton(R.string.confirmation_no, dialogClickListener);
                        alertDialog.show();
                    }

                }break;

                case "password":{
                    String response = sqlHelper.getStringResponse();
                    if(response.equals("1")){
                        Toast.makeText(LoginActivity.this, "Password changed successfully.", Toast.LENGTH_SHORT).show();
                        setLogInInput();
                    }
                }break;
            }
        } catch (Exception e) {
            EmailHelper emailHelper = new EmailHelper(LoginActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
            //To resolve
            Toast.makeText(LoginActivity.this, getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
        }

    }

    private void setPasswordInput(){
        phoneNumber = editTextPhoneNumber.getText().toString();
        editTextPhoneNumber.setText("");
        editTextPhoneNumber.setHint("Enter new passsword");
        editTextPhoneNumber.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPassword.setVisibility(View.VISIBLE);
        editTextPassword.setHint("Re-enter password");
        editTextPassword.setText("");
        btnLogIn.setText(getString(R.string.save_password));

    }

    private void setLogInInput(){
        editTextPhoneNumber.setHint("Phone No");
        editTextPhoneNumber.setText("");
        editTextPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
        editTextPassword.setHint("Password");
        editTextPassword.setText("");
        tvForgotPassword.setVisibility(View.VISIBLE);
        btnLogIn.setText(getString(R.string.login_button));
    }

}



