package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlansFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlansFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlansFragment extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "userId";
    private static final String ARG_PARAM2 = "param2";


    private String userId;
    private String mParam2;
    private String planType;

    //Private Members
    private LinearLayout llContainerPlanFree, llContainerPlanUser, llContainerPlanCompany;
    private Button btnFree, btnUser, btnCompany;
    private Context context;
    private View rootView;

    private OnFragmentInteractionListener mListener;

    public PlansFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 User Id.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlansFragment.
     */

    public static PlansFragment newInstance(String param1, String param2) {
        PlansFragment fragment = new PlansFragment();
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
            userId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_plans, container, false);
        context = getActivity();
        Checkout.preload(getActivity());
        llContainerPlanFree = (LinearLayout) rootView.findViewById(R.id.containerPlanFree);
        llContainerPlanUser = (LinearLayout) rootView.findViewById(R.id.containerPlanUser);
        llContainerPlanCompany = (LinearLayout) rootView.findViewById(R.id.containerPlanCompany);
        btnFree = (Button) rootView.findViewById(R.id.btn_Free);
        btnUser = (Button) rootView.findViewById(R.id.btn_User);
        btnCompany = (Button) rootView.findViewById(R.id.btn_Company);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_Free:
                    case R.id.containerPlanFree:{
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        planType = "free";
                                        saveUserPlan();
                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:
                                        break;
                                    default:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Are you sure?");
                        builder.setMessage("Free plan has only limited access to features.").setPositiveButton("Continue", dialogClickListener)
                                .setNegativeButton("Choose Another Plan", dialogClickListener).show();
                        break;
                    }
                    case R.id.btn_Company:
                    case R.id.containerPlanCompany:{
                        planType = "company";
                        createOrder();
                        break;
                    }
                    case R.id.btn_User:
                    case R.id.containerPlanUser:{
                        planType = "user";
                        createOrder();
                        break;
                    }
                }
            }
        };

        llContainerPlanCompany.setOnClickListener(onClickListener);
        llContainerPlanFree.setOnClickListener(onClickListener);
        llContainerPlanUser.setOnClickListener(onClickListener);
        btnCompany.setOnClickListener(onClickListener);
        btnUser.setOnClickListener(onClickListener);
        btnFree.setOnClickListener(onClickListener);
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
                case "create-order":{
                    makePayment(new JSONObject(sqlHelper.getStringResponse()));
                    break;
                }
                case "save-package":{
                    SharedPreferences sharedPreferences = context.getSharedPreferences("hmw", 0);
                    sharedPreferences.edit().putString("user_id", userId)
                            .commit();
                    sharedPreferences.edit().putBoolean("is_loggedin",true).commit();
                    Fragment fragment = ProfileFragment.newInstance(userId,"");
                    ((MainActivity) context).initFragment(fragment);
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

    public void saveUserPlan(){
        SqlHelper sqlHelper = new SqlHelper(context, PlansFragment.this);
        sqlHelper.setExecutePath("postpackageprofile.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("save-package");
        ContentValues params = new ContentValues();
        params.put("id", userId);
        params.put("emid", ((MainActivity) context).isAdminLoggedIn());
        params.put("month", 0);
        params.put("callAction", planType.equals("user") ? "personel" : "company");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    //Private Functions

    private void makePayment(JSONObject jsonObject){
        try {
            Checkout checkout = new Checkout();
            checkout.setImage(R.drawable.logo_white_min);
            JSONObject options = new JSONObject();
            options.put("name", "HELLOmywork");
            options.put("description", "Reference No. #123456");
            options.put("order_id", jsonObject.getString("id"));
            options.put("currency", "INR");
            options.put("amount", planType.equals("user") ? "9900" : "19900");
            checkout.open((MainActivity) context, options);
        } catch (Exception e) {
            Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
        }
    }

    private void createOrder(){
        SqlHelper sqlHelper = new SqlHelper(context, PlansFragment.this);
        sqlHelper.setExecutePath("createorder.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("create-order");
        ContentValues params = new ContentValues();
        params.put("amount", planType.equals("user") ? "9900" : "19900");
        params.put("user_id", userId);
        HashMap<String, String> extras = new HashMap<>();
        extras.put("plan_type", planType);
        sqlHelper.setExtras(extras);
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

}
