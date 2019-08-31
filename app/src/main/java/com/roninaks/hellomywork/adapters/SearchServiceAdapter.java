package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.models.CategoryModel;

import java.util.ArrayList;

public class SearchServiceAdapter extends RecyclerView.Adapter<SearchServiceAdapter.ViewHolder>{
    private ArrayList<CategoryModel> categoryModels;
    private Context context;
    private View rootview;
    private RequestOptions requestOptions;


    public SearchServiceAdapter(Context context, ArrayList<CategoryModel> categoryModels, View rootview) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.rootview = rootview;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.profile_default);
        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public SearchServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_results_services,parent,false);
        return new SearchServiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchServiceAdapter.ViewHolder holder, final int position)  {
        try {
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(context.getDrawable(R.drawable.ic_help))
                    .into(holder.ivIcon);
            holder.tvCategoryName.setText(categoryModels.get(position).getName());
            //holder.imgDefinitionImageType.setImageResource(categoryModels.get(position).getType() == null || categoryModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }

    }


    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        Typeface tfSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Semibold.otf");
//        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/myriadpro.otf");
        TextView tvCategoryName;
        ImageView ivIcon;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
        }
    }
}
