package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.ProfileImage;
import com.roninaks.hellomywork.adapters.ActivityFeedAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CommentsModel;
import com.roninaks.hellomywork.models.ProfilePostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import es.dmoral.toasty.Toasty;

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

    public static String imageUrl = "";
    public static boolean imageChanged = false;
    public static Bitmap bitmap;

    // TODO: Rename and change types of parameters
    private String userId;
    private String mParam2;
    private String us_id;
    View rootView;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 4;
    String imageBaseUri = "https://www.hellomywork.com/",telphone, sentToMail, whatsppNumber, profileName,profileCard;
    Context context;
    ImageView masterProfilePster, ivProfileBackBtn, ivSettings;
    EditText postadDescrption,writeComments;
    private TextView profilePCName,profileMainName, profileUnion, profileJTRole, profileWebsite, profileLocation, profileCNumber, profileWhatsappNumber, profileEmail, profileSublocation, profileAddress, profileSkills;
    ImageButton profileCallPhoneBTN, profileSentEmailBTN, profileUseWhatspp,profileShare,profileBookmark;
    Button buttonEdit,buttonImageUpload,buttonForsale,buttonOffers,buttonRequired,buttonAppreciations,buttonAchievemnet,buttonRandom,buttonPost;
    private RequestOptions requestOptions;
    private LinearLayout llpostMaster;
    RecyclerView recyclerView;
    ArrayList <ProfilePostModel> profilePostModels;
    ActivityFeedAdapter activityFeedAdapter;
    String tagButton, isBookMarked;
    boolean filled;


    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 User ID.
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
            userId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getContext();
        if (userId == ""){
            us_id = ((MainActivity) context).isLoggedIn();
//            us_id = "8664";
        }
        else {
            us_id = userId;
        }
        fetchProfileCardInfo(context, us_id);
        fetchProfilePostInfo(context, "fetch_id");
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        JSONObject jsonObject;

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
        profileShare=view.findViewById(R.id.profile_card_shareBTN);
        profileBookmark=view.findViewById(R.id.profile_card_bookmarkBTN);
        postadDescrption=view.findViewById(R.id.Post_ad_Description);
        buttonAchievemnet=view.findViewById(R.id.Achievement_BTN);
        buttonAppreciations=view.findViewById(R.id.Appreciation_BTN);
        buttonOffers=view.findViewById(R.id.Offers_BTN);
        buttonForsale=view.findViewById(R.id.ForSale_BTN);
        buttonRequired=view.findViewById(R.id.Required_BTN);
        buttonRandom=view.findViewById(R.id.Random_BTN);
        buttonPost=view.findViewById(R.id.button_Post);
        ivSettings=view.findViewById(R.id.profile_settings);
        writeComments=view.findViewById(R.id.editText_WriteComments);
        profileMainName = view.findViewById(R.id.profileMainName);
        llpostMaster = view.findViewById(R.id.llpostMaster);
        requestOptions = new RequestOptions();

        buttonEdit=view.findViewById(R.id.button_Edit);
        buttonImageUpload=view.findViewById(R.id.button_ImageUpload);
        ivProfileBackBtn=view.findViewById(R.id.imgBack);
//        requestOptions.placeholder(R.drawable.icon_image);
//        requestOptions.error(R.drawable.icon_image);


        profilePostModels = new ArrayList<>();

        recyclerView = view.findViewById(R.id.profileRecyclerView);

        if(((MainActivity) context).isLoggedIn().isEmpty()){
            ivSettings.setVisibility(View.GONE);
        }
        if(userId != ((MainActivity) context).isLoggedIn()){
            llpostMaster.setVisibility(View.GONE);
        }

        buttonEdit.setVisibility(View.GONE);
        if(((MainActivity) context).isLoggedIn().equals(us_id)){
            buttonEdit.setVisibility(View.VISIBLE);
            buttonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = PremiumSignupFragment.newInstance("edit",userId, "");
                    ((MainActivity) context).initFragment(fragment);

                }
            });
        }



        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postadDescrption.equals("")){
                    Toast.makeText(context, "Descrpition is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveInformation();
                }

            }
        });

        ivProfileBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).onBackPressed();
            }
        });

        buttonImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image", imageUrl);
                bundle.putString("fragment", "profile");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        buttonForsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_ForSale-min.png";
            }
        });
        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Random-min.png";
            }
        });
        buttonRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Required-min.png";
            }
        });
        buttonAchievemnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Achievement-min.png";
            }
        });
        buttonAppreciations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Appreciations-min.png";
            }
        });
        buttonOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Offers-min.png";
            }
        });



