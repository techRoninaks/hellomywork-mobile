package com.roninaks.hellomywork.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.AdminActivity;
import com.roninaks.hellomywork.adapters.AnnouncementsAdapter;
import com.roninaks.hellomywork.adapters.TopPerformerListAdapter;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.AnnouncementsModel;
import com.roninaks.hellomywork.models.TopPerformerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment implements SqlDelegate {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // constants for relative time
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    private String empId;
    private String empName;
    
    private View rootView;
    private Context context;
    private RecyclerView recyclerViewTopPerformers,recyclerViewAnnouncements;
    private ArrayList<TopPerformerModel> topPerformerModels = new ArrayList<TopPerformerModel>();
    private ArrayList<AnnouncementsModel> announcementsModels = new ArrayList<AnnouncementsModel>();
    private TopPerformerListAdapter topPerformerListAdapter;
    private AnnouncementsAdapter announcementsAdapter;
    private String firstName,lastName,userName,userRole,targetLeads,targetProspective,targetConversions,currentLead,currentProspetive,currentConversions;
    private TextView tvName,tvRole,progressLeads,progressProspective,progressConversions;

    private ProgressBar progressBarProspective,progressBarLeads,progressBarConversions;
    private OnFragmentInteractionListener mListener;
    private ImageView ivPopupAdmin;
    private LinearLayout llBackground;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */

    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
            empId = getArguments().getString(ARG_PARAM1);
            empName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        // Get logged in user
        empId = ((AdminActivity) context).isLoggedIn();
        if(empId.isEmpty()){
            getActivity().getSupportFragmentManager().popBackStack("Dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Fragment fragment = AdminLogin.newInstance("", "");
            ((AdminActivity) context).initFragment(fragment);
        }else {
            getUserDetails(context);
        }
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerViewTopPerformers = rootView.findViewById(R.id.recyclerView_topPerformers);
        recyclerViewAnnouncements = rootView.findViewById(R.id.recyclerView_announcemnents);
        tvName = rootView.findViewById(R.id.userNameAdmin);
        tvRole = rootView.findViewById(R.id.userJobAdmin);

        progressLeads = rootView.findViewById(R.id.leads_progress);
        progressProspective = rootView.findViewById(R.id.prospective_progress);
        progressConversions = rootView.findViewById(R.id.conversions_progress);

        // Populate recycler view using fetch data
        getTopPerformers(context);
        getAnnouncements(context);
        getPieData(context);

        llBackground = rootView.findViewById(R.id.llwhitebg);
        progressBarProspective = rootView.findViewById(R.id.stats_progressbarProspective);
        progressBarLeads = rootView.findViewById(R.id.stats_progressbarLeads);
        progressBarConversions = rootView.findViewById(R.id.stats_progressbarConversions);

        ivPopupAdmin = rootView.findViewById(R.id.admin_dashboard_menu);
        ivPopupAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                Menu m = popup.getMenu();
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.option_menu_admin, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.options_home:{
                                ((AdminActivity) context).finish();
                                break;
                            }
                            case R.id.options_logout_admin:{
                                int count = ((AdminActivity) context).getSupportFragmentManager().getBackStackEntryCount();
                                if (count > 0) {
                                    ((AdminActivity) context).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }
                                ((AdminActivity) context).logoutAdmin();
                                break;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        //int leadPercentage = getProgressPercentage(currentProspetive,targetProspective);
        //Set progress using fetch data


        // Code for animated Pie view configurations


        return rootView;
    }

    private double getProgressPercentage(String currentValues, String targetValue) {
        double percentage = 0;
        try {
            int current = Integer.parseInt(currentValues);
            int target = Integer.parseInt(targetValue);
            percentage = ((double)current/target)*100;
        }
        catch (Exception e){}

        return percentage;
    }

    private void getPieData(Context context) {

        SqlHelper sqlHelper = new SqlHelper(context, DashboardFragment.this);
        sqlHelper.setMasterUrl(context.getString(R.string.master_url));
        sqlHelper.setExecutePath(context.getString(R.string.driver_path_admin) + "getPieData.php");
        sqlHelper.setActionString("pie_data");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("userId", empId);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(false);
    }

    private void getUserDetails(Context context) {

        SqlHelper sqlHelper = new SqlHelper(context, DashboardFragment.this);
        sqlHelper.setMasterUrl(context.getString(R.string.master_url));
        sqlHelper.setExecutePath(context.getString(R.string.driver_path_admin) + "fetchUser.php");
        sqlHelper.setActionString("get_user");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", empId);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(false);
    }

    private void getTopPerformers(Context context) {

        SqlHelper sqlHelper = new SqlHelper(context, DashboardFragment.this);
        sqlHelper.setMasterUrl(context.getString(R.string.master_url));
        sqlHelper.setExecutePath(context.getString(R.string.driver_path_admin) + "getConversions.php");
        sqlHelper.setActionString("topPerformers");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", empId);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(false);
    }

    private void getAnnouncements(Context context) {

        SqlHelper sqlHelper = new SqlHelper(context, DashboardFragment.this);
        sqlHelper.setMasterUrl(context.getString(R.string.master_url));
        sqlHelper.setExecutePath(context.getString(R.string.driver_path_admin) + "getNotifications.php");
        sqlHelper.setActionString("announcements");
        sqlHelper.setMethod("POST");
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", empId);
        sqlHelper.setParams(contentValues);
        sqlHelper.executeUrl(false);
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


    private void initRecyclerViewTopPerformers(JSONArray jsonArray, int length) {

        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 0; i < length; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TopPerformerModel topPerformerModel = modelHelper.buildTopPerformerModel(jsonObject);
                topPerformerModels.add(topPerformerModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewTopPerformers.setLayoutManager(layoutManager);
        topPerformerListAdapter = new TopPerformerListAdapter(context, topPerformerModels,rootView);
        recyclerViewTopPerformers.setAdapter(topPerformerListAdapter);
    }

    private void initRecyclerViewAnnouncements(JSONArray jsonArray, int length) {

        ModelHelper modelHelper = new ModelHelper(this.context);
        for (int i = 0; i < length; i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AnnouncementsModel announcementsModel = modelHelper.buildAnnouncementModel(jsonObject);
                announcementsModels.add(announcementsModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewAnnouncements.setLayoutManager(layoutManager);
        announcementsAdapter = new AnnouncementsAdapter(context, announcementsModels,rootView);
        recyclerViewAnnouncements.setAdapter(announcementsAdapter);
    }


    @Override
    public void onResponse(SqlHelper sqlHelper) {
        String response = sqlHelper.getStringResponse();
        String success = "";
        JSONObject jsonObjectresponse = sqlHelper.getJSONResponse();
        if(sqlHelper.getActionString() =="topPerformers"){
            try {
                String jsonObjectData = jsonObjectresponse.getString("data");
                JSONObject jsonData = new JSONObject(jsonObjectData);
                success = jsonData.getString("success");
                //success = jsonObjectresponse.getString("success");
                //String suc = response.getString("data");
                if(success.equals("successful")){
                    String result = jsonData.getString("result");
                    JSONArray jsonArray = new JSONArray(result);
                    int length = (jsonArray.length());
                    initRecyclerViewTopPerformers(jsonArray, length);
                }
                else {
                    //show error
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
        else if(sqlHelper.getActionString() =="announcements"){
            try {
                String jsonObjectData = jsonObjectresponse.getString("data");
                JSONObject jsonData = new JSONObject(jsonObjectData);
                success = jsonData.getString("success");

                if(success.equals("successful")){
                    String result = jsonData.getString("result");
                    JSONArray jsonArray = new JSONArray(result);
                    int length = (jsonArray.length());
                    initRecyclerViewAnnouncements(jsonArray, length);
                }
                else {
                    //show error
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
        else if(sqlHelper.getActionString().equals("get_user")){
            try {
                JSONObject jsonObject = sqlHelper.getJSONResponse();
                if(jsonObject.getString("fName").isEmpty()){
                    //show error
                }
                else {
                    String fName = jsonObject.getString("fName");
                    String lName = jsonObject.getString("lName");
                    String userName = jsonObject.getString("userName");
                    String userRole = jsonObject.getString("roleName");
                    setUser(fName,lName,userName,userRole);

                }

            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

        else if(sqlHelper.getActionString().equals("pie_data")){
            try {
                String jsonObjectData = jsonObjectresponse.getString("data");
                JSONObject jsonData = new JSONObject(jsonObjectData);
                success = jsonData.getString("success");
                if(success.equals("successful")){
                    JSONObject jsonResult = new JSONObject(jsonData.getString("result"));
                    targetLeads = jsonResult.getString("LEADS_TARGET");
                    targetProspective = jsonResult.getString("PROSPECT_TARGET");
                    targetConversions = jsonResult.getString("CONVERSIONS_TARGET");

                    currentLead = jsonResult.getString("LEADS_FINAL");
                    currentProspetive = jsonResult.getString("PROSPECT_FINAL");
                    currentConversions = jsonResult.getString("CONVERSIONS");

                    setProgressData(currentLead,targetLeads,currentProspetive,targetProspective,currentConversions,targetConversions);
                }
                else {
                    llBackground.setVisibility(View.INVISIBLE);
                    targetLeads = "0";
                    targetProspective = "0";
                    targetConversions = "0";

                    currentLead = "0";
                    currentProspetive = "0";
                    currentConversions = "0";

                    setProgressData(currentLead,targetLeads,currentProspetive,targetProspective,currentConversions,targetConversions);
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Network error try again later", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }

    }

    private void setProgressData(String currentLead,String targetLeads,String currentProspetive,String targetProspective,String currentConversions,String targetConversions) {

        // Find progress percentage
        double leadPercentage = getProgressPercentage(currentLead,targetLeads);
        double prospectivePercentage = getProgressPercentage(currentProspetive,targetProspective);
        double conversionPercentage = getProgressPercentage(currentConversions,targetConversions);

        // Set progress
        progressBarProspective.setProgress((int) leadPercentage);
        progressBarLeads.setProgress((int) prospectivePercentage);
        progressBarConversions.setProgress((int) conversionPercentage);

        // Set pie chart
        AnimatedPieView mAnimatedPieView = rootView.findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset
                .addData(new SimplePieInfo(Integer.parseInt(currentLead), getResources().getColor(R.color.graph_green),"Green" ))
                .addData(new SimplePieInfo(Integer.parseInt(currentProspetive), getResources().getColor(R.color.graph_red), "Red"))
                .addData(new SimplePieInfo(Integer.parseInt(currentConversions), getResources().getColor(R.color.graph_yellow), "Yellow"))
                .strokeMode(false)
                .pieRadius(110)
                .duration(800);
        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();

        // Set progress text
        progressLeads.setText(currentLead+"/"+targetLeads);
        progressProspective.setText(currentProspetive+"/"+targetProspective);
        progressConversions.setText(currentConversions+"/"+targetConversions);

    }

    private void setUser(String fName,String lName,String user,String role){
        firstName = fName;
        lastName = lName;
        userName = user;
        userRole = role;
        String name = firstName + " " + lastName;
        tvName.setText(name);
        tvRole.setText(userRole);
    }

    // function to return relative time w.r.t to current system time
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
