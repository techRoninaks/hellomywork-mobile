package com.roninaks.hellomywork.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.ProfilePostModel;

import java.util.ArrayList;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PostBookmarkAdapter extends RecyclerView.Adapter<PostBookmarkAdapter.ViewHolder> implements SqlDelegate {

    private Context context;
    private View rootview;
    private ArrayList<ProfilePostModel> profilePostModels;
    private RecyclerView recyclerView;
    private String baseImagePostUrl;
    private RequestOptions requestOptions;


    public PostBookmarkAdapter(Context context, final ArrayList<ProfilePostModel> profilePostModels, View rootview, RecyclerView recyclerView) {
        this.context = context;
        this.profilePostModels = profilePostModels;
        this.rootview = rootview;
        this.recyclerView = recyclerView;
        baseImagePostUrl = "https://www.hellomywork.com/";
        baseImagePostUrl = "http://understandable-blin.hostingerapp.com/helloMyWork-Mobile/php/";
        requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon_image);
        requestOptions.error(R.drawable.icon_image);
    }

    @NonNull
    @Override
    public PostBookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookmark_post_item,parent,false);
        return new PostBookmarkAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostBookmarkAdapter.ViewHolder holder, final int position) {
        try {
            holder.tvBookmarkPostName.setText(this.profilePostModels.get(position).getName());
            holder.tvBookmarkPostDes.setText(profilePostModels.get(position).getDescription());
            holder.tvBookmarkPostPlace.setText(profilePostModels.get(position).getLocation());
            String date = profilePostModels.get(position).getDate();
            date = date.split(String.valueOf(' '))[0];
            String [] datepost = date.split("-");//Converting date to standard form.
            holder.tvBookmarkPostDate.setText(datepost[2]+"-"+datepost[1]+"-"+datepost[0]);
            //holder.tvBookmarkPostTime.setText(profilePostModels.get(position).getDescription());

            holder.tvLikeCount.setText(profilePostModels.get(position).getLikeCount());
            holder.tvCommentCount.setText(profilePostModels.get(position).getCommentCount());

            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions
                            .centerCrop()
                    )
                    .asBitmap()
                    .load(baseImagePostUrl + profilePostModels.get(position).getImageUri())
                    .listener(new RequestListener<Bitmap>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                            int width = holder.ivBookmarkPostImage.getWidth();
                            int height = holder.ivBookmarkPostImage.getHeight();
                            Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                            image.eraseColor(getColor());
                            holder.ivBookmarkPostImage.setImageBitmap(image);
                            holder.tvBookmarkNoImageDesc.setVisibility(View.VISIBLE);
                            holder.tvBookmarkNoImageDesc.setText(profilePostModels.get(position).getDescription());
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.ivBookmarkPostImage);
            if(profilePostModels.get(position).getIsLiked().equals("0")){
                holder.filled = false;
                holder.ivIsLiked.setImageDrawable(context.getDrawable(R.drawable.ic_lik_post_min));
            }
            else if(profilePostModels.get(position).getIsLiked().equals("1")){
                holder.filled = true;
                holder.ivIsLiked.setImageDrawable(context.getDrawable(R.drawable.ic_like_post_fill_min));
            }
            if(profilePostModels.get(position).getIsBoomarked().equals("0")){
                holder.bookmarkFilled = false;
                holder.ivIsBookmarked.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkpost_min));
            }
            else if(profilePostModels.get(position).getIsBoomarked().equals("1")){
                holder.bookmarkFilled = true;
                holder.ivIsBookmarked.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkfill_idcard));
            }

            holder.ivIsLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                        builder.setMessage("You need to login to make bookmark").setPositiveButton("Go to login?", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                    else {
                        if (holder.filled) {
                            holder.ivIsLiked.setImageDrawable(context.getDrawable(R.drawable.ic_lik_post_min));
                            holder.filled = false;
                            int  likeCount =Integer.parseInt(holder.tvLikeCount.getText().toString());
                            likeCount = likeCount - 1;
                            holder.tvLikeCount.setText(Integer.toString(likeCount));
                            updateLike("delete");
                        } else {
                            holder.ivIsLiked.setImageDrawable(context.getDrawable(R.drawable.ic_like_post_fill_min));
                            holder.filled = true;
                            int  likeCount =Integer.parseInt(holder.tvLikeCount.getText().toString());
                            likeCount =likeCount + 1;
                            holder.tvLikeCount.setText(Integer.toString(likeCount));
                            updateLike("add");
                        }
                    }
                }

                private void updateLike(String action) {
                    SqlHelper sqlHelper = new SqlHelper(context, PostBookmarkAdapter.this);
                    sqlHelper.setExecutePath("updatelike.php");
                    sqlHelper.setActionString("like");
                    sqlHelper.setMethod("POST");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userid", ((MainActivity) context).isLoggedIn());
                    contentValues.put("action", action);
                    contentValues.put("postid", profilePostModels.get(position).getId());
                    sqlHelper.setParams(contentValues);
                    sqlHelper.executeUrl(false);
                }
            });


            holder.ivIsBookmarked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.bookmarkFilled= false;
                    String mapping_id = profilePostModels.get(position).getId();
                    String is_active = profilePostModels.get(position).getIsBoomarked();
                    if(profilePostModels!=null) {
//                        holder.ivIsBookmarked.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkpost_min));
//                        Toast.makeText(context, "Post have been removed from bookmark", Toast.LENGTH_SHORT).show();
                        int position = holder.getAdapterPosition();
                        profilePostModels.remove(position);
                        notifyItemRemoved(position);
                        notifyDataSetChanged();
                        bookmark(mapping_id,is_active);
                    }

                }

                private void bookmark(String mapping_id,String is_active) {

                    SqlHelper sqlHelper = new SqlHelper(context, PostBookmarkAdapter.this);
                    sqlHelper.setExecutePath("updatebookmarks.php");
                    sqlHelper.setActionString("bookmark");
                    sqlHelper.setMethod("POST");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userId", ((MainActivity) context).isLoggedIn());
                    contentValues.put("type", "posts");
                    contentValues.put("mapping_id", mapping_id);
                    contentValues.put("is_active", is_active);
                    sqlHelper.setParams(contentValues);
                    sqlHelper.executeUrl(false);
                }
            });

            holder.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



        }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
        }
    }

    @Override
    public int getItemCount() {
        return profilePostModels.size();
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        String response = sqlHelper.getStringResponse();
        if(sqlHelper.getActionString() == "bookmark"){
            if(response.equals("1"))
                Toast.makeText(context, "bookmarked", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "bookmark removed", Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvBookmarkPostName, tvBookmarkPostDes,tvBookmarkPostPlace,tvBookmarkPostDate,tvBookmarkPostTime, tvBookmarkNoImageDesc;
        TextView tvLikeCount,tvCommentCount;
        ImageView ivIsBookmarked,ivIsLiked,ivComments,ivShare,ivBookmarkPostImage;
        boolean filled= false;
        boolean bookmarkFilled= false;
        public ViewHolder(View itemView) {
            super(itemView);
            tvBookmarkPostName = itemView.findViewById(R.id.bookmark_post_name);
            tvBookmarkPostDes = itemView.findViewById(R.id.bookmark_post_des);
            tvBookmarkPostPlace = itemView.findViewById(R.id.bookmark_post_location);
            tvBookmarkPostDate = itemView.findViewById(R.id.bookmark_post_date);
            tvBookmarkPostTime = itemView.findViewById(R.id.bookmark_post_time);
            tvBookmarkNoImageDesc = itemView.findViewById(R.id.tvNoImageTextDesc);

            tvLikeCount = itemView.findViewById(R.id.postBookmarkLikeCount);
            tvCommentCount = itemView.findViewById(R.id.postBookmarkCommentCount);

            ivIsBookmarked = itemView.findViewById(R.id.bookmark_post_bookmarked);
            ivIsLiked = itemView.findViewById(R.id.iv_postBookmark_like_button);
            ivComments = itemView.findViewById(R.id.iv_postBookmarkComment);
            ivShare = itemView.findViewById(R.id.iv_postBookmark_Share);
            ivBookmarkPostImage = itemView.findViewById(R.id.ivBookmarkPostImage);
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
