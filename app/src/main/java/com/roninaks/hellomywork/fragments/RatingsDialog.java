package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.adapters.RatingsAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.RatingsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RatingsDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RatingsDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingsDialog extends DialogFragment implements SqlDelegate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "userId";
    private static final String ARG_PARAM2 = "currentUserId";

    // TODO: Rename and change types of parameters
    private String userId;
    private String currentUserId;

    //Private Members
    private Context context;
    private View rootView;
    private TextView tvMainRating, tvTotalRatings, tvViewMore, tvTotalReviews;
    private EditText etReview;
    private Button btnSubmit;
    private RecyclerView rvRatings;
    private RatingBar rbRatings;
    private ArrayList<RatingsModel> ratingsModels;

    private OnFragmentInteractionListener mListener;

    public RatingsDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userId User Id to Rate.
     * @param currentUserId User Id of current User.
     * @return A new instance of fragment RatingsDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static RatingsDialog newInstance(String userId, String currentUserId) {
        RatingsDialog fragment = new RatingsDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userId);
        args.putString(ARG_PARAM2, currentUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString(ARG_PARAM1);
            currentUserId = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ratings_dialog, container, false);
        context = getActivity();
        fetchRatingsAndReviews();
        tvMainRating = (TextView) rootView.findViewById(R.id.tvMainRating);
        tvTotalRatings = (TextView) rootView.findViewById(R.id.tvRatingCount);
        tvTotalReviews = (TextView) rootView.findViewById(R.id.tvTotalReviews);
        tvViewMore = (TextView) rootView.findViewById(R.id.tvViewMore);
        etReview = (EditText) rootView.findViewById(R.id.etReview);
        btnSubmit = (Button) rootView.findViewById(R.id.btn_Submit);
        rbRatings = (RatingBar) rootView.findViewById(R.id.rb_UserRating);
        rvRatings = (RecyclerView) rootView.findViewById(R.id.rvRating);

        //Click events
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRatingsAndReviews();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
                case "ratings-get":{
                    JSONObject jsonObject = new JSONObject(sqlHelper.getStringResponse());
                    JSONObject master = jsonObject.getJSONObject("master");
                    tvMainRating.setText("" + (int) Float.parseFloat(master.getString("avg_rating")));
                    rbRatings.setRating((int) Float.parseFloat(master.getString("rating")));
                    tvTotalRatings.setText(master.getString("total_ratings") + " people rated");
                    tvTotalReviews.setText(master.getString("total_reviews") + " reviews");
                    buildRatings(jsonObject.getJSONArray("data"));
                    break;
                }
                case "ratings-save":{
                    if(sqlHelper.getStringResponse().equals(context.getString(R.string.response_success))){
                        Toast.makeText(context, "Your ratings have been saved", Toast.LENGTH_SHORT).show();
                        RatingsDialog.this.dismiss();
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //Private Functions

    private void fetchRatingsAndReviews(){
        SqlHelper sqlHelper = new SqlHelper(context, RatingsDialog.this);
        sqlHelper.setExecutePath("getratings.php");
        sqlHelper.setActionString("ratings-get");
        sqlHelper.setMethod("GET");
        ContentValues contentValues = new ContentValues();
        contentValues.put("u_id", userId);
        contentValues.put("c_id", currentUserId);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(true);
    }

    private void saveRatingsAndReviews(){
        SqlHelper sqlHelper = new SqlHelper(context, RatingsDialog.this);
        sqlHelper.setExecutePath("updateratings.php");
        sqlHelper.setActionString("ratings-save");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("u_id", userId);
        contentValues.put("c_id", currentUserId);
        contentValues.put("rating", rbRatings.getRating());
        contentValues.put("review", etReview.getText().toString());
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(true);
    }

    private void buildRatings(JSONArray jsonArray){
        try{
            ratingsModels = new ArrayList<>();
            for(int i = 0; i < jsonArray.length(); i++){
                RatingsModel ratingsModel = new ModelHelper().buildRatingsModel(jsonArray.getJSONObject(i));
                ratingsModels.add(ratingsModel);
            }
            RatingsAdapter adapter = new RatingsAdapter(context, ratingsModels, rootView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            rvRatings.setLayoutManager(layoutManager);
            rvRatings.setAdapter(adapter);
        }catch (Exception e){

        }
    }
}