//        ivComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                    if (writeComments.requestFocus()) {
////                    }
//            }
//        });








        profileCallPhoneBTN.setOnClickListener(new View.OnClickListener() {
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
                    if(telphone.equals("No Number")){
                        Toast.makeText(context, "No number saved by" + profileName, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ((MainActivity) context).callPhone("+91" + telphone);
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
                    builder.setMessage("You need to login to make mails").setPositiveButton("Go to login?", dialogClickListener)
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
        profileShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
//                shareIntent.setType("*/*");
                String shareBody = "Welcome to Hello My Work.\n\nInstall Hello My work.\n\n";
                String imgUri = imageBaseUri+profileCard;
                Uri imgpath = Uri.parse(imgUri);
                String shareSub = "Hello my work Invitation";
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imgpath);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                shareIntent.setType("image/png");
                startActivityForResult(Intent.createChooser(shareIntent, "Share using"), 0);

            }
        });
        profileBookmark.setOnClickListener(new View.OnClickListener() {
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
                    builder.setMessage("You need to login to make bookmark").setPositiveButton("Go to login?", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                else
                {

                    if (filled) {
                        profileBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkpost_min));
                        Toast.makeText(context, "Bookmark have been removed", Toast.LENGTH_SHORT).show();
                        filled = false;
                    } else {
                        profileBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkfill_idcard));
                        Toast.makeText(context, "Bookmark have been added", Toast.LENGTH_SHORT).show();
                        filled = true;
                    }

                    SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
                    sqlHelper.setExecutePath("updatebookmarks.php");
                    sqlHelper.setActionString("bookmark");
                    sqlHelper.setMethod("POST");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userId", ((MainActivity) context).isLoggedIn());
                    contentValues.put("type", "profiles");
                    contentValues.put("mapping_id", us_id);
                    contentValues.put("isActive", isBookMarked);
                    sqlHelper.setParams(contentValues);
                    sqlHelper.executeUrl(true);
                }
            }
        });

        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                Menu m = popup.getMenu();
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.settings_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.options_logout:{
                                ((MainActivity) context).logout();
                                break;
                            }

                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        rootView = view;
        return view;
    }

    private void fetchProfilePostInfo(Context context, String fetch_id) {
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("getprofilepost.php");
        sqlHelper.setActionString("profilePosts");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", us_id);
        contentValues.put("userid", ((MainActivity) context).isLoggedIn());
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(true);
    }

    private void fetchProfileCardInfo(Context context, String us_id) {
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("getprofilecard.php");
        sqlHelper.setActionString("profileCard");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", us_id);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(true);
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
//                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if(sqlHelper.getActionString() =="profilePosts"){
            try {
                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(1);
                int length = (jsonArray.getJSONObject(1).length());
                inntRecyclerView(jsonArray, length);
            } catch (JSONException e) {
//                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else  if (sqlHelper.getActionString() == "bookmark"){
            String responseFrom = sqlHelper.getStringResponse();
            if(responseFrom.equals("0")){
                Toast.makeText(context, "Booked removed", Toast.LENGTH_SHORT).show();
            }
            else if(responseFrom.equals("0")){
                Toast.makeText(context, "Booked saved", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void inntRecyclerView(JSONArray jsonArray, int length) {
        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 1; i <= length; i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProfilePostModel profilePostModel = modelHelper.buildProfilePostModel(jsonObject);
                profilePostModel.setCommentsModels(getCommentList(jsonObject));
                profilePostModels.add(profilePostModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModels,rootView, us_id);
        recyclerView.setAdapter(activityFeedAdapter);
    }

    private ArrayList<CommentsModel> getCommentList(JSONObject jsonObject) {
        JSONArray jsonArray;
        ArrayList<CommentsModel> commentsModels = new ArrayList<>();
        ModelHelper modelHelper = new ModelHelper(this.context);
        try {
            jsonArray = jsonObject.getJSONArray("comments");
            for (int i= 0; i< jsonArray.length(); i++) {
                CommentsModel commentsModel = new CommentsModel();
                commentsModel.setComment(jsonArray.getJSONObject(i).getString("comment"));
                commentsModel.setCommentName(jsonArray.getJSONObject(i).getString("name"));
                commentsModel.setCommentId(jsonArray.getJSONObject(i).getString("id"));
                commentsModel.setCommentU_Id(jsonArray.getJSONObject(i).getString("u_id"));
                commentsModel.setCommentP_Id(jsonArray.getJSONObject(i).getString("p_id"));
                commentsModel.setCommentIsReported(jsonArray.getJSONObject(i).getString("IsReported"));
                commentsModel.setCommentIsActive(jsonArray.getJSONObject(i).getString("IsActive"));
                commentsModels.add(commentsModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return commentsModels;
    }

    private void populateProfileCardInfo(JSONObject jsonObject) {
        try {
            String address = jsonObject.getString("address");
            address = address.replace("&#32;", " ");
            profilePCName.setText(jsonObject.getString("name"));
            profileMainName.setText(jsonObject.getString("name"));
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
            isBookMarked = jsonObject.getString("isActive");
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
                whatsppNumber = jsonObject.getString("whatapp");
            }
            sentToMail = jsonObject.getString("email");
            profileCard = jsonObject.getString("card");

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions
//                            .placeholder(R.drawable.icon_image)
//                            .error(R.drawable.icon_image)
                            .fitCenter()
                    )
                    .asBitmap()
                    .load(imageBaseUri+jsonObject.getString("card"))
                    .into(masterProfilePster);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveInformation(){
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("postpost.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("submit");
        ContentValues params = new ContentValues();
//        params.put("u_id", ((MainActivity) context).isLoggedIn());
        params.put("u_id", us_id);
        params.put("des", postadDescrption.getText().toString());
        params.put("tag", tagButton);
        params.put("image", imageChanged ? StringHelper.imageToString(bitmap) : "1");
        params.put("mob", "1");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
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
