package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.adapters.HomeCategoriesAdapter;
import com.roninaks.hellomywork.adapters.SearchLandingTabsAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CategoryModel;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "directory";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String directory;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Private Members
    private Context context;
    private View rootView;
    private RecyclerView rvTabs, rvCategories;
    private ArrayList<String> tabList;
    private ArrayList<CategoryModel> categoryModels, alSales, alRepairs, alService, alMovers, alHealth, alPersonal, alEats, alRest, alEvents, alBusiness, alRenovation, alMore;
    private EditText etSearch;
    private ImageView ivSearch, ivOptions;

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
    // TODO: Rename and change types and number of parameters
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
        etSearch = (EditText) rootView.findViewById(R.id.etSearch);
        ivSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        ivOptions = (ImageView) rootView.findViewById(R.id.imgOptions);

        //Setting Defaults
        loadTabs();
        loadCategories();

        //Listeners
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

    public void onTabClick(int position, ArrayList<String> tabs, int currentPosition){
//        TextView tvCurrent = (TextView)rvTabs.findViewHolderForLayoutPosition(position).itemView.findViewById(R.id.tvTabName);
//        tvCurrent.setTypeface(tvCurrent.getTypeface(), Typeface.BOLD);
//        TextView tvPrevious = (TextView)rvTabs.findViewHolderForLayoutPosition(currentPosition).itemView.findViewById(R.id.tvTabName);
//        tvPrevious.setTypeface(tvPrevious.getTypeface(), Typeface.NORMAL);
        String tag = tabs.get(position).toLowerCase();
        showList(tag);
    }

    //Private Functions

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
        SearchLandingTabsAdapter adapter = new SearchLandingTabsAdapter(context, SearchLanding.this, tabList, rootView);
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
        HomeCategoriesAdapter adapter = new HomeCategoriesAdapter(context, categoryModels, rootView);;
        if(tag.equals("sales")){
            adapter = new HomeCategoriesAdapter(context, alSales, rootView);
        }
        if(tag.equals("repairs")){
            adapter = new HomeCategoriesAdapter(context, alRepairs, rootView);
        }
        if(tag.equals("service")){
            adapter = new HomeCategoriesAdapter(context, alService, rootView);
        }
        if(tag.equals("movers")){
            adapter = new HomeCategoriesAdapter(context, alMovers, rootView);
        }
        if(tag.equals("health")){
            adapter = new HomeCategoriesAdapter(context, alHealth, rootView);
        }
        if(tag.equals("personal")){
            adapter = new HomeCategoriesAdapter(context, alPersonal, rootView);
        }
        if(tag.equals("eats")){
            adapter = new HomeCategoriesAdapter(context, alEats, rootView);
        }
        if(tag.equals("events")){
            adapter = new HomeCategoriesAdapter(context, alEvents, rootView);
        }
        if(tag.equals("business")){
            adapter = new HomeCategoriesAdapter(context, alBusiness, rootView);
        }
        if(tag.equals("renovation")){
            adapter = new HomeCategoriesAdapter(context, alRenovation, rootView);
        }
        if(tag.equals("more")){
            adapter = new HomeCategoriesAdapter(context, alMore, rootView);
        }
        rvCategories.setAdapter(adapter);
    }
}
