package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.DialogFragment;
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
import com.roninaks.hellomywork.interfaces.OnLoadMoreListener;
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

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int SEARCH_RESULT_LIMIT = 24;

    public static String imageUrl = "";
    public static boolean imageChanged = false;
    public static Bitmap bitmap;


    private String userId;
    private String mParam2;
    private String us_id;
    View rootView;
    private final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 4;
    String imageBaseUri = "https://www.hellomywork.com/",telphone, sentToMail, whatsppNumber, profileName,profileCard;
    Context context;
    ImageView masterProfilePster, ivProfileBackBtn, ivSettings, ivRatings;
    EditText postadDescrption,writeComments;
    TextView sendComment;
    private TextView viewMoreComments,profilePCName,profileMainName, profileUnion, profileJTRole, profileWebsite, profileLocation, profileCNumber, profileWhatsappNumber, profileEmail, profileSublocation, profileAddress, profileSkills;
    ImageButton profileCallPhoneBTN, profileSentEmailBTN, profileUseWhatspp,profileShare,profileBookmark;
    Button buttonEdit,buttonImageUpload,buttonForsale,buttonOffers,buttonRequired,buttonAppreciations,buttonAchievemnet,buttonRandom,buttonPost,buttonForsale2,buttonOffers2,buttonRequired2,buttonAppreciations2,buttonAchievemnet2,buttonRandom2,buttonPost2;
    private RequestOptions requestOptions;
    private LinearLayout llpostMaster;
    RecyclerView recyclerView;
    ArrayList <ProfilePostModel> profilePostModels,profilePostModelsOffers,profilePostModelsForSale,profilePostModelsRequired,profilePostModelsAchivement,profilePostModelsAppreciation;
    ActivityFeedAdapter activityFeedAdapter;
    LinearLayout llBtnGrp;
    Handler handler;
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
     * @param userId User ID.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */

    public static ProfileFragment newInstance(String userId, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userId);
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
    public void onResume() {
        if(imageChanged){
            buttonImageUpload.setBackgroundResource(R.drawable.career_button_color_radius);
            buttonImageUpload.setTextColor(getResources().getColor(R.color.colorTextWhitePrimary));
            buttonPost.setBackgroundResource(R.drawable.card_background_shape);
            buttonPost.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
        }
        super.onResume();
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
        fetchProfilePostInfo(context, "fetch_id","1");
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
        ivRatings=view.findViewById(R.id.profile_ratings);
        writeComments=view.findViewById(R.id.editText_WriteComments);
        profileMainName = view.findViewById(R.id.profileMainName);
        llpostMaster = view.findViewById(R.id.llpostMaster);
        requestOptions = new RequestOptions();

        buttonAchievemnet2=view.findViewById(R.id.Achievement_BTN2);
        buttonAppreciations2=view.findViewById(R.id.Appreciation_BTN2);
        buttonOffers2=view.findViewById(R.id.Offers_BTN2);
        buttonForsale2=view.findViewById(R.id.ForSale_BTN2);
        buttonRequired2=view.findViewById(R.id.Required_BTN2);
        buttonRandom2=view.findViewById(R.id.Random_BTN2);
        viewMoreComments = view.findViewById(R.id.view_more_comments);

        buttonEdit=view.findViewById(R.id.button_Edit);
        buttonImageUpload=view.findViewById(R.id.button_ImageUpload);
        ivProfileBackBtn=view.findViewById(R.id.imgBack);
//        requestOptions.placeholder(R.drawable.icon_image);
//        requestOptions.error(R.drawable.icon_image);

        sendComment = view.findViewById(R.id.addComment);

        profilePostModels = new ArrayList<>();
        profilePostModelsOffers = new ArrayList<>();
        profilePostModelsForSale = new ArrayList<>();
        profilePostModelsAchivement= new ArrayList<>();
        profilePostModelsAppreciation = new ArrayList<>();
        profilePostModelsRequired = new ArrayList<>();

        handler = new Handler();

        recyclerView = view.findViewById(R.id.profileRecyclerView);

        if(((MainActivity) context).isLoggedIn().isEmpty()){
            ivSettings.setVisibility(View.GONE);
            ivRatings.setVisibility(View.GONE);
        }
        if(userId.equals(((MainActivity) context).isLoggedIn())){
            ivRatings.setVisibility(View.GONE);
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
        }else{
            llpostMaster.setVisibility(View.GONE);
            buttonEdit.setVisibility(View.GONE);
        }

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postadDescrption.equals("")){
                    Toast.makeText(context, "Descrpition is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveInformation();
                    refreshData();
                    fetchProfilePostInfo(context, "fetch_id","1");
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    recyclerView.setLayoutManager(layoutManager);
                    activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModels,rootView, us_id,recyclerView);
                    recyclerView.setAdapter(activityFeedAdapter);

                    if(tagButton.equals("assets/img/icon/ic_Required-min.png")){
                        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsRequired,rootView, us_id,recyclerView);
                        recyclerView.setAdapter(activityFeedAdapter);
                    }
                    if(tagButton.equals("assets/img/icon/ic_Achievement-min.png")){
                        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsAchivement,rootView, us_id,recyclerView);
                        recyclerView.setAdapter(activityFeedAdapter);
                    }
                    if(tagButton.equals("assets/img/icon/ic_Appreciations-min.png")){
                        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsAppreciation,rootView, us_id,recyclerView);
                        recyclerView.setAdapter(activityFeedAdapter);
                    }
                    if(tagButton.equals("assets/img/icon/ic_Offers-min.png")){
                        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsOffers,rootView, us_id,recyclerView);
                        recyclerView.setAdapter(activityFeedAdapter);
                    }
                    if(tagButton.equals("assets/img/icon/ic_ForSale-min.png")){
                        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsForSale,rootView, us_id,recyclerView);
                        recyclerView.setAdapter(activityFeedAdapter);
                    }
                    setDefaultButton(false);
                    postadDescrption.setText("");

                    //change button after post
                    buttonPost.setBackgroundResource(R.drawable.career_button_color_radius);
                    buttonPost.setTextColor(getResources().getColor(R.color.colorTextWhitePrimary));
                    buttonImageUpload.setBackgroundResource(R.drawable.card_background_shape);
                    buttonImageUpload.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
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

        // 1st scroll view


        buttonForsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultButton(true);
                tagButton = "assets/img/icon/ic_ForSale-min.png";
                buttonForsale.setAlpha(1);
                }
        });
        buttonRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultButton(true);
                tagButton = "assets/img/icon/ic_Random-min.png";
                buttonRandom.setAlpha(1);

            }
        });
        buttonRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultButton(true);
                tagButton = "assets/img/icon/ic_Required-min.png";
                buttonRequired.setAlpha(1);
            }
        });
        buttonAchievemnet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultButton(true);
                tagButton = "assets/img/icon/ic_Achievement-min.png";
                buttonAchievemnet.setAlpha(1);
            }
        });
        buttonAppreciations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultButton(true);
                tagButton = "assets/img/icon/ic_Appreciations-min.png";
                buttonAppreciations.setAlpha(1);
            }
        });
        buttonOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultButton(true);
                tagButton = "assets/img/icon/ic_Offers-min.png";
                buttonOffers.setAlpha(1);
            }
        });


    //2nd scroll view

        buttonForsale2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_ForSale-min.png";
                setDefaultButton(false);
                buttonForsale2.setAlpha(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsForSale,rootView, us_id,recyclerView);
                recyclerView.setAdapter(activityFeedAdapter);


            }
        });
        buttonRandom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Random-min.png";
                setDefaultButton(false);
                buttonRandom2.setAlpha(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModels,rootView, us_id,recyclerView);
                recyclerView.setAdapter(activityFeedAdapter);

            }
        });
        buttonRequired2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Required-min.png";
                setDefaultButton(false);
                buttonRequired2.setAlpha(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsRequired,rootView, us_id,recyclerView);
                recyclerView.setAdapter(activityFeedAdapter);
            }
        });
        buttonAchievemnet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Achievement-min.png";
                setDefaultButton(false);
                buttonAchievemnet2.setAlpha(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsAchivement,rootView, us_id,recyclerView);
                recyclerView.setAdapter(activityFeedAdapter);
            }
        });
        buttonAppreciations2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Appreciations-min.png";
                setDefaultButton(false);
                buttonAppreciations2.setAlpha(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsAppreciation,rootView, us_id,recyclerView);
                recyclerView.setAdapter(activityFeedAdapter);
            }
        });
        buttonOffers2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagButton = "assets/img/icon/ic_Offers-min.png";
                setDefaultButton(false);
                buttonOffers2.setAlpha(1);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(layoutManager);
                activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModelsOffers,rootView, us_id,recyclerView);
                recyclerView.setAdapter(activityFeedAdapter);
            }
        });


        // share buttons

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

        ivRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = RatingsDialog.newInstance(userId, ((MainActivity) context).isLoggedIn());
                ((MainActivity) context).initFragment(fragment, "RatingsDialog");
            }
        });

