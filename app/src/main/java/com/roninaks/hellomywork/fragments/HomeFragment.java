package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.adapters.HomeCategoriesAdapter;
import com.roninaks.hellomywork.adapters.HomeTopPerformersAdapter;
import com.roninaks.hellomywork.adapters.HomeUnionAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;
import com.roninaks.hellomywork.models.UnionModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//TODO: Top performers click
public class HomeFragment extends Fragment implements SqlDelegate {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //Private Members
    private Context context;
    private LinearLayout llContainerSales, llContainerRepairs, llContainerService, llContainerMovers, llContainerHealth, llContainerPersonal, llContainerEats, llContainerRest, llContainerEvents, llContainerRenovation, llContainerBusiness, llContainerMore;
    private TextView tvCategoriesMore, tvUnionsMore, tvLocation;
    private EditText etSearch;
    private ImageView ivNotification, ivProfile, ivSearch, ivOptions, ivBanner, ivLocationDropDown;
    private RecyclerView rvPopularCategories, rvUnions, rvTopPerformers;
    private AutoCompleteTextView acLocation;
    private View rootView;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<UnionModel> unionModels;
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private ArrayList<String> locationList;

    private OnFragmentInteractionListener mListener;

    private View.OnClickListener onDirectoriesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String path = "default";
            switch (v.getId()){
                case R.id.containerDirectorySales:{
                    path = "sales";
                    break;
                }
                case R.id.containerDirectoryRepairs:{
                    path = "repairs";
                    break;
                }
                case R.id.containerDirectoryService:{
                    path = "services";
                    break;
                }
                case R.id.containerDirectoryMovers:{
                    path = "movers";
                    break;
                }
                case R.id.containerDirectoryHealth:{
                    path = "health";
                    break;
                }
                case R.id.containerDirectoryPersonal:{
                    path = "personal";
                    break;
                }
                case R.id.containerDirectoryEats:{
                    path = "eats";
                    break;
                }
                case R.id.containerDirectoryRest:{
                    path = "rest";
                    break;
                }
                case R.id.containerDirectoryEvents:{
                    path = "events";
                    break;
                }
                case R.id.containerDirectoryRenovation:{
                    path = "renovation";
                    break;
                }
                case R.id.containerDirectoryBusiness:{
                    path = "business";
                    break;
                }
                case R.id.containerDirectoryMore:{
                    path = "more";
                    break;
                }
                case R.id.tvCategoriesMore:{
                    path = "all";
                    break;
                }
            }
            Fragment fragment = SearchLanding.newInstance(path,"");
            ((MainActivity) context).initFragment(fragment);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        locationList = new ArrayList<>();
        llContainerSales = (LinearLayout) rootView.findViewById(R.id.containerDirectorySales);
        llContainerRepairs = (LinearLayout) rootView.findViewById(R.id.containerDirectoryRepairs);
        llContainerService = (LinearLayout) rootView.findViewById(R.id.containerDirectoryService);
        llContainerMovers = (LinearLayout) rootView.findViewById(R.id.containerDirectoryMovers);
        llContainerHealth = (LinearLayout) rootView.findViewById(R.id.containerDirectoryHealth);
        llContainerPersonal = (LinearLayout) rootView.findViewById(R.id.containerDirectoryPersonal);
        llContainerEats = (LinearLayout) rootView.findViewById(R.id.containerDirectoryEats);
        llContainerRest = (LinearLayout) rootView.findViewById(R.id.containerDirectoryRest);
        llContainerEvents = (LinearLayout) rootView.findViewById(R.id.containerDirectoryEvents);
        llContainerRenovation = (LinearLayout) rootView.findViewById(R.id.containerDirectoryRenovation);
        llContainerBusiness = (LinearLayout) rootView.findViewById(R.id.containerDirectoryBusiness);
        llContainerMore = (LinearLayout) rootView.findViewById(R.id.containerDirectoryMore);
        tvCategoriesMore = (TextView) rootView.findViewById(R.id.tvCategoriesMore);
        tvUnionsMore = (TextView) rootView.findViewById(R.id.tvUnionsMore);
        tvLocation = (TextView) rootView.findViewById(R.id.tvLocation);
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        ivNotification = (ImageView) rootView.findViewById(R.id.imgNotification);
        ivProfile = (ImageView) rootView.findViewById(R.id.imgProfile);
        ivSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        ivOptions = (ImageView) rootView.findViewById(R.id.imgOptions);
        ivLocationDropDown = (ImageView) rootView.findViewById(R.id.imgLocationDropDown);
        rvPopularCategories = (RecyclerView) rootView.findViewById(R.id.rvPopularCategories);
        rvTopPerformers = (RecyclerView) rootView.findViewById(R.id.rvTopPerformers);
        rvUnions = (RecyclerView) rootView.findViewById(R.id.rvUnions);
        acLocation = (AutoCompleteTextView) rootView.findViewById(R.id.acLocation);

