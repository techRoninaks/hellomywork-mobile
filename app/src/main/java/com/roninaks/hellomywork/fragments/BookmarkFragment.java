package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.adapters.SearchProfileAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;
import com.roninaks.hellomywork.models.UnionModel;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment implements SqlDelegate {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String userId;
    private String default_load;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<UnionModel> unionModels;
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private Context context;
    LinearLayout bmcontainerProfile, bmcontainerPosts;



    View rootView,bmporfilenav,bmpostnav;
    TextView bmhead, bmprofiles, bmposts;
    private ImageView backarrow, imgOptions;
    LinearLayout LlContainerProfile, llContainerPost;
    RecyclerView rvProfiles, rvPosts;


    private OnFragmentInteractionListener mListener;

    public BookmarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkFragment.
     */
    // TODO: Rename and change types and number of parameters



    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
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
        rootView = inflater.inflate(R.layout.fragment_bookmark, container, false);
        context = getActivity();
        userId = ((MainActivity)context).isLoggedIn();
        if(userId.isEmpty()){
            //TODO Redirect to home page
        }
        bmhead = (TextView) rootView.findViewById(R.id.bmhead);
        bmprofiles = (TextView) rootView.findViewById(R.id.bmprofiles);
        bmposts = (TextView) rootView.findViewById(R.id.bmposts);
        bmcontainerProfile = (LinearLayout) rootView.findViewById(R.id.bmcontainerProfile);
        bmcontainerPosts = (LinearLayout) rootView.findViewById(R.id.bmcontainerPosts);
        bmpostnav = (View) rootView.findViewById(R.id.bmpostnav);
        bmporfilenav = (View) rootView.findViewById(R.id.bmprofilenav);
        rvProfiles = (RecyclerView) rootView.findViewById(R.id.rvProfiles);
        rvPosts = (RecyclerView) rootView.findViewById(R.id.rvPosts);
//        backarrow = (ImageView) rootView.findViewById(R.id.backarrow);
        imgOptions = (ImageView) rootView.findViewById(R.id.imgOptions);

        //Defaults
        toggleTabs(default_load == null || default_load.isEmpty() ? "profiles" : default_load);

        imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                Menu m = popup.getMenu();
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.options_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.options_login:{
                                break;
                            }
                            case R.id.options_signup:{
                                break;
                            }
                            case R.id.options_careers:{
                                break;
                            }
                            case R.id.options_about:{
                                Fragment fragment = AboutFragment.newInstance("", "");
                                ((MainActivity) context).initFragment(fragment);
                                break;
                            }
                            case R.id.options_contact:{
                                break;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        View.OnClickListener toggleTabsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bmprofiles: {
                        toggleTabs("profiles");
                        break;
                    }
                    case R.id.bmposts: {
                        toggleTabs("services");
                        break;
                    }
                }
            }
        };

        bmprofiles.setOnClickListener(toggleTabsListener);
        bmposts.setOnClickListener(toggleTabsListener);

        // option popup


        return rootView;
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
                case "posts":{
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("response");
                    if(response.equals(context.getString(R.string.response_success))){
                        buildPosts(jsonArray);
                    }else{
                        Toast.makeText(context, "Sorry. Your Union list seems empty", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case "profiles":{
                    if(sqlHelper.getStringResponse().equals("0")){
                        Toast.makeText(context, "Sorry. No results", Toast.LENGTH_SHORT).show();
                    }else{
                        JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                        buildProfiles(jsonArray);
                    }
                }
            }
        }catch (Exception e){

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


    // Load container for tabs

    private void loadProfiles(){
        SqlHelper sqlHelper = new SqlHelper(context, BookmarkFragment.this);
        sqlHelper.setExecutePath("getbookmarks.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("profiles");
        ContentValues params = new ContentValues();
        params.put("userId", userId);
        params.put("type", "profiles");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadPosts() {
        SqlHelper sqlHelper = new SqlHelper(context, BookmarkFragment.this);
        sqlHelper.setExecutePath("searchQuery.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("profiles");
        ContentValues params = new ContentValues();
//        params.put("srchType", searchKey);
//        params.put("locType", location);
//        params.put("catType", category);
        params.put("pageNo", "1");
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    //Private Functions
    private void toggleTabs(String tabName){
        switch (tabName){
            case "profiles":{
                if(serviceProviderModels == null){
                    loadProfiles();
                }
                bmprofiles.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
                bmposts.setTextColor(getResources().getColor(R.color.colorTextBlackHint));

                bmporfilenav.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                bmpostnav.setBackgroundColor(getResources().getColor(R.color.colorTextWhiteSecondary));

                bmcontainerProfile.setVisibility(View.VISIBLE);
                bmcontainerPosts.setVisibility(View.GONE);
                break;
            }
            case "posts":{
                if(categoryModels == null){
                    loadPosts();
                }
                bmposts.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
                bmprofiles.setTextColor(getResources().getColor(R.color.colorTextBlackHint));

                bmpostnav.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                bmporfilenav.setBackgroundColor(getResources().getColor(R.color.colorTextWhiteSecondary));

                bmcontainerPosts.setVisibility(View.VISIBLE);
                bmcontainerProfile.setVisibility(View.GONE);
                break;
            }
        }
    }

    private void buildProfiles(JSONArray jsonArray){
        try{
            serviceProviderModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                ServiceProviderModel serviceProviderModel = new ModelHelper().buildServiceProviderModel(jsonArray.getJSONObject(i), "search_profiles");
                serviceProviderModels.add(serviceProviderModel);
            }
            GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
            SearchProfileAdapter adapter = new SearchProfileAdapter(context, serviceProviderModels, rootView);
            rvProfiles.setLayoutManager(layoutManager);
            rvProfiles.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buildPosts(JSONArray jsonArray){
        try{
            serviceProviderModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                ServiceProviderModel serviceProviderModel = new ModelHelper().buildServiceProviderModel(jsonArray.getJSONObject(i), "search_profiles");
                serviceProviderModels.add(serviceProviderModel);
            }
            GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
            SearchProfileAdapter adapter = new SearchProfileAdapter(context, serviceProviderModels, rootView);
            rvProfiles.setLayoutManager(layoutManager);
            rvProfiles.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
