package com.roninaks.hellomywork.adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
//import androidx.core.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.fragments.ProfileFragment;
import com.roninaks.hellomywork.fragments.UnionsIndividualFragment;
import com.roninaks.hellomywork.models.ServiceProviderModel;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SearchProfileAdapter extends RecyclerView.Adapter<SearchProfileAdapter.ViewHolder>{
    private ArrayList<ServiceProviderModel> serviceProviderModels;
    private Context context;
    private View rootview;
//    private RequestOptions requestOptions;


    public SearchProfileAdapter(Context context, ArrayList<ServiceProviderModel> serviceProviderModels, View rootview) {
        this.context = context;
        this.serviceProviderModels = serviceProviderModels;
        this.rootview = rootview;
//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.profile_default);
//        requestOptions.error(R.drawable.profile_default);
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
//            holder.ivProfile.
//            holder.tvPremium.setText(serviceProviderModels.get(position).isPremium() ? context.getString(R.string.sr_profile_premium) : context.getString(R.string.sr_profile_nonpremium));
            holder.tvRole.setText(serviceProviderModels.get(position).getRole());
            holder.tvLocation.setText(serviceProviderModels.get(position).getSublocation());
            //holder.imgDefinitionImageType.setImageResource(serviceProviderModels.get(position).getType() == null || serviceProviderModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);

//            if(serviceProviderModels.get(position).getWhatsapp().isEmpty())
//                holder.ivWhatsapp.setVisibility(View.INVISIBLE);

//            if(serviceProviderModels.get(position).getEmail().isEmpty())
//                holder.ivEmail.setVisibility(View.INVISIBLE);

            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: On Clicklistener
                    Fragment fragment = ProfileFragment.newInstance(""+serviceProviderModels.get(position).getId(),"");
                    ((MainActivity) context).initFragment(fragment);
                }
            });
            View.OnClickListener actionListeners = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imgWhatsapp:{
                            if(((MainActivity) context).isLoggedIn().isEmpty() && !serviceProviderModels.get(position).getWhatsapp().isEmpty()){
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                Intent myIntent = new Intent(context, LoginActivity.class);
                                                context.startActivity(myIntent);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Oho!, You are not Logged In");
                                builder.setMessage("You need to login to make calls").setPositiveButton("Go to login?", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }else {
                                ((MainActivity) context).sendWhatsapp("+91" + serviceProviderModels.get(position).getWhatsapp());
                            }
                            break;
                        }
                        case R.id.imgPhone:{
                            if(((MainActivity) context).isLoggedIn().isEmpty()){
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                Intent myIntent = new Intent(context, LoginActivity.class);
                                                context.startActivity(myIntent);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Oho!, You are not Logged In");
                                builder.setMessage("You need to login to make calls").setPositiveButton("Go to login?", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                            else {
                                ((MainActivity) context).callPhone("+91" + serviceProviderModels.get(position).getPhone());
                            }
                            break;
                        }
                        case R.id.imgEmail:{
                            if(((MainActivity) context).isLoggedIn().isEmpty() && !serviceProviderModels.get(position).getEmail().isEmpty()) {
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                Intent myIntent = new Intent(context, LoginActivity.class);
                                                context.startActivity(myIntent);
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                                break;
                                            default:
                                                Toast.makeText(context, "Nothing", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle("Oho!, You are not Logged In");
                                builder.setMessage("You need to login to make calls").setPositiveButton("Go to login?", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }
                            else {
                                ((MainActivity) context).sendMail(serviceProviderModels.get(position).getEmail());
                            }
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
            ivQr = itemView.findViewById(R.id.imgQr);
            tvPremium = itemView.findViewById(R.id.tvPremium);
            tvName = itemView.findViewById(R.id.tvname);
            tvRole = itemView.findViewById(R.id.tvdesignation);
            tvLocation = itemView.findViewById(R.id.tvaddress);
            tvRating = itemView.findViewById(R.id.tvRating);
            llMaster = itemView.findViewById(R.id.containerMaster);
            ivProfile = itemView.findViewById(R.id.imgProfile);
        }
    }
}