        //Set Defaults
        Resources res = context.getResources();
        locationList.addAll(Arrays.asList(res.getStringArray(R.array.india_top_places)));
        ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, locationList);
        acLocation.setAdapter(adapterLocation);
        loadCategories();
        loadUnions();
        loadProfiles();
        llContainerSales.setOnClickListener(onDirectoriesClickListener);
        llContainerRepairs.setOnClickListener(onDirectoriesClickListener);
        llContainerService.setOnClickListener(onDirectoriesClickListener);
        llContainerMovers.setOnClickListener(onDirectoriesClickListener);
        llContainerHealth.setOnClickListener(onDirectoriesClickListener);
        llContainerPersonal.setOnClickListener(onDirectoriesClickListener);
        llContainerEats.setOnClickListener(onDirectoriesClickListener);
        llContainerRest.setOnClickListener(onDirectoriesClickListener);
        llContainerEvents.setOnClickListener(onDirectoriesClickListener);
        llContainerRenovation.setOnClickListener(onDirectoriesClickListener);
        llContainerBusiness.setOnClickListener(onDirectoriesClickListener);
        llContainerMore.setOnClickListener(onDirectoriesClickListener);
        tvCategoriesMore.setOnClickListener(onDirectoriesClickListener);
        tvUnionsMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = UnionsFragment.newInstance("", "");
                ((MainActivity) context).initFragment(fragment);
            }
        });
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SearchResults.newInstance(etSearch.getText().toString(), "1", "");
                ((MainActivity) context).initFragment(fragment);
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ProfileFragment

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

        View.OnClickListener locationDropDown = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLocation.setVisibility(View.GONE);
                acLocation.setVisibility(View.VISIBLE);
            }
        };
        tvLocation.setOnClickListener(locationDropDown);
        ivLocationDropDown.setOnClickListener(locationDropDown);
        acLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    acLocation.setVisibility(View.GONE);
                    tvLocation.setVisibility(View.GONE);
                }
                return true;
            }
        });
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Fragment fragment = SearchResults.newInstance(etSearch.getText().toString(), "1", "");
                    ((MainActivity) context).initFragment(fragment);
                }
                return true;
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
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("response");
                    if(response.equals(context.getString(R.string.response_success))){
                        buildProfiles(jsonArray);
                    }else{
                        Toast.makeText(context, "Sorry. No results", Toast.LENGTH_SHORT).show();
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

    //private functions

    private void loadCategories(){
        SqlHelper sqlHelper = new SqlHelper(context, HomeFragment.this);
        sqlHelper.setExecutePath("getunion.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("categories");
        ContentValues params = new ContentValues();
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadUnions(){
        SqlHelper sqlHelper = new SqlHelper(context, HomeFragment.this);
        sqlHelper.setExecutePath("getunionlist.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("unions");
        ContentValues params = new ContentValues();
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadProfiles(){
        SqlHelper sqlHelper = new SqlHelper(context, HomeFragment.this);
        sqlHelper.setExecutePath("getprofile.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("profiles");
        ContentValues params = new ContentValues();
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void buildCategories(JSONArray jsonArray){
        try{
            categoryModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                CategoryModel categoryModel = new ModelHelper().buildCategoryModel(jsonArray.getJSONObject(i));
                categoryModels.add(categoryModel);
            }
            HomeCategoriesAdapter adapter = new HomeCategoriesAdapter(context, categoryModels, rootView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvPopularCategories.setLayoutManager(layoutManager);
            rvPopularCategories.setAdapter(adapter);
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
            HomeUnionAdapter adapter = new HomeUnionAdapter(context, unionModels, rootView);
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
                ServiceProviderModel serviceProviderModel = new ModelHelper().buildServiceProviderModel(jsonArray.getJSONObject(i), "top_performers");
                serviceProviderModels.add(serviceProviderModel);
            }
            HomeTopPerformersAdapter adapter = new HomeTopPerformersAdapter(context, serviceProviderModels, rootView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            rvTopPerformers.setLayoutManager(layoutManager);
            rvTopPerformers.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
