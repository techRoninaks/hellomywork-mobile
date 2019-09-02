package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.adapters.SearchProfileAdapter;
import com.roninaks.hellomywork.adapters.SearchServiceAdapter;
import com.roninaks.hellomywork.adapters.SearchUnionAdapter;
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
 * {@link SearchResults.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchResults#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResults extends Fragment implements SqlDelegate {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "search_key";
    private static final String ARG_PARAM2 = "location";
    private static final String ARG_PARAM3 = "category";
    private static final String ARG_PARAM4 = "default";

    // TODO: Rename and change types of parameters
    private String searchKey;
    private String location;
    private String category;
    private String default_load;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<UnionModel> unionModels;
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private Context context;

    //Private Members
    View rootView;
    TextView tvTabService, tvTabProfile, tvTabUnions;
    LinearLayout llContainerService, llContainerProfiles, llContainerUnions;
    RecyclerView rvServices, rvProfiles, rvUnions;
    private EditText etSearch;
    private ImageView ivSearch, ivOptions;
    private OnFragmentInteractionListener mListener;


    public SearchResults() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 SearchKey.
     * @param param2 Location.
     * @param param3 Category.
     * @return A new instance of fragment SearchResults.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResults newInstance(String param1, String param2, String param3) {
        SearchResults fragment = new SearchResults();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 SearchKey.
     * @param param2 Location.
     * @param param3 Category.
     * @param param4 Default Value - Services | Profiles | Unions
     * @return A new instance of fragment SearchResults.
     */
    public static SearchResults newInstance(String param1, String param2, String param3, String param4) {
        SearchResults fragment = new SearchResults();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchKey = getArguments().getString(ARG_PARAM1);
            location = getArguments().getString(ARG_PARAM2);
            category = getArguments().getString(ARG_PARAM3);
            default_load = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        context = getActivity();
        tvTabProfile = (TextView) rootView.findViewById(R.id.tvTabProfiles);
        tvTabService = (TextView) rootView.findViewById(R.id.tvTabService);
        tvTabUnions = (TextView) rootView.findViewById(R.id.tvTabUnions);
        llContainerProfiles = (LinearLayout) rootView.findViewById(R.id.containerProfile);
        llContainerService = (LinearLayout) rootView.findViewById(R.id.containerService);
        llContainerUnions = (LinearLayout) rootView.findViewById(R.id.containerUnions);
        rvProfiles = (RecyclerView) rootView.findViewById(R.id.rvProfiles);
        rvServices = (RecyclerView) rootView.findViewById(R.id.rvServices);
        rvUnions = (RecyclerView) rootView.findViewById(R.id.rvUnions);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        ivSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        ivOptions = (ImageView) rootView.findViewById(R.id.imgOptions);
        //Defaults
        toggleTabs(default_load == null || default_load.isEmpty() ? "services" : default_load);
        etSearch.setText(searchKey);
        //On click listeners
        View.OnClickListener toggleTabsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tvTabProfiles:{
                        toggleTabs("profiles");
                        break;
                    }
                    case R.id.tvTabService:{
                        toggleTabs("services");
                        break;
                    }
                    case R.id.tvTabUnions:{
                        toggleTabs("unions");
                        break;
                    }
                }
            }
        };
        tvTabProfile.setOnClickListener(toggleTabsListener);
        tvTabService.setOnClickListener(toggleTabsListener);
        tvTabUnions.setOnClickListener(toggleTabsListener);

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SearchResults.newInstance(etSearch.getText().toString(), "1", "");
                ((MainActivity) context).initFragment(fragment);
            }
        });
        ivOptions.setOnClickListener(new View.OnClickListener() {
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
                case "categories":{
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("response");
                    if(response.equals(context.getString(R.string.response_success))){
                        buildCategories(jsonArray);
                    }else{
                        Toast.makeText(context, "Sorry. Your category list seems empty", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case "unions":{
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("response");
                    if(response.equals(context.getString(R.string.response_success))){
                        buildUnions(jsonArray);
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

    //Loaders
    private void loadCategories(){
        SqlHelper sqlHelper = new SqlHelper(context, SearchResults.this);
        sqlHelper.setExecutePath("searchcategory.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("categories");
        ContentValues params = new ContentValues();
        params.put("srch_key", searchKey);
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadUnions(){
        SqlHelper sqlHelper = new SqlHelper(context, SearchResults.this);
        sqlHelper.setExecutePath("searchunions.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("unions");
        ContentValues params = new ContentValues();
        params.put("srch_key", searchKey);
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadProfiles(){
        SqlHelper sqlHelper = new SqlHelper(context, SearchResults.this);
        sqlHelper.setExecutePath("searchQuery.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("profiles");
        ContentValues params = new ContentValues();
        params.put("srchType", searchKey);
        params.put("locType", location);
        params.put("catType", category);
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
                llContainerUnions.setVisibility(View.GONE);
                llContainerService.setVisibility(View.GONE);
                llContainerProfiles.setVisibility(View.VISIBLE);
                break;
            }
            case "services":{
                if(categoryModels == null){
                    loadCategories();
                }
                llContainerProfiles.setVisibility(View.GONE);
                llContainerUnions.setVisibility(View.GONE);
                llContainerService.setVisibility(View.VISIBLE);
                break;
            }
            case "unions":{
                if(unionModels == null){
                    loadUnions();
                }
                llContainerProfiles.setVisibility(View.GONE);
                llContainerService.setVisibility(View.GONE);
                llContainerUnions.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void buildCategories(JSONArray jsonArray){
        try{
            categoryModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                CategoryModel categoryModel = new ModelHelper().buildCategoryModel(jsonArray.getJSONObject(i));
                categoryModels.add(categoryModel);
            }
            SearchServiceAdapter adapter = new SearchServiceAdapter(context, categoryModels, rootView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            rvServices.setLayoutManager(layoutManager);
            rvServices.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buildUnions(JSONArray jsonArray){
        try{
            unionModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                UnionModel unionModel = new ModelHelper().buildUnionModel(jsonArray.getJSONObject(i));
                unionModels.add(unionModel);
            }
            SearchUnionAdapter adapter = new SearchUnionAdapter(context, unionModels, rootView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            rvUnions.setLayoutManager(layoutManager);
            rvUnions.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
