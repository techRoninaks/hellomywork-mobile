package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.TopPerformerModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TopPerformerListAdapter extends RecyclerView.Adapter<TopPerformerListAdapter.ViewHolder>{

    private ArrayList<TopPerformerModel> topPerformerModels;
    private Context context;
    private View rootview;

    public TopPerformerListAdapter(Context context, ArrayList<TopPerformerModel> topPerformerModels, View rootview) {
        this.context = context;
        this.rootview = rootview;
        this.topPerformerModels = topPerformerModels;
    }

    @NonNull
    @Override
    public TopPerformerListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_performer_list,parent,false);
        return new TopPerformerListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopPerformerListAdapter.ViewHolder holder, int position) {
        try {
            String topPerformerId = topPerformerModels.get(position).getId();
            String topPerformerUserName = topPerformerModels.get(position).getUsesrName();
            String topPerformerConversions = topPerformerModels.get(position).getUserConversions();
            holder.tvTopPerformersId.setText(topPerformerId);
            holder.tvTopPerformersName.setText(topPerformerUserName);
            holder.tvTopPerformersConversions.setText(topPerformerConversions + " Conversions");
            if(position == 0) {
                holder.llBackground.setBackgroundResource(R.drawable.list_bg);
                holder.tvTopPerformersId.setTextColor(ContextCompat.getColor(context, R.color.colorTextWhitePrimary));
                holder.tvTopPerformersName.setTextColor(ContextCompat.getColor(context, R.color.colorTextWhitePrimary));
                holder.tvTopPerformersConversions.setTextColor(ContextCompat.getColor(context, R.color.colorTextWhitePrimary));
            }
            if(position == 4) {
                holder.tvTopPerformersName.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.tvTopPerformersId.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                holder.tvTopPerformersConversions.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
            }

        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return topPerformerModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTopPerformersName,tvTopPerformersConversions,tvTopPerformersId;
        LinearLayout llListItem,llBackground;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTopPerformersId = itemView.findViewById(R.id.list_top_performer_no);
            tvTopPerformersName = itemView.findViewById(R.id.list_top_performer_name);
            tvTopPerformersConversions = itemView.findViewById(R.id.list_top_performer_conversions);
//            llListItem = itemView.findViewById(R.id.ll_list_top_performer);
            llBackground = itemView.findViewById(R.id.ll_list_top_performer);
        }
    }
}
