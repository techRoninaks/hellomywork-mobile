package com.roninaks.hellomywork.adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.RecyclerView;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

//import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.fragments.UnionsIndividualFragment;
import com.roninaks.hellomywork.models.UnionModel;

import java.util.ArrayList;
import java.util.Random;

public class HomeUnionAdapter extends RecyclerView.Adapter<HomeUnionAdapter.ViewHolder>{
    private ArrayList<UnionModel> unionModels;
    private Context context;
    private View rootview;
//    private RequestOptions requestOptions;
    private int colorList[] = {R.color.palette_orange, R.color.palette_brown, R.color.palette_blue, R.color.palette_green};


    public HomeUnionAdapter(Context context, ArrayList<UnionModel> unionModels, View rootview) {
        this.context = context;
        this.unionModels = unionModels;
        this.rootview = rootview;
//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.profile_default);
//        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public HomeUnionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_unions_individual_listitem,parent,false);
        return new HomeUnionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeUnionAdapter.ViewHolder holder, final int position)  {
        try {
//            holder.ivIcon.setImageDrawable(context.getDrawable(R.drawable.ic_unions_black));
            holder.ivIcon.setBackground(getDrawable(position%6));
            holder.tvUnionName.setText(unionModels.get(position).getName());
            //holder.imgDefinitionImageType.setImageResource(unionModels.get(position).getType() == null || unionModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = UnionsIndividualFragment.newInstance("" + unionModels.get(position).getId(), "");
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
        return unionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        Typeface tfSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Semibold.otf");
//        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/myriadpro.otf");
        TextView tvUnionName;
        ImageView ivIcon;
        LinearLayout llMaster;
        public ViewHolder(View itemView) {
            super(itemView);
            tvUnionName = itemView.findViewById(R.id.tvUnionName);
            ivIcon = itemView.findViewById(R.id.ivUnionIcon);
            llMaster = itemView.findViewById(R.id.containerMaster);
        }
    }

    private Drawable getDrawable(int n){
//        Random rand = new Random();
//        int n = rand.nextInt(6);
        Drawable drawable = null;
        switch (n){
            case 0:{
                drawable = context.getDrawable(R.drawable.ic_union_home1);
                break;
            }
            case 1:{
                drawable = context.getDrawable(R.drawable.ic_union_home2);
                break;
            }
            case 2:{
                drawable = context.getDrawable(R.drawable.ic_union_home3);
                break;
            }
            case 3:{
                drawable = context.getDrawable(R.drawable.ic_union_home4);
                break;
            }
            case 4:{
                drawable = context.getDrawable(R.drawable.ic_union_home5);
                break;
            }
            case 5:{
                drawable = context.getDrawable(R.drawable.ic_union_home6);
                break;
            }
        }
        return drawable;
    }
}

