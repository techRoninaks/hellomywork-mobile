package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
//import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.AdminActivity;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.RegisterActivity;
import com.roninaks.hellomywork.adapters.HomeCategoriesAdapter;
import com.roninaks.hellomywork.adapters.SearchAdapter;
import com.roninaks.hellomywork.adapters.SearchLandingTabsAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.SearchSuggestionsModel;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchLanding.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchLanding#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchLanding extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "directory";
    private static final String ARG_PARAM2 = "param2";


    private String directory;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Private Members
    private Context context;
    private View rootView;
    private RecyclerView rvTabs, rvCategories;
    private ArrayList<String> tabList;
    private ArrayList<CategoryModel> categoryModels, alSales, alRepairs, alService, alMovers, alHealth, alPersonal, alEats, alRest, alEvents, alBusiness, alRenovation, alMore;
    private AutoCompleteTextView acSearch;
    private ImageView ivSearch, ivOptions, ivBanner;
    private LinearLayout llBanner;

    private ArrayList<SearchSuggestionsModel> searchSuggestionsModels;
    public SearchLanding() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchLanding.
     */

    public static SearchLanding newInstance(String param1, String param2) {
        SearchLanding fragment = new SearchLanding();
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
            directory = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_search_landing, container, false);
        context = getActivity();
        rvTabs = (RecyclerView) rootView.findViewById(R.id.rvTabs);
        rvCategories = (RecyclerView) rootView.findViewById(R.id.rvCategories);
        acSearch = (AutoCompleteTextView) rootView.findViewById(R.id.etSearch);
        ivSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        ivOptions = (ImageView) rootView.findViewById(R.id.imgOptions);
        ivBanner = (ImageView) rootView.findViewById(R.id.imgBanner);

        //Setting Defaults
        loadTabs();
        loadCategories();

        //Listeners
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SearchResults.newInstance(acSearch.getText().toString(), "1", "");
                ((MainActivity) context).initFragment(fragment);
            }
        });
        ivOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                Menu m = popup.getMenu();
                MenuInflater inflater = popup.getMenuInflater();
                if(((MainActivity) context).isLoggedIn().isEmpty()){
                    inflater.inflate(R.menu.options_menu, popup.getMenu());
                }
                else{
                    inflater.inflate(R.menu.options_menu_logged_in, popup.getMenu());
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.options_login:{
                                if(((MainActivity) context).isLoggedIn().isEmpty()){
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }else{
                                    Fragment fragment = ProfileFragment.newInstance(((MainActivity) context).isLoggedIn(), "");
                                    ((MainActivity) context).initFragment(fragment);
                                }
                                break;
                            }
                            case R.id.options_signup:{
                                if(((MainActivity) context).isLoggedIn().isEmpty()){
                                    context.startActivity(new Intent(context, RegisterActivity.class));
                                }else{
                                    Fragment fragment = ProfileFragment.newInstance(((MainActivity) context).isLoggedIn(), "");
                                    ((MainActivity) context).initFragment(fragment);
                                }
                                break;
                            }
                            case R.id.options_careers:{
                                Fragment fragment = CareersFragment.newInstance("", "");
                                ((MainActivity) context).initFragment(fragment);
                                break;
                            }
                            case R.id.options_about:{
                                Fragment fragment = AboutFragment.newInstance("", "");
                                ((MainActivity) context).initFragment(fragment);
                                break;
                            }
                            case R.id.options_contact:{
                                Fragment fragment = ContactFragment.newInstance("", "");
                                ((MainActivity) context).initFragment(fragment);
                                break;
                            }
                            case R.id.options_admin:{
                                Intent intent = new Intent(context, AdminActivity.class);
                                startActivity(intent);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        acSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Fragment fragment = SearchResults.newInstance(acSearch.getText().toString(), ((MainActivity) context).getDefaultLocation(), "");
                    ((MainActivity) context).initFragment(fragment);
                }
                return true;
            }
        });

        acSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchKey = s.toString();
                if(searchKey.length() == 3){
                    loadSearchList(searchKey);
                }
            }
        });
        acSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchString = acSearch.getText().toString();
                String[] search = searchString.split("\\[");
                Fragment fragment = SearchResults.newInstance(search[0].trim(), "" + searchSuggestionsModels.get(position).getLocationId(), "" + searchSuggestionsModels.get(position).getCategoryId());
                ((MainActivity) context).initFragment(fragment);
            }
        });
        return rootView;
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
                case "search":{
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    if(jsonArray.length() >= 1){
                        buildSearchList(jsonArray);
                    }
                    break;
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

        void onFragmentInteraction(Uri uri);
    }

    public void onTabClick(int position, ArrayList<String> tabs, int currentPosition, int widthFocused){
        String tag = tabs.get(position).toLowerCase();
        showList(tag);
        showBanner(tag);
        SearchLandingTabsAdapter adapter = new SearchLandingTabsAdapter(context, SearchLanding.this, tabList, rootView, tag);
        rvTabs.setAdapter(adapter);
        scrollToPosition(position, widthFocused);
    }

    public void scrollToPosition(int position, int offset){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels/2;
        int widthFocused = offset/2;
        rvTabs.scrollToPosition(position);
        if(position!=0)
            rvTabs.scrollBy(-width + widthFocused, 0);
    }

    //Private Functions

    private void loadSearchList(String key){
        SqlHelper sqlHelper = new SqlHelper(context, SearchLanding.this);
        sqlHelper.setExecutePath("searchAuto.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("search");
        ContentValues params = new ContentValues();
        params.put("search", key);
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(false);
    }

    private void loadCategories(){
        SqlHelper sqlHelper = new SqlHelper(context, SearchLanding.this);
        sqlHelper.setExecutePath("getunion.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("categories");
        ContentValues params = new ContentValues();
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void loadTabs(){
        tabList = new ArrayList<>();
        Resources res = context.getResources();
        tabList.addAll(Arrays.asList(res.getStringArray(R.array.search_landing_tabs)));
        SearchLandingTabsAdapter adapter = new SearchLandingTabsAdapter(context, SearchLanding.this, tabList, rootView, directory.toLowerCase());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        rvTabs.setLayoutManager(layoutManager);
        rvTabs.setAdapter(adapter);
    }

    private void buildCategories(JSONArray jsonArray){
        try{
            categoryModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                CategoryModel categoryModel = new ModelHelper().buildCategoryModel(jsonArray.getJSONObject(i));
                categoryModels.add(categoryModel);
            }
            GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
            rvCategories.setLayoutManager(layoutManager);
            filterList();
            showList(directory.toLowerCase());
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filterList(){
        alSales = new ArrayList<>();
        alRepairs = new ArrayList<>();
        alService = new ArrayList<>();
        alMovers = new ArrayList<>();
        alHealth = new ArrayList<>();
        alPersonal = new ArrayList<>();
        alEats = new ArrayList<>();
        alRest = new ArrayList<>();
        alEvents = new ArrayList<>();
        alBusiness = new ArrayList<>();
        alRenovation = new ArrayList<>();
        alMore = new ArrayList<>();
        for(int i = 0; i < categoryModels.size(); i++){
            CategoryModel categoryModel = categoryModels.get(i);
            String tag = categoryModel.getTag().toLowerCase();
            boolean ignored = true;
            if(tag.equals("sales")){
                alSales.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("repairs")){
                alRepairs.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("service")){
                alService.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("movers")){
                alMovers.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("health")){
                alHealth.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("personal")){
                alPersonal.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("eats")){
                alEats.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("events")){
                alEvents.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("business")){
                alBusiness.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("rest")){
                alRest.add(categoryModel);
                ignored = false;
            }
            if(tag.equals("renovation")){
                alRenovation.add(categoryModel);
                ignored = false;
            }
            if(ignored){
                alMore.add(categoryModel);
            }
        }
    }

    private void showList(String tag){
        HomeCategoriesAdapter adapter = new HomeCategoriesAdapter(context, categoryModels, rootView, "search");
        if(tag.equals("sales")){
            adapter = new HomeCategoriesAdapter(context, alSales, rootView, "search");
        }
        if(tag.equals("repairs")){
            adapter = new HomeCategoriesAdapter(context, alRepairs, rootView, "search");
        }
        if(tag.equals("service")){
            adapter = new HomeCategoriesAdapter(context, alService, rootView, "search");
        }
        if(tag.equals("movers")){
            adapter = new HomeCategoriesAdapter(context, alMovers, rootView, "search");
        }
        if(tag.equals("health")){
            adapter = new HomeCategoriesAdapter(context, alHealth, rootView, "search");
        }
        if(tag.equals("personal")){
            adapter = new HomeCategoriesAdapter(context, alPersonal, rootView, "search");
        }
        if(tag.equals("eats")){
            adapter = new HomeCategoriesAdapter(context, alEats, rootView, "search");
        }
        if(tag.equals("rest")){
            adapter = new HomeCategoriesAdapter(context, alRest, rootView, "search");
        }
        if(tag.equals("events")){
            adapter = new HomeCategoriesAdapter(context, alEvents, rootView, "search");
        }
        if(tag.equals("business")){
            adapter = new HomeCategoriesAdapter(context, alBusiness, rootView, "search");
        }
        if(tag.equals("renovation")){
            adapter = new HomeCategoriesAdapter(context, alRenovation, rootView, "search");
        }
        if(tag.equals("more")){
            adapter = new HomeCategoriesAdapter(context, alMore, rootView, "search");
        }
        rvCategories.setAdapter(adapter);
    }

    private void showBanner(String tag){
        String imgString = context.getString(R.string.sl_banner_prefix) + tag;
        int imgResource = context.getResources().getIdentifier(imgString, "drawable",
                context.getPackageName());
        ivBanner.setImageDrawable(context.getDrawable(imgResource));
    }

    private void buildSearchList(JSONArray jsonArray){
        try{
            searchSuggestionsModels = new ArrayList<>();
            for(int i=0; i < jsonArray.length(); i++){
                SearchSuggestionsModel searchSuggestionsModel = new ModelHelper().buildSearchSuggestionsModel(jsonArray.getString(i), "Home");
                searchSuggestionsModels.add(searchSuggestionsModel);
            }
            SearchAdapter adapter = new SearchAdapter(context, acSearch.getId(), searchSuggestionsModels, SearchLanding.this);
            acSearch.setAdapter(adapter);
        }catch (Exception e){

        }
    }

}
