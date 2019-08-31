package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.StringHelper;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeTopPerformersAdapter extends RecyclerView.Adapter<HomeTopPerformersAdapter.ViewHolder>{
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private Context context;
    private View rootview;
    private RequestOptions requestOptions;
    private int colorList[] = {R.color.palette_orange, R.color.palette_brown, R.color.palette_blue, R.color.palette_green};


    public HomeTopPerformersAdapter(Context context, ArrayList<ServiceProviderModel> serviceProviderModels, View rootview) {
        this.context = context;
        this.serviceProviderModels = serviceProviderModels;
        this.rootview = rootview;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.profile_default);
        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public HomeTopPerformersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_top_performers_individual_listitem,parent,false);
        return new HomeTopPerformersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeTopPerformersAdapter.ViewHolder holder, final int position)  {
        try {
            Random rand = new Random();
            int n = rand.nextInt(4);
            holder.tvProfileName.setText(serviceProviderModels.get(position).getName());
            holder.tvRoleName.setText(serviceProviderModels.get(position).getRole());
            holder.tvRating.setText("" + StringHelper.roundFloat(serviceProviderModels.get(position).getRating(), 2));
            //holder.imgDefinitionImageType.setImageResource(serviceProviderModels.get(position).getType() == null || serviceProviderModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ((MainActivity) context).initFragment();
                }
            });

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }

    }


    @Override
    public int getItemCount() {
        return serviceProviderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        Typeface tfSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Semibold.otf");
//        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/myriadpro.otf");
        TextView tvProfileName, tvRoleName, tvRating;
        CircleImageView ivIcon;
        LinearLayout llMaster;
        public ViewHolder(View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvRoleName = itemView.findViewById(R.id.tvRoleName);
            tvRating = itemView.findViewById(R.id.tvRating);
            ivIcon = itemView.findViewById(R.id.civProfileImage);
            llMaster = itemView.findViewById(R.id.containerMaster);
        }
    }
}
