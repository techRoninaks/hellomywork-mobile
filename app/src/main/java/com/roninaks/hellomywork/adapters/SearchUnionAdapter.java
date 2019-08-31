package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.models.UnionModel;

import java.util.ArrayList;

public class SearchUnionAdapter extends RecyclerView.Adapter<SearchUnionAdapter.ViewHolder>{
    private ArrayList<UnionModel> unionModels;
    private Context context;
    private View rootview;
    private RequestOptions requestOptions;


    public SearchUnionAdapter(Context context, ArrayList<UnionModel> unionModels, View rootview) {
        this.context = context;
        this.unionModels = unionModels;
        this.rootview = rootview;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.profile_default);
        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public SearchUnionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_results_services,parent,false);
        return new SearchUnionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUnionAdapter.ViewHolder holder, final int position)  {
        try {
            holder.tvCategoryName.setText(unionModels.get(position).getName());
            //holder.imgDefinitionImageType.setImageResource(unionModels.get(position).getType() == null || unionModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);

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
        TextView tvCategoryName;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
