package com.roninaks.hellomywork.adapters;

import android.content.Context;
//import android.support.annotation.NonNull;
//import androidx.core.app.Fragment;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.fragments.UnionsIndividualFragment;
import com.roninaks.hellomywork.models.RatingsModel;

import java.util.ArrayList;

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ViewHolder>{
    private ArrayList<RatingsModel> ratingsModels;
    private Context context;
    private View rootview;
//    private RequestOptions requestOptions;


    public RatingsAdapter(Context context, ArrayList<RatingsModel> ratingsModels, View rootview) {
        this.context = context;
        this.ratingsModels = ratingsModels;
        this.rootview = rootview;
//        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.profile_default);
//        requestOptions.error(R.drawable.profile_default);
    }


    @NonNull
    @Override
    public RatingsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ratings_individual_listitem,parent,false);
        return new RatingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingsAdapter.ViewHolder holder, final int position)  {
        try {
            holder.tvUserName.setText(ratingsModels.get(position).getUserName());
            holder.tvReviews.setText(ratingsModels.get(position).getReview().isEmpty() ? "No review" : ratingsModels.get(position).getReview());
            holder.tvRating.setText("" + ratingsModels.get(position).getRating());
        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }

    }


    @Override
    public int getItemCount() {
        return ratingsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //        Typeface tfSemibold = Typeface.createFromAsset(context.getAssets(), "fonts/MyriadPro-Semibold.otf");
//        Typeface tfRegular = Typeface.createFromAsset(context.getAssets(), "fonts/myriadpro.otf");
        TextView tvUserName, tvRating, tvReviews;
        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvReviews = itemView.findViewById(R.id.tvReviewText);
        }
    }
}
