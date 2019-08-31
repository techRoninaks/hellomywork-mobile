package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.models.CategoryModel;
import com.roninaks.hellomywork.models.ServiceProviderModel;

import java.util.ArrayList;

public class SearchProfileAdapter extends RecyclerView.Adapter<SearchProfileAdapter.ViewHolder>{
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private Context context;
    private View rootview;
    private RequestOptions requestOptions;


    public SearchProfileAdapter(Context context, ArrayList<ServiceProviderModel> serviceProviderModels, View rootview) {
        this.context = context;
        this.serviceProviderModels = serviceProviderModels;
        this.rootview = rootview;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.profile_default);
        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public SearchProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_results_profile,parent,false);
        return new SearchProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProfileAdapter.ViewHolder holder, final int position)  {
        try {
//            Glide.with(context)
//                    .setDefaultRequestOptions(requestOptions)
//                    .asBitmap()
//                    .load(context.getDrawable(R.drawable.ic_help))
//                    .into(holder.ivIcon);
            holder.tvName.setText(serviceProviderModels.get(position).getName());
            holder.tvPremium.setText(serviceProviderModels.get(position).isPremium() ? context.getString(R.string.sr_profile_premium) : context.getString(R.string.sr_profile_nonpremium));
            holder.tvRole.setText(serviceProviderModels.get(position).getRole());
            //holder.imgDefinitionImageType.setImageResource(serviceProviderModels.get(position).getType() == null || serviceProviderModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);
            if(serviceProviderModels.get(position).getWhatsapp().isEmpty())
                holder.ivWhatsapp.setVisibility(View.INVISIBLE);
            if(serviceProviderModels.get(position).getEmail().isEmpty())
                holder.ivEmail.setVisibility(View.INVISIBLE);
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: On Clicklistener
//                    Fragment fragment = ;
//                    ((MainActivity) context).initFragment(fragment);

                }
            });
            View.OnClickListener actionListeners = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imgWhatsapp:{
                            ((MainActivity) context).sendWhatsapp("+91" + serviceProviderModels.get(position).getWhatsapp());
                            break;
                        }
                        case R.id.imgPhone:{
                            ((MainActivity) context).callPhone("+91" + serviceProviderModels.get(position).getPhone());
                            break;
                        }
                        case R.id.imgEmail:{
                            ((MainActivity) context).sendMail(serviceProviderModels.get(position).getEmail());
                            break;
                        }
                    }
                }
            };
            holder.ivWhatsapp.setOnClickListener(actionListeners);
            holder.ivEmail.setOnClickListener(actionListeners);
            holder.ivPhone.setOnClickListener(actionListeners);
            holder.ivBookmark.setOnClickListener(actionListeners);
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
        TextView tvPremium, tvName, tvRole, tvLocation, tvRating;
        ImageView ivProfile, ivQr, ivWhatsapp, ivPhone, ivEmail, ivBookmark;
        LinearLayout llMaster;
        public ViewHolder(View itemView) {
            super(itemView);
            ivWhatsapp = itemView.findViewById(R.id.imgWhatsapp);
            ivPhone = itemView.findViewById(R.id.imgPhone);
            ivEmail = itemView.findViewById(R.id.imgEmail);
            ivBookmark = itemView.findViewById(R.id.imgBookmark);
            tvPremium = itemView.findViewById(R.id.tvPremium);
            tvName = itemView.findViewById(R.id.tvName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvRating = itemView.findViewById(R.id.tvRating);
            llMaster = itemView.findViewById(R.id.containerMaster);
//            ivProfile = itemView.findViewById(R.id.imgProfile);
        }
    }
}
