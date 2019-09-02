package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.graphics.Typeface;
//import android.support.annotation.NonNull;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.fragments.SearchLanding;

import java.util.ArrayList;

public class SearchLandingTabsAdapter extends RecyclerView.Adapter<SearchLandingTabsAdapter.ViewHolder>{
    private ArrayList<String> tabs;
    private SearchLanding fragment;
    private Context context;
    private View rootview;
//    private RequestOptions requestOptions;
    private int currentPosition;


    public SearchLandingTabsAdapter(Context context, SearchLanding fragment, ArrayList<String> tabs, View rootview) {
        this.context = context;
        this.fragment = fragment;
        this.tabs = tabs;
        this.rootview = rootview;
//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.profile_default);
//        requestOptions.error(R.drawable.profile_default);
        currentPosition = 0;
    }


    @NonNull
    @Override
    public SearchLandingTabsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_results_tabs_individual_listitem,parent,false);
        return new SearchLandingTabsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchLandingTabsAdapter.ViewHolder holder, final int position)  {
        try {
            if(position == 0){
                holder.tvTabName.setTypeface(holder.tvTabName.getTypeface(), Typeface.BOLD);
            }
            holder.tvTabName.setText(tabs.get(position));
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.onTabClick(position, tabs, currentPosition);
                    currentPosition = position;

                }
            });

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }

    }


    @Override
    public int getItemCount() {
        return tabs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        Typeface tfSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Semibold.otf");
//        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/myriadpro.otf");
        TextView tvTabName;
        LinearLayout llMaster;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTabName = itemView.findViewById(R.id.tvTabName);
            llMaster = itemView.findViewById(R.id.containerMaster);
        }
    }

}
