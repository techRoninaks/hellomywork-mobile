package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.AdminActivity;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdminLogin.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdminLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminLogin extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    //Private Members
    private EditText etEmail, etPassword;
    private Button btnSubmit;
    private View rootView;
    private Context context;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences sharedPreferences;

    public AdminLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminLogin.
     */
    public static AdminLogin newInstance(String param1, String param2) {
        AdminLogin fragment = new AdminLogin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_admin_login, container, false);
        context = getActivity();
        sharedPreferences = context.getSharedPreferences("hmw", 0);
        if(sharedPreferences.getBoolean("is_loggedin_admin", false)){
            Fragment fragment = DashboardFragment.newInstance(sharedPreferences.getString("emp_id", ""), sharedPreferences.getString("emp_name", ""));
            ((AdminActivity) context).initFragment(fragment, "Dashboard");
        }
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    attemptSignup();
                }
            }
        });
        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try{
            switch (sqlHelper.getActionString()){
                case "login":{
                    if(sqlHelper.getStringResponse().equals("0")){
                        Toast.makeText(context, context.getString(R.string.invalid_cred), Toast.LENGTH_SHORT).show();
                        clearFields();
                    }else{
                        JSONObject jsonObject = new JSONObject(sqlHelper.getStringResponse());
                        sharedPreferences.edit().putBoolean("is_loggedin_admin", true).commit();
                        sharedPreferences.edit().putString("emp_id", jsonObject.getString("userId")).commit();
                        sharedPreferences.edit().putString("emp_name", jsonObject.getString("userName")).commit();
                        Fragment fragment = DashboardFragment.newInstance(jsonObject.getString("userId"), jsonObject.getString("userName"));
                        ((AdminActivity) context).initFragment(fragment, "Dashboard");
                    }
                    break;
                }
            }
        }catch (Exception e){
            Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    //Private Functions

    private boolean validate(){
        boolean valid = true;
        String password = etPassword.getText().toString();
        String email = etEmail.getText().toString();
        if(email.isEmpty()){
            etEmail.setError(context.getString(R.string.required_field));
            valid = false;
        }else if(!email.contains("@")){
            etEmail.setError(context.getString(R.string.invalid_email));
            valid = false;
        }
        if(password.isEmpty()){
            etPassword.setError(context.getString(R.string.required_field));
            valid = false;
        }
        return valid;
    }

    private void clearFields(){
        etPassword.setText("");
        etEmail.setText("");
    }

    private void attemptSignup(){
        SqlHelper sqlHelper = new SqlHelper(context, AdminLogin.this);
        sqlHelper.setMasterUrl(context.getString(R.string.master_url));
        sqlHelper.setExecutePath("admin/login/assets/php/login.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("login");
        ContentValues params = new ContentValues();
        params.put("userEmail", etEmail.getText().toString());
        params.put("userPassword", etPassword.getText().toString());
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }
}
