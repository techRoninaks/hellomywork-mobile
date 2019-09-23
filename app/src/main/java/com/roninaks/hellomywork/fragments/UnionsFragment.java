package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.AdminActivity;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.RegisterActivity;
import com.roninaks.hellomywork.adapters.UnionsAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.UnionModel;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UnionsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UnionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UnionsFragment extends Fragment implements SqlDelegate {

    //TODO Unions Banner
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //Private Members
    private View rootView;
    private Context context;
    private RecyclerView rvUnions;
    private ArrayList<UnionModel> unionModels;
    private AutoCompleteTextView acSearch;
    private ImageView ivSearch, ivOptions;

    public UnionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UnionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UnionsFragment newInstance(String param1, String param2) {
        UnionsFragment fragment = new UnionsFragment();
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
        rootView = inflater.inflate(R.layout.fragment_unions, container, false);
        context = getActivity();
        rvUnions = rootView.findViewById(R.id.rvUnions);
        acSearch = (AutoCompleteTextView) rootView.findViewById(R.id.etSearch);
        ivSearch = (ImageView) rootView.findViewById(R.id.imgSearch);
        ivOptions = (ImageView) rootView.findViewById(R.id.imgOptions);
        //Load Defaults
        loadUnions();

        //Listeners
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = SearchResults.newInstance(acSearch.getText().toString(), "1", "", "unions");
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
                    Fragment fragment = SearchResults.newInstance(acSearch.getText().toString(), "1", "");
                    ((MainActivity) context).initFragment(fragment);
                }
                return true;
            }
        });
        acSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = UnionsIndividualFragment.newInstance("" + getUnionIdByName(parent.getItemAtPosition(position).toString()), "");
                ((MainActivity) context).initFragment(fragment);
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

    //Private Functions

    private void loadUnions(){
        SqlHelper sqlHelper = new SqlHelper(context, UnionsFragment.this);
        sqlHelper.setExecutePath("getunionlist.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("unions");
        ContentValues params = new ContentValues();
        sqlHelper.setParams(params);
        sqlHelper.executeUrl(true);
    }

    private void buildUnions(JSONArray jsonArray){
        try{
            unionModels = new ArrayList<>();
            ArrayList<String> searchList = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                UnionModel unionModel = new ModelHelper().buildUnionModel(jsonArray.getJSONObject(i));
                searchList.add(unionModel.getName());
                unionModels.add(unionModel);
            }
            //Set Unions List
            UnionsAdapter adapter = new UnionsAdapter(context, unionModels, rootView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            rvUnions.setLayoutManager(layoutManager);
            rvUnions.setAdapter(adapter);

            //Set Search bar Autocomplete
            ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, searchList);
            adapterSearch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            acSearch.setAdapter(adapterSearch);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private int getUnionIdByName(String unionName){
        for(int i = 0; i < unionModels.size(); i++){
            if(unionName.toLowerCase().equals(unionModels.get(i).getName().toLowerCase())){
                return unionModels.get(i).getId();
            }
        }
        return -1;
    }
}
