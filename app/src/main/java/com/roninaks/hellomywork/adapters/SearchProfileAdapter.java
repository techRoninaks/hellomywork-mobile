package com.roninaks.hellomywork.adapters;

import android.app.Activity;
import android.content.ContentValues;
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
import com.roninaks.hellomywork.fragments.HomeFragment;
import com.roninaks.hellomywork.fragments.ProfileFragment;
import com.roninaks.hellomywork.fragments.UnionsIndividualFragment;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.CallbackDelegate;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.ServiceProviderModel;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class SearchProfileAdapter extends RecyclerView.Adapter<SearchProfileAdapter.ViewHolder> implements SqlDelegate {
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
            holder.ivBookmark.setImageDrawable(serviceProviderModels.get(position).isBookmarked() ?
                    context.getDrawable(R.drawable.ic_bookmarkfill_idcard) : context.getDrawable(R.drawable.ic_bookmarkpost));
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = ProfileFragment.newInstance(""+serviceProviderModels.get(position).getId(),"");
                    ((MainActivity) context).initFragment(fragment);
                }
            });
            View.OnClickListener actionListeners = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.imgWhatsapp:{
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
                                builder.setMessage("You need to login to send whatsapp messages").setPositiveButton("Go to login?", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }else if(serviceProviderModels.get(position).getWhatsapp().isEmpty()){
                                Toast.makeText(context, "Sorry. No number available.", Toast.LENGTH_SHORT).show();
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
                            }else if(serviceProviderModels.get(position).getPhone().isEmpty()){
                                Toast.makeText(context, "Sorry. No number available.", Toast.LENGTH_SHORT).show();
                            }else {
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
                                builder.setMessage("You need to login to send emails").setPositiveButton("Go to login?", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }else if(serviceProviderModels.get(position).getEmail().isEmpty()){
                                Toast.makeText(context, "Sorry. No information available.", Toast.LENGTH_SHORT).show();
                            }else {
                                ((MainActivity) context).sendMail(serviceProviderModels.get(position).getEmail());
                            }
                            break;
                        }
                        case R.id.imgBookmark:{
                            if(((MainActivity) context).isLoggedIn().isEmpty()) {
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
                                builder.setMessage("You need to login to bookmark a profile").setPositiveButton("Go to login?", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();
                            }else {
                                updateBookmarks(position);
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
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public int getItemCount() {
        return serviceProviderModels.size();
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        try {
            switch (sqlHelper.getActionString()) {
                case "bookmark": {
                    if(sqlHelper.getStringResponse().equals("0")){
                        Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
                        updateProfile(Integer.parseInt(sqlHelper.getExtras().get("position")));
                    }
                    break;
                }
            }
        }catch (Exception e){
            Toast.makeText(context, context.getString(R.string.unexpected), Toast.LENGTH_SHORT).show();
        }
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

    private void updateBookmarks(int position){
        SqlHelper sqlHelper = new SqlHelper(context, SearchProfileAdapter.this);
        sqlHelper.setExecutePath("updatebookmarks.php");
        sqlHelper.setMethod("POST");
        sqlHelper.setActionString("bookmark");
        ContentValues params = new ContentValues();
        params.put("userId", ((MainActivity) context).isLoggedIn());
        params.put("type", "profiles");
        params.put("mapping_id", "" + serviceProviderModels.get(position).getId());
        params.put("is_active", serviceProviderModels.get(position).isBookmarked() ? "1" : "0");
        sqlHelper.setParams(params);
        HashMap<String, String> extras = new HashMap<>();
        extras.put("position", "" + position);
        sqlHelper.setExtras(extras);
        sqlHelper.executeUrl(false);
        updateProfile(position);
    }

    private void updateProfile(int position){
        ServiceProviderModel serviceProviderModel = serviceProviderModels.get(position);
        serviceProviderModel.setBookmarked(!serviceProviderModel.isBookmarked());
        notifyItemChanged(position);
    }
}
