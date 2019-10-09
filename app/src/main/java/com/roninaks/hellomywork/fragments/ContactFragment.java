package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    //Private Members
    private View rootView;
    private Context context;
    private EditText etName, etPhone, etEmail, etMessage;
    private Button btnSubmit;
    private ImageView ivBack;

    private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */

    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
        context = getActivity();
        etName = (EditText) rootView.findViewById(R.id.editText_Name);
        etEmail = (EditText) rootView.findViewById(R.id.editText_Email);
        etPhone = (EditText) rootView.findViewById(R.id.editText_Phone);
        etMessage = (EditText) rootView.findViewById(R.id.editText_Message);
        btnSubmit = (Button) rootView.findViewById(R.id.btn_Submit);
        ivBack = (ImageView) rootView.findViewById(R.id.imgBack);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()){
                    submitEnquiry();
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).onBackPressed();
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
        try {
            switch (sqlHelper.getActionString().toLowerCase()) {
                case "submit": {
                    JSONObject jsonObject = new JSONObject(sqlHelper.getStringResponse());
                    String response = jsonObject.getJSONObject("data").getString("success");
                    if(response.toLowerCase().equals(context.getString(R.string.response_success))){
                        Toast.makeText(context, context.getString(R.string.contact_success), Toast.LENGTH_SHORT).show();
                        clearForm();
                    }else{
                        Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    //Private Members

    private void clearForm(){
        etPhone.setText("");
        etMessage.setText("");
        etEmail.setText("");
        etName.setText("");
    }

    private boolean validate(){
        boolean cancel = false;
        String name = etName.getText().toString();
        String email = etEmail.getText().toString();
        String message = etMessage.getText().toString();
        if(name.isEmpty()){
            etName.setError(context.getString(R.string.required_field));
            cancel = true;
        }
        if(email.isEmpty()){
            etEmail.setError(context.getString(R.string.required_field));
        }
        if(message.isEmpty()){
            etMessage.setError(context.getString(R.string.required_field));
            cancel = true;
        }
        return cancel;
    }

    private void submitEnquiry(){
        SqlHelper sqlHelper = new SqlHelper(context, ContactFragment.this);
        sqlHelper.setExecutePath("enquiry.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("submit");
        ContentValues params = new ContentValues();
        params.put("userName", etName.getText().toString());
        params.put("email", etEmail.getText().toString());
        params.put("notes", etMessage.getText().toString());
        params.put("phone", etPhone.getText().toString());
        params.put("mob", "1");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }
}
