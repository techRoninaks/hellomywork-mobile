package com.roninaks.hellomywork.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.models.ProfilePostModel;

import java.util.ArrayList;
import java.util.Random;

public class ActivityFeedAdapter extends RecyclerView.Adapter<ActivityFeedAdapter.ViewHolder>{
        private Context context;
        private View rootview;
        private ArrayList<ProfilePostModel> profilePostModels;
        private RequestOptions requestOptions;
        private String baseImagePostUrl;
//        private int colorList[] = {R.color.palette_orange, R.color.palette_brown, R.color.palette_blue, R.color.palette_green};


        public ActivityFeedAdapter(Context context, ArrayList<ProfilePostModel> profilePostModels, View rootview) {
            this.context = context;
            this.profilePostModels = profilePostModels;
            this.rootview = rootview;
            baseImagePostUrl = "https://www.hellomywork.com/";
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_image);
            requestOptions.error(R.drawable.icon_image);
        }


        @NonNull
        @Override
        public ActivityFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.profile_posts_item,parent,false);
            return new ActivityFeedAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityFeedAdapter.ViewHolder holder, final int position)  {
            try {
                holder.tvPostProfileName.setText(profilePostModels.get(position).getName());
                holder.tvPostProfileDes.setText(profilePostModels.get(position).getDescription());
                holder.tvPostProfileLocation.setText(profilePostModels.get(position).getLocation());
                holder.tvPostProfileDate.setText(profilePostModels.get(position).getDate());
                holder.tvPostProfileTime.setText(profilePostModels.get(position).getTime());
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions
                            .centerCrop()
                            )
                        .asBitmap()
                        .load(baseImagePostUrl + profilePostModels.get(position).getImageUri())
                        .into(holder.ivPostProfileImage);
            }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
            }

        }


        @Override
        public int getItemCount() {
            return profilePostModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvPostProfileName, tvPostProfileDes, tvPostProfileLocation, tvPostProfileDate, tvPostProfileTime;
            ImageView ivPostProfileImage;
            ImageButton btnProfilePostShare;
            public ViewHolder(View itemView) {
                super(itemView);
                tvPostProfileName = itemView.findViewById(R.id.post_item_name);
                tvPostProfileDes = itemView.findViewById(R.id.post_item_descprition);
                tvPostProfileLocation = itemView.findViewById(R.id.post_item_loaction);
                tvPostProfileDate = itemView.findViewById(R.id.post_item_date);
                tvPostProfileTime = itemView.findViewById(R.id.post_item_time);
                ivPostProfileImage = itemView.findViewById(R.id.post_item_post_image);
                btnProfilePostShare = itemView.findViewById(R.id.post_item_shareBTN);
            }
        }
    }
