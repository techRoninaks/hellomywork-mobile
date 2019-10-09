package com.roninaks.hellomywork.adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.fragments.ProfileFragment;
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
//    private RequestOptions requestOptions;
    private int colorList[] = {R.color.palette_orange, R.color.palette_brown, R.color.palette_blue, R.color.palette_green};


    public HomeTopPerformersAdapter(Context context, ArrayList<ServiceProviderModel> serviceProviderModels, View rootview) {
        this.context = context;
        this.serviceProviderModels = serviceProviderModels;
        this.rootview = rootview;
//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.profile_default);
//        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public HomeTopPerformersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_top_performers_individual_listitem,parent,false);
        return new HomeTopPerformersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeTopPerformersAdapter.ViewHolder holder, final int position)  {
        try {
            Random rand = new Random();
            int n = rand.nextInt(4);
            holder.tvProfileName.setText(serviceProviderModels.get(position).getName());
            holder.tvRoleName.setText(serviceProviderModels.get(position).getRole());
            holder.tvRating.setText("" + StringHelper.roundFloat(serviceProviderModels.get(position).getRating(), 2));
            String image = serviceProviderModels.get(position).getImage();
            Glide.with(context)
                    .asBitmap()
                    .load(context.getString(R.string.master_url) + image)
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
                            image.eraseColor(getColor());
                            holder.ivIcon.setImageBitmap(image);
                            holder.tvProfileFirstLetter.setVisibility(View.VISIBLE);
                            holder.tvProfileFirstLetter.setText(serviceProviderModels.get(position).getName().substring(0, 1));
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.ivIcon);
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = ProfileFragment.newInstance(""+serviceProviderModels.get(position).getId(),"");
                    ((MainActivity) context).initFragment(fragment);
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
        TextView tvProfileName, tvRoleName, tvRating, tvProfileFirstLetter;
        CircleImageView ivIcon;
        LinearLayout llMaster;
        public ViewHolder(View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            tvRoleName = itemView.findViewById(R.id.tvRoleName);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvProfileFirstLetter = itemView.findViewById(R.id.tvNameFirstLetter);
            ivIcon = itemView.findViewById(R.id.civProfileImage);
            llMaster = itemView.findViewById(R.id.containerMaster);
        }
    }

    private int getColor(){
        Random rand = new Random();
        int n = rand.nextInt(4);
        int color = ResourcesCompat.getColor(context.getResources(), R.color.palette_blue, null);
        switch (n){
            case 0:{
                color = ResourcesCompat.getColor(context.getResources(), R.color.palette_blue, null);
                break;
            }
            case 1:{
                color = ResourcesCompat.getColor(context.getResources(), R.color.palette_orange, null);
                break;
            }
            case 2:{
                color = ResourcesCompat.getColor(context.getResources(), R.color.palette_brown, null);
                break;
            }
            case 3:{
                color = ResourcesCompat.getColor(context.getResources(), R.color.palette_green, null);
                break;
            }
        }
        return color;
    }
}
