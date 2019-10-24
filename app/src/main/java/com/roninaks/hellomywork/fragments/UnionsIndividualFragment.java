package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.adapters.ActivityFeedAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CommentsModel;
import com.roninaks.hellomywork.models.ProfilePostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnionsIndividualFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnionsIndividualFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnionsIndividualFragment extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<ProfilePostModel> profilePostModels,profilePostModelsOffers,profilePostModelsForSale,profilePostModelsRequired,profilePostModelsAchivement,profilePostModelsAppreciation;
    private Context context;
    private TextView unionName;
    private ActivityFeedAdapter activityFeedAdapter;
    private ImageView ivBack;
    private View rootView;

    private OnFragmentInteractionListener mListener;
    private EditText writeComments;
    private TextView sendComment;
    private TextView viewMoreComments;

    private Button btnUnionRandom,btnUnionForsale,btnUnionOffers,btnUnionRequired,btnUnionAppreciation,btnUnionAchivement;

    public UnionsIndividualFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnionsIndividualFragmentTemp.
     */

    public static UnionsIndividualFragment newInstance(String param1, String param2) {
        UnionsIndividualFragment fragment = new UnionsIndividualFragment();
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
        rootView = inflater.inflate(R.layout.fragment_unions_individual, container, false);
        profilePostModels = new ArrayList<>();
        unionName = rootView.findViewById(R.id.union_indi_name);
        ivBack = rootView.findViewById(R.id.imgBack);
        recyclerView = rootView.findViewById(R.id.unionPostRv);

        writeComments=rootView.findViewById(R.id.editText_WriteComments);
        viewMoreComments = rootView.findViewById(R.id.view_more_comments);
        sendComment = rootView.findViewById(R.id.addComment);

        btnUnionRandom = rootView.findViewById(R.id.unions_btn_random);
        btnUnionForsale = rootView.findViewById(R.id.unions_btn_forsale);
        btnUnionRequired = rootView.findViewById(R.id.unions_btn_required);
        btnUnionOffers = rootView.findViewById(R.id.unions_btn_offers);
        btnUnionAchivement = rootView.findViewById(R.id.unions_btn_achivement);
        btnUnionAppreciation = rootView.findViewById(R.id.unions_btn_appreciation);

        profilePostModelsOffers = new ArrayList<>();
        profilePostModelsForSale = new ArrayList<>();
        profilePostModelsAchivement = new ArrayList<>();
        profilePostModelsAppreciation = new ArrayList<>();
        profilePostModelsRequired = new ArrayList<>();

        fetchProfilePostInfo(context, mParam1);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).onBackPressed();
            }
        });


        btnUnionRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(btnUnionRandom);
                filterPost(profilePostModels);
            }
        });
        btnUnionForsale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(btnUnionForsale);
                filterPost(profilePostModelsForSale);
            }
        });
        btnUnionOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(btnUnionOffers);
                filterPost(profilePostModelsOffers);
            }
        });
        btnUnionRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(btnUnionRequired);
                filterPost(profilePostModelsRequired);
            }
        });
        btnUnionAppreciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(btnUnionAppreciation);
                filterPost(profilePostModelsAppreciation);
            }
        });
        btnUnionAchivement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonActive(btnUnionAchivement);
                filterPost(profilePostModelsAchivement);
            }
        });

        return rootView;
    }

    private void fetchProfilePostInfo(Context context, String fetch_id) {
//        SqlHelper sqlHelper = new SqlHelper(context, UnionsIndividualFragment.this);
//        sqlHelper.setExecutePath("getallpost.php");
//        sqlHelper.setActionString("profilePosts");
//        sqlHelper.setMethod("GET");
//        sqlHelper.setParams(new ContentValues());
//        sqlHelper.executeUrl(false);

        SqlHelper sqlHelper = new SqlHelper(context, UnionsIndividualFragment.this);
        sqlHelper.setExecutePath("getserviceposts.php");
        sqlHelper.setActionString("profilePosts");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", mParam1);
        contentValues.put("action", "queryAction");
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(false);
    }

    private void setButtonActive(Button button) {
        btnUnionRandom.setAlpha(0.5f);
        btnUnionForsale.setAlpha(0.5f);
        btnUnionRequired.setAlpha(0.5f);
        btnUnionOffers.setAlpha(0.5f);
        btnUnionAppreciation.setAlpha(0.5f);
        btnUnionAchivement.setAlpha(0.5f);
        button.setAlpha(1);
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
        if(sqlHelper.getActionString() =="profilePosts"){
            try {
                JSONArray jsonArray = new JSONArray(response);
                if(jsonArray.length()<2){
                    unionName.setText(jsonArray.getJSONObject(0).getString("unionName"));
                    Toast.makeText(context, "No posts in this union", Toast.LENGTH_SHORT).show();
                }
                else {
                    JSONObject jsonObject = jsonArray.getJSONObject(1);
                    int length = (jsonArray.getJSONObject(1).length());
                    unionName.setText(jsonArray.getJSONObject(0).getString("unionName"));
                    inntRecyclerView(jsonArray, length);
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void initTagList(JSONArray jsonArray, int length) {
        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 1; i <= length; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("offer").equals("assets/img/icon/ic_Offers-min.png")) {
                    ProfilePostModel profilePostModelOffers = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelOffers.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsOffers.add(profilePostModelOffers);
                }
                if (jsonObject.getString("offer").equals("assets/img/icon/ic_ForSale-min.png")) {
                    ProfilePostModel profilePostModelForSale = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelForSale.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsForSale.add(profilePostModelForSale);
                }
                if (jsonObject.getString("offer").equals("assets/img/icon/ic_Required-min.png")) {
                    ProfilePostModel profilePostModelRequired = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelRequired.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsRequired.add(profilePostModelRequired);
                }
                if (jsonObject.getString("offer").equals("assets/img/icon/ic_Achievement-min.png")) {
                    ProfilePostModel profilePostModelAchievement = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelAchievement.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsAchivement.add(profilePostModelAchievement);
                }
                if (jsonObject.getString("offer").equals("assets/img/icon/ic_Appreciations-min.png")) {
                    ProfilePostModel profilePostModelAppreciation = modelHelper.buildProfilePostModel(jsonObject);
                    profilePostModelAppreciation.setCommentsModels(getCommentList(jsonObject));
                    profilePostModelsAppreciation.add(profilePostModelAppreciation);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterPost(ArrayList<ProfilePostModel> profPostModels){
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        activityFeedAdapter = new ActivityFeedAdapter(context, profPostModels,rootView);
        recyclerView.setAdapter(activityFeedAdapter);
    }

    private void inntRecyclerView(JSONArray jsonArray, int length) {

        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 1; i <= length; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProfilePostModel profilePostModel = modelHelper.buildProfilePostModel(jsonObject);
                profilePostModel.setCommentsModels(getCommentList(jsonObject));
                initTagList(jsonArray,length);
                profilePostModels.add(profilePostModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            filterPost(profilePostModels);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        activityFeedAdapter = new ActivityFeedAdapter(context, profilePostModels,rootView);
        recyclerView.setAdapter(activityFeedAdapter);
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
