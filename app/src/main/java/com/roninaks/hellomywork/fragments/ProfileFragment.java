package com.roninaks.hellomywork.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.ProfileImage;
import com.roninaks.hellomywork.activities.RegisterActivity;
import com.roninaks.hellomywork.adapters.ActivityFeedAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.ProfilePostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements SqlDelegate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String us_id;
    View rootView;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 4;
    String imageBaseUri = "https://www.hellomywork.com/",telphone, sentToMail, whatsppNumber, profileName;
    Context context;
    ImageView masterProfilePster;
    TextView profilePCName, profileUnion, profileJTRole, profileWebsite, profileLocation, profileCNumber, profileWhatsappNumber, profileEmail, profileSublocation, profileAddress, profileSkills;
    ImageButton profileCallPhoneBTN, profileSentEmailBTN, profileUseWhatspp;
    private RequestOptions requestOptions;
    RecyclerView recyclerView;
    ArrayList <ProfilePostModel> profilePostModels;
    ActivityFeedAdapter activityFeedAdapter;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        context = getContext();
        if (mParam1 == null){
            us_id = "0007";
        }
        else {
            us_id = mParam1;
        }
        fetchProfileCardInfo(context, us_id);
        detchProfilePostInfo(context, "fetch_id");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        masterProfilePster = view.findViewById(R.id.profileMasterCardIV);
        profilePCName = view.findViewById(R.id.profileCard_textV_Name);
        profileUnion = view.findViewById(R.id.profileCard_textV_Union);
        profileJTRole = view.findViewById(R.id.profileCard_textV_Role);
        profileWebsite = view.findViewById(R.id.profileCard_textV_Website);
        profileLocation = view.findViewById(R.id.profileCard_textV_Location);
        profileCNumber = view.findViewById(R.id.profileCard_textV_Contact);
        profileWhatsappNumber = view.findViewById(R.id.profileCard_textV_Whatsapp);
        profileEmail = view.findViewById(R.id.profileCard_textV_Email);
        profileSublocation = view.findViewById(R.id.profileCard_textV_SubLocation);
        profileAddress = view.findViewById(R.id.profileCard_textV_Address);
        profileSkills = view.findViewById(R.id.profileCard_textV_Skills);
        profileCallPhoneBTN = view.findViewById(R.id.prodile_card_phoneBTN);
        profileSentEmailBTN = view.findViewById(R.id.prodile_card_mailBTN);
        profileUseWhatspp = view.findViewById(R.id.prodile_card_whatsBTN);
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon_image);
        requestOptions.error(R.drawable.icon_image);

        profilePostModels = new ArrayList<>();

        recyclerView = view.findViewById(R.id.profileRecyclerView);


        profileCallPhoneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((MainActivity) context).isLoggedIn().isEmpty()){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent myIntent = new Intent(context,LoginActivity.class);
                                    startActivity(myIntent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Oho!, You are not Logged In");
                    builder.setMessage("You need to login to make calls").setPositiveButton("Go to login?", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    if(telphone.equals("No Number")){
                        Toast.makeText(context, "No number saved by" + profileName, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ((MainActivity) context).callPhone("+91" + whatsppNumber);
                    }
                }
            }
        });

        profileUseWhatspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((MainActivity) context).isLoggedIn().isEmpty()){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent myIntent = new Intent(context,LoginActivity.class);
                                    startActivity(myIntent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Oho!, You are not Logged In");
                    builder.setMessage("You need to login to make calls").setPositiveButton("Go to login?", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    if(whatsppNumber.equals("No Number")){
                        Toast.makeText(context, "No number saved by" + profileName, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ((MainActivity) context).sendWhatsapp("+91" + whatsppNumber);
                    }
                }
            }
        });

        profileSentEmailBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((MainActivity) context).isLoggedIn().isEmpty()){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    Intent myIntent = new Intent(context,LoginActivity.class);
                                    startActivity(myIntent);
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Oho!, You are not Logged In");
                    builder.setMessage("You need to login to make calls").setPositiveButton("Go to login?", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else {
                    if(sentToMail.equals("No Email")){
                        Toast.makeText(context, "No email saved by" + profileName, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ((MainActivity) context).sendMail(sentToMail);
                    }
                }
            }
        });
        rootView = view;
        return view;
    }

    private void detchProfilePostInfo(Context context, String fetch_id) {
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("getallpost.php");
        sqlHelper.setActionString("profilePosts");
        sqlHelper.setMethod("GET");
        sqlHelper.setParams(new ContentValues());
        sqlHelper.executeUrl(false);
    }

    private void fetchProfileCardInfo(Context context, String us_id) {
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("getprofilecard.php");
        sqlHelper.setActionString("profileCard");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", us_id);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        String response = sqlHelper.getStringResponse();
        if(sqlHelper.getActionString() == "profileCard"){
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(1);
                populateProfileCardInfo(jsonObject);
            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if(sqlHelper.getActionString() =="profilePosts"){
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(1);
                int length = Integer.parseInt(jsonArray.getJSONObject(0).getString("response"));
                inntRecyclerView(jsonArray, length);
            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    private void inntRecyclerView(JSONArray jsonArray, int length) {
        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 1; i <= length; i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProfilePostModel profilePostModel = modelHelper.buildProfilePostModel(jsonObject);
                profilePostModels.add(profilePostModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModels,rootView);
        recyclerView.setAdapter(activityFeedAdapter);
    }

    private void populateProfileCardInfo(JSONObject jsonObject) {
        try {
            String address = jsonObject.getString("address");
            address = address.replace("&#32;", " ");
            profilePCName.setText(jsonObject.getString("name"));
            profileName = jsonObject.getString("name");
            profileUnion.setText(jsonObject.getString("union"));
            profileJTRole.setText(jsonObject.getString("role"));
            profileWebsite.setText(jsonObject.getString("website"));
            profileLocation.setText(jsonObject.getString("location"));
            profileCNumber.setText(jsonObject.getString("phone")+", "+jsonObject.getString("phone2"));
            profileWhatsappNumber.setText(jsonObject.getString("whatapp"));
            profileEmail.setText(jsonObject.getString("email"));
            profileSublocation.setText(jsonObject.getString("sublocation"));
            profileAddress.setText(address+", "+jsonObject.getString("pincode"));
            profileSkills.setText(jsonObject.getString("skills"));
            try {
                telphone = jsonObject.getString("phone").split(",")[0];
            }
            catch (Exception e){
                telphone = jsonObject.getString("phone");
            }
            try {
                whatsppNumber = jsonObject.getString("whatapp").split(",")[0];
            }
            catch (Exception e){
                telphone = jsonObject.getString("whatapp");
            }
            sentToMail = jsonObject.getString("email");

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions
                            .placeholder(R.drawable.icon_image)
                            .error(R.drawable.icon_image)
                            .fitCenter()
                    )
                    .asBitmap()
                    .load(imageBaseUri+jsonObject.getString("card"))
                    .into(masterProfilePster);


        } catch (JSONException e) {
            e.printStackTrace();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
       String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the phone call

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
