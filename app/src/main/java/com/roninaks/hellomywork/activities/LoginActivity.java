package com.roninaks.hellomywork.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.roninaks.hellomywork.R;

import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.helpers.EmailHelper;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.PermissionsHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;

import org.json.JSONObject;

import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements SqlDelegate {

    //This is a test commit
    EditText editTextPhoneNumber;
    EditText editTextPassword;
    Button btnLogIn;
    TextView tvSignUp, tvForgotPassword;
    Activity activity;
    String userPhone,userPassword;
    ImageView imgViewBack;

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

        View.OnClickListener onButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    switch (view.getId()) {
                        case R.id.button_LogIn:
                            if(validateLogIn()) {
                                Toasty.normal(LoginActivity.this, "Success").show();
                                userPhone = editTextPhoneNumber.getText().toString();
                                userPassword = editTextPassword.getText().toString();

                                JSONObject json = new JSONObject();
                                json.put("userPhone", userPhone);
                                json.put("userPassword", userPassword);

                                attemptLogin(json);
                                Toasty.normal(LoginActivity.this, "Send data").show();
                            }
                            break;

                        case R.id.textView_SignUp:
                            //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                            //Toasty.normal(LoginActivity.this, "Hi").show();
                            Intent myIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                            startActivity(myIntent);
                            break;

                        case R.id.textView_ForgotPassword:
                            //startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                            Toasty.normal(LoginActivity.this, "forgot").show();
                            editTextPassword.setVisibility(View.INVISIBLE);
                            tvForgotPassword.setVisibility(View.INVISIBLE);
                            btnLogIn.setText("Send OTP");
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
        btnLogIn.setOnClickListener(onButtonClickListener);
        tvForgotPassword.setOnClickListener(onButtonClickListener);
        tvSignUp.setOnClickListener(onButtonClickListener);
        imgViewBack.setOnClickListener(onButtonClickListener);
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


    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try {
            JSONObject jsonObject = sqlHelper.getJSONResponse();
            String r = sqlHelper.getStringResponse();
            String response = jsonObject.getString("userName");
            if (!(r == "0")) {
                SharedPreferences sharedPreferences = this.getSharedPreferences("HelloMyWork", 0);
                Toasty.normal(LoginActivity.this, "Login success").show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else if (jsonObject.isNull("0")) {
                editTextPassword.setText("");
                editTextPhoneNumber.setText("");
                Toasty.error(LoginActivity.this, R.string.invalid_cred, Toast.LENGTH_SHORT, false).show();
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
        } catch (Exception e) {
            EmailHelper emailHelper = new EmailHelper(LoginActivity.this, EmailHelper.TECH_SUPPORT, "Error: LoginActivity", e.getMessage() + "\n" + StringHelper.convertStackTrace(e));
            emailHelper.sendEmail();
            //To resolve
            Toast.makeText(LoginActivity.this, getString(R.string.invalid_cred), Toast.LENGTH_SHORT).show();
        }

    }

}



