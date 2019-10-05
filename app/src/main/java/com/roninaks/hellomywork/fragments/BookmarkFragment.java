package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.roninaks.hellomywork.activities.AdminActivity;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.RegisterActivity;
import com.roninaks.hellomywork.adapters.PostBookmarkAdapter;
import com.roninaks.hellomywork.adapters.SearchProfileAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.CommentsModel;
import com.roninaks.hellomywork.models.ProfilePostModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;
import com.roninaks.hellomywork.models.UnionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment implements SqlDelegate{

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String BOOKMARK = "bookmark";
    private static final int SEARCH_RESULT_LIMIT = 24;


    private String mParam1;
    private String mParam2;
    private String userId;
    private String default_load;
    private ArrayList<CategoryModel> categoryModels;
    private ArrayList<UnionModel> unionModels;
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private ArrayList<ProfilePostModel> profilePostModels;
    private Context context;
    LinearLayout bmcontainerProfile, bmcontainerPosts;
    private SwipeRefreshLayout swipeContainerProfile,swipeContainerPost;


    View rootView,bmporfilenav,bmpostnav;
    TextView bmhead, bmprofiles, bmposts;
    private ImageView backarrow, imgOptions;
    LinearLayout LlContainerProfile, llContainerPost;
    RecyclerView rvProfiles, rvPosts;
    Handler handler;


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
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent myIntent = new Intent(context, LoginActivity.class);
                            context.startActivity(myIntent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            Fragment fragment = HomeFragment.newInstance("", "");
                            ((MainActivity) context).initFragment(fragment);
                            break;
                        default:
                            Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Oho!, You are not Logged In");
            builder.setMessage("You need to login to view this page").setPositiveButton("Go to login?", dialogClickListener)
                    .setNegativeButton("Go Home", dialogClickListener).show();
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

        swipeContainerProfile = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeContainerPost = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_containerPosts);
        handler = new Handler();

        //Defaults
        toggleTabs(default_load == null || default_load.isEmpty() ? "profiles" : default_load);

        imgOptions.setOnClickListener(new View.OnClickListener() {
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

        View.OnClickListener toggleTabsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bmprofiles: {
                        toggleTabs("profiles");
                        break;
                    }
                    case R.id.bmposts: {
                        toggleTabs("posts");
                        break;
                    }
                }
            }
        };
        bmprofiles.setOnClickListener(toggleTabsListener);
        bmposts.setOnClickListener(toggleTabsListener);

        // option popup

        swipeContainerProfile.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadProfiles();
            }
        });
        swipeContainerPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPosts();
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
                case "posts":{
                    swipeContainerPost.setRefreshing(false);
                    JSONArray jsonArray = new JSONArray(sqlHelper.getStringResponse());
                    String response = jsonArray.getJSONObject(0).getString("count");
                    if(response.equals("")){
                        Toast.makeText(context, "Sorry. Your Union list seems empty", Toast.LENGTH_SHORT).show();
                    }else{
                        buildPosts(jsonArray);
                    }
                    break;
                }
                case "profiles":{
                    swipeContainerProfile.setRefreshing(false);
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
        sqlHelper.setExecutePath("getbookmarks.php");
        sqlHelper.setMethod("GET");
        sqlHelper.setActionString("posts");
        ContentValues params = new ContentValues();
//        params.put("srchType", searchKey);
//        params.put("locType", location);
//        params.put("catType", category);
//        params.put("pageNo", "1");
//        params.put("mob", "1");
        params.put("type", "posts");
        params.put("userId", ((MainActivity) context).isLoggedIn());
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

                swipeContainerProfile.setVisibility(View.VISIBLE);
                swipeContainerPost.setVisibility(View.GONE);
                break;
            }
            case "posts":{
                if(profilePostModels == null){
                    loadPosts();
                }
                bmposts.setTextColor(getResources().getColor(R.color.colorTextBlackPrimary));
                bmprofiles.setTextColor(getResources().getColor(R.color.colorTextBlackHint));

                bmpostnav.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                bmporfilenav.setBackgroundColor(getResources().getColor(R.color.colorTextWhiteSecondary));

                swipeContainerProfile.setVisibility(View.GONE);
                swipeContainerPost.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    private void buildProfiles(JSONArray jsonArray){
        try{
            serviceProviderModels = new ArrayList<>();
            boolean isProfileBookmark = true;
            for(int i = 1; i < jsonArray.length(); i++){
                ServiceProviderModel serviceProviderModel = new ModelHelper().buildServiceProviderModel(jsonArray.getJSONObject(i), "search_profiles");
                serviceProviderModels.add(serviceProviderModel);
            }
            GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
            SearchProfileAdapter adapter = new SearchProfileAdapter(context, serviceProviderModels, rootView,rvProfiles,true);
            rvProfiles.setLayoutManager(layoutManager);
            rvProfiles.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buildPosts(JSONArray jsonArray){
        try{
            profilePostModels = new ArrayList<>();
            for(int i = 1; i < jsonArray.length(); i++){
                ProfilePostModel profilePostModel = new ModelHelper().buildProfilePostBookmarkModel(jsonArray.getJSONObject(i));
                //profilePostModel.setCommentsModels(getCommentList(jsonArray.getJSONObject(i)));
                profilePostModels.add(profilePostModel);
            }
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
            PostBookmarkAdapter adapter = new PostBookmarkAdapter(context,profilePostModels,rootView,rvPosts);

            rvPosts.setLayoutManager(gridLayoutManager);
            rvPosts.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void addPost() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        final PostBookmarkAdapter adapter = new PostBookmarkAdapter(context,profilePostModels,rootView,rvPosts);

        rvProfiles.setLayoutManager(gridLayoutManager);
        rvProfiles.setAdapter(adapter);
//        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        int index = (int)Math.ceil(((double)categoryModels.size() / SEARCH_RESULT_LIMIT) )+ 1;
//                        loadPosts(String.valueOf(index));
//                        adapter.notifyDataSetChanged();
//                        adapter.setLoaded();
//                    }
//                },1000);
//            }
//        });
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



}