//        addCommentBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //submit
//            }
//        });

        rootView = view;
        return view;
    }

    private void refreshData(){
        profilePostModels.clear();
        profilePostModelsAppreciation.clear();
        profilePostModelsAchivement.clear();
        profilePostModelsRequired.clear();
        profilePostModelsForSale.clear();
        profilePostModelsOffers.clear();
    }

    private void setDefaultButton(boolean s){
        if (s){
            buttonAchievemnet.setAlpha(0.5f);
            buttonRandom.setAlpha(0.5f);
            buttonForsale.setAlpha(0.5f);
            buttonRequired.setAlpha(0.5f);
            buttonOffers.setAlpha(0.5f);
            buttonAppreciations.setAlpha(0.5f);
        }
        else{
            buttonAchievemnet2.setAlpha(0.5f);
            buttonRandom2.setAlpha(1);
            buttonForsale2.setAlpha(0.5f);
            buttonRequired2.setAlpha(0.5f);
            buttonOffers2.setAlpha(0.5f);
            buttonAppreciations2.setAlpha(0.5f);
        }
    }


    private void loadMoreComments(Context context,String us_id) {
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("getcomments.php");
        sqlHelper.setActionString("postComments");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", us_id);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(true);
    }

    private void fetchProfilePostInfo(Context context, String fetch_id,String pageNo) {
        SqlHelper sqlHelper = new SqlHelper(context, ProfileFragment.this);
        sqlHelper.setExecutePath("getprofilepost.php");
        sqlHelper.setActionString("profilePosts");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", us_id);
        contentValues.put("userid", ((MainActivity) context).isLoggedIn());
        contentValues.put("pageNo",pageNo);
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
                //inntRecyclerView(jsonArray, length);
                initTagList(jsonArray,length);
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


    private void initTagList(JSONArray jsonArray, int length) {
        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 1; i <= length; i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("offer").equals("assets/img/icon/ic_Offers-min.png")){
                    ProfilePostModel profilePostModelOffers = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelOffers.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsOffers.add(profilePostModelOffers);
                }
                if(jsonObject.getString("offer").equals("assets/img/icon/ic_ForSale-min.png")){
                    ProfilePostModel profilePostModelForSale = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelForSale.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsForSale.add(profilePostModelForSale);
                }
                if(jsonObject.getString("offer").equals("assets/img/icon/ic_Required-min.png")){
                    ProfilePostModel profilePostModelRequired = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelRequired.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsRequired.add(profilePostModelRequired);
                }
                if(jsonObject.getString("offer").equals("assets/img/icon/ic_Achievement-min.png")){
                    ProfilePostModel profilePostModelAchievement = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelAchievement.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsAchivement.add(profilePostModelAchievement);
                }
                if(jsonObject.getString("offer").equals("assets/img/icon/ic_Appreciations-min.png")){
                    ProfilePostModel profilePostModelAppreciation = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelAppreciation.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsAppreciation.add(profilePostModelAppreciation);
                }
                ProfilePostModel profilePostModel = modelHelper.buildProfilePostModel(jsonObject);
                profilePostModel.setCommentsModels(getCommentList(jsonObject));
                profilePostModels.add(profilePostModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModels,rootView, us_id,recyclerView);
        recyclerView.setAdapter(activityFeedAdapter);
        activityFeedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int index = (int)Math.ceil(((double)profilePostModels.size() / SEARCH_RESULT_LIMIT) )+ 1;
                        fetchProfilePostInfo(context,"",String.valueOf(index));
                        activityFeedAdapter.notifyDataSetChanged();
                        activityFeedAdapter.setLoaded();
                    }
                },1000);
            }
        });
    }

    private ArrayList<CommentsModel> getCommentList(JSONObject jsonObject) {
        JSONArray jsonArray;
        ModelHelper modelHelper = new ModelHelper(this.context);
        ArrayList<CommentsModel> commentsModels =  new ArrayList<>();
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
