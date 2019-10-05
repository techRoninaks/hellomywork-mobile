package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.disklrucache.DiskLruCache;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.ProfileImage;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostAdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostAdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostAdFragment extends DialogFragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText etPostAd;
    Button buttonEdit,buttonImageUpload,buttonForsale,buttonOffers,buttonRequired,buttonAppreciations,buttonAchievemnet,buttonRandom,buttonPost;


    String tagButton;
    private String mParam1;
    private String mParam2;
    private  Context context;

    //Static Members
    public static String imageUrl = "";
    public static boolean imageChanged = false;
    public static Bitmap bitmap;

    private OnFragmentInteractionListener mListener;

    public PostAdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostAdFragment.
     */

    public static PostAdFragment newInstance(String param1, String param2) {
        PostAdFragment fragment = new PostAdFragment();
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
        View view = inflater.inflate(R.layout.fragment_post_ad, container, false);
        context = getActivity();
        buttonAchievemnet = view.findViewById(R.id.Achievement_BTN);
        buttonRandom = view.findViewById(R.id.Random_BTN);
        buttonAppreciations = view.findViewById(R.id.Appreciation_BTN);
        buttonOffers = view.findViewById(R.id.Offers_BTN);
        buttonImageUpload = view.findViewById(R.id.button_ImageUpload);
        buttonForsale = view.findViewById(R.id.ForSale_BTN);
        buttonRequired =view.findViewById(R.id.Required_BTN);
        buttonPost =view.findViewById(R.id.button_Post);
        etPostAd = view.findViewById(R.id.Post_ad_Description);

        //Setting Default Values
        imageUrl = "";
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

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPostAd.equals("")){
                    Toast.makeText(context, "Descrpition is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveInformation();
                }
            }
        });

        buttonImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileImage.class);
                Bundle bundle = new Bundle();
                bundle.putString("image", imageUrl);
                bundle.putString("fragment", "postad");
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    private void saveInformation(){
        SqlHelper sqlHelper = new SqlHelper(context, PostAdFragment.this);
        sqlHelper.setExecutePath("postpost.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("submit");
        ContentValues params = new ContentValues();
        params.put("u_id", ((MainActivity) context).isLoggedIn());
        params.put("des", etPostAd.getText().toString());
        params.put("tag", tagButton);
        params.put("image", imageChanged ? StringHelper.imageToString(bitmap) : "1");
        params.put("mob", "1");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
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
}
