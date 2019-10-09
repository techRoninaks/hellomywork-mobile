package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.models.AnnouncementsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.ViewHolder> {

    private ArrayList<AnnouncementsModel> announcementsModels;
    private Context context;
    private View rootview;

    public AnnouncementsAdapter(Context context, ArrayList<AnnouncementsModel> announcementsModels, View rootview) {
        this.context = context;
        this.rootview = rootview;
        this.announcementsModels = announcementsModels;
    }

    @NonNull
    @Override
    public AnnouncementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.announcments_list_item,parent,false);
        return new AnnouncementsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementsAdapter.ViewHolder holder, int position) {
        try {
            String announcementDate = announcementsModels.get(position).getAnnouncementDate();
            String announcementMessage = announcementsModels.get(position).getAnnouncementMessage();
            String announcementTime = announcementsModels.get(position).getGetAnnouncementTime();

            holder.tvAnnouncementDate.setText(getDay(announcementDate));
            holder.tvAnnouncementMessage.setText(announcementMessage);
            holder.tvAnnouncementTime.setText(formatTime(announcementTime));
        }catch (Exception e){
        }
    }

    @Override
    public int getItemCount() {
        return announcementsModels.size();
    }

    private String getDay(String announcementDate){
        String day = "";
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String today = df.format(c);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = df.format(cal.getTime());
        if(announcementDate.equals(today)){
            day = "Today";
        }
        else if(announcementDate.equals(yesterday)) day = "Yesterday";
        else day = "Earlier";

        return day;
    }

    private String formatTime(String announcementTime){
        SimpleDateFormat time1 = new SimpleDateFormat("hh:mm:ss");
        Date date;
        try {
            date = time1.parse(announcementTime);
        }catch (Exception e){
            date = Calendar.getInstance().getTime();
        }

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm aa");
        String time = dateFormat2.format(date);

        return time;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAnnouncementDate,tvAnnouncementMessage,tvAnnouncementTime;
        LinearLayout llAnnouncementItem;
        public ViewHolder(View itemView) {
            super(itemView);
            tvAnnouncementDate = itemView.findViewById(R.id.list_announcement_date);
            tvAnnouncementMessage = itemView.findViewById(R.id.list_announcement_text);
            tvAnnouncementTime = itemView.findViewById(R.id.list_announcement_time);
            llAnnouncementItem = itemView.findViewById(R.id.list_announcement);
        }
    }
}
