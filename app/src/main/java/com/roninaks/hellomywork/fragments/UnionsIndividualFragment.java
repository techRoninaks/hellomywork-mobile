package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private ArrayList<ProfilePostModel> profilePostModels;
    private Context context;
    private TextView unionName;
    private ActivityFeedAdapter activityFeedAdapter;
    private ImageView ivBack;
    private View rootView;

    private OnFragmentInteractionListener mListener;

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
    // TODO: Rename and change types and number of parameters
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
        fetchProfilePostInfo(context, mParam1);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).onBackPressed();
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

    private void inntRecyclerView(JSONArray jsonArray, int length) {

        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 1; i <= length; i++) {
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
}
