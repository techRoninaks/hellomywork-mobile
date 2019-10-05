package com.roninaks.hellomywork.adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.fragments.ProfileFragment;
import com.roninaks.hellomywork.fragments.SearchResults;
import com.roninaks.hellomywork.interfaces.OnLoadMoreListener;
import com.roninaks.hellomywork.models.CategoryModel;

import java.util.ArrayList;

public class SearchServiceAdapter extends RecyclerView.Adapter<SearchServiceAdapter.ViewHolder>{
    private ArrayList<CategoryModel> categoryModels;
    private Context context;
    private View rootview;
    private RequestOptions requestOptions;
    private RecyclerView recyclerView;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public SearchServiceAdapter(Context context, ArrayList<CategoryModel> categoryModels, View rootview,RecyclerView recyclerView) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.rootview = rootview;
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.profile_default);
        requestOptions.error(R.drawable.profile_default);
        this.recyclerView = recyclerView;
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = getItemCount();
                    lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (!loading && (totalItemCount - lastVisibleItem-1)==0) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
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
//            Glide.with(context)
//                    .setDefaultRequestOptions(requestOptions)
//                    .asBitmap()
//                    .load(context.getDrawable(R.drawable.ic_help))
//                    .into(holder.ivIcon);


            String imgString = categoryModels.get(position).getIcon().substring(22).replace("-min.png", "");
            int imgResource = context.getResources().getIdentifier(imgString, "drawable",
                    context.getPackageName());
            holder.ivIcon.setImageDrawable(context.getDrawable(imgResource));
            holder.tvCategoryName.setText(categoryModels.get(position).getName());
            holder.llMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = SearchResults.newInstance("","",""+categoryModels.get(position).getId(),"profiles");
                    ((MainActivity) context).initFragment(fragment);
                }
            });
            //holder.imgDefinitionImageType.setImageResource(categoryModels.get(position).getType() == null || categoryModels.get(position).getType().equalsIgnoreCase("director")? R.drawable.ic_director: R.drawable.ic_actor);

        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
           // Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return categoryModels == null ? 0 : categoryModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
//        Typeface tfSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Semibold.otf");
//        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/myriadpro.otf");
        TextView tvCategoryName;
        ImageView ivIcon;
        LinearLayout llMaster;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            ivIcon = itemView.findViewById(R.id.ivCategoryIcon);
            llMaster = itemView.findViewById(R.id.containerMaster);
        }
    }
}
