package com.roninaks.hellomywork.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CommentsModel;
import com.roninaks.hellomywork.models.ProfilePostModel;

import java.util.ArrayList;

public class ActivityFeedAdapter extends RecyclerView.Adapter<ActivityFeedAdapter.ViewHolder> implements SqlDelegate {
        private Context context;
        private View rootview;
        private ArrayList<ProfilePostModel> profilePostModels;
        private RequestOptions requestOptions;
        private String baseImagePostUrl;
        RecyclerView commnetsRecyclerView;
        CommetsAdapter commetsAdapter;
//        private int colorList[] = {R.color.palette_orange, R.color.palette_brown, R.color.palette_blue, R.color.palette_green};


        public ActivityFeedAdapter(Context context, ArrayList<ProfilePostModel> profilePostModels, View rootview) {
            this.context = context;
            this.profilePostModels = profilePostModels;
            this.rootview = rootview;
            baseImagePostUrl = "https://www.hellomywork.com/";
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_image);
            requestOptions.error(R.drawable.icon_image);
        }


        @NonNull
        @Override
        public ActivityFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.profile_posts_item,parent,false);
            return new ActivityFeedAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ActivityFeedAdapter.ViewHolder holder, final int position)  {
            try {

                holder.tvPostProfileName.setText(profilePostModels.get(position).getName());
                holder.tvPostProfileDes.setText(profilePostModels.get(position).getDescription());
                holder.tvPostProfileLocation.setText(profilePostModels.get(position).getLocation());
                String date = profilePostModels.get(position).getDate();
                holder.tvPostProfileDate.setText(date.split(String.valueOf(' '))[0]);
                holder.tvPostProfileTime.setText(profilePostModels.get(position).getTime());
                holder.tvPostLikeCount.setText(profilePostModels.get(position).getLikeCount());
                holder.tvPostCommentCount.setText(profilePostModels.get(position).getCommentCount());
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions
                            .centerCrop()
                            )
                        .asBitmap()
                        .load(baseImagePostUrl + profilePostModels.get(position).getImageUri())
                        .into(holder.ivPostProfileImage);

                holder.ivLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.filled) {
                            holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_lik_post_min));
                            holder.filled = false;
                        }
                        else {
                            holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_like_post_fill_min));
                            holder.filled=true;

                        }
                    }
                });
                holder.ivBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(holder.filled) {
                            holder.ivBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkpost_min));
                            Toast.makeText(context, "Post have been removed from bookmark", Toast.LENGTH_SHORT).show();

                            holder.filled= false;
                        }
                        else {
                            holder.ivBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_fill_green256_min));
                            Toast.makeText(context, "Post have been added to bookmark", Toast.LENGTH_SHORT).show();
                            holder.filled= true;
                        }
                        bookmark();
                    }

                    private void bookmark() {

                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
                        sqlHelper.setExecutePath("updatebookmarks.php");
                        sqlHelper.setActionString("bookmark");
                        sqlHelper.setMethod("POST");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("userId", ((MainActivity) context).isLoggedIn());
                        contentValues.put("type", "profiles");
                        contentValues.put("mapping_id", profilePostModels.get(position).getId());
                        contentValues.put("is_active", profilePostModels.get(position).getIsBoomarked());
                        sqlHelper.setParams(contentValues);
                        sqlHelper.executeUrl(false);
                    }
                });
//                holder.ivComment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        writeComments.requestFocus();
//
//                    }
//                });
                holder.ivShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                holder.writeComments.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        boolean handled = false;
                        if (actionId == EditorInfo.IME_ACTION_SEND) {
                            sendMessage();
                            handled = true;
                        }
                        return handled;
                    }

                    private void sendMessage() {
                        String comment = holder.writeComments.getText().toString();
                        String us_id = ((MainActivity) context).isLoggedIn();
                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
                        sqlHelper.setExecutePath("postcomment.php");
                        sqlHelper.setActionString("commentPost");
                        sqlHelper.setMethod("POST");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("u_id", us_id);
                        contentValues.put("comment", comment);
                        contentValues.put("p_id", profilePostModels.get(position).getId());
                        sqlHelper.setParams(contentValues);
                        sqlHelper.executeUrl(false);
                        holder.writeComments.clearFocus();
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }
                });

                populateRecycler(holder.commentrecyclerView, profilePostModels.get(position).getCommentsModels());

            }catch (Exception e){
//            EmailHelper emailHelper = new EmailHelper(context, EmailHelper.TECH_SUPPORT, "Error: ActorAdapter", StringHelper.convertStackTrace(e));
//            emailHelper.sendEmail();
            }

        }

    private void populateRecycler(RecyclerView commentrecyclerView, ArrayList<CommentsModel> commentsModels) {
        CommetsAdapter commetsAdapter =new CommetsAdapter(context, commentsModels, commentrecyclerView);
        LinearLayoutManager linearLayout =new LinearLayoutManager(context);
        commentrecyclerView.setLayoutManager(linearLayout);
        commentrecyclerView.setAdapter(commetsAdapter);
    }


    @Override
        public int getItemCount() {
            return profilePostModels.size();
        }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        String response = sqlHelper.getStringResponse();
        if (response.equals("Success "))
            Toast.makeText(context, "Comments Added", Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvPostProfileName, tvPostProfileDes, tvPostProfileLocation, tvPostProfileDate, tvPostProfileTime, tvPostLikeCount, tvPostCommentCount;
            ImageView ivPostProfileImage;
            ImageButton btnProfilePostShare;
            EditText writeComments;
            boolean filled= false;
            RecyclerView commentrecyclerView;
            private ImageView ivLike,ivComment,ivShare,ivBookmark;
            public ViewHolder(View itemView) {
                super(itemView);
                tvPostProfileName = itemView.findViewById(R.id.post_item_name);
                tvPostProfileDes = itemView.findViewById(R.id.post_item_descprition);
                tvPostProfileLocation = itemView.findViewById(R.id.post_item_loaction);
                tvPostProfileDate = itemView.findViewById(R.id.post_item_date);
                tvPostProfileTime = itemView.findViewById(R.id.post_item_time);
                ivPostProfileImage = itemView.findViewById(R.id.post_item_post_image);
                btnProfilePostShare = itemView.findViewById(R.id.post_item_shareBTN);
                ivLike=itemView.findViewById(R.id.iv_LikeButton);
                ivComment=itemView.findViewById(R.id.iv_Comment);
                ivShare=itemView.findViewById(R.id.iv_Share);
                ivBookmark=itemView.findViewById(R.id.iv_Bookmark);
                writeComments = itemView.findViewById(R.id.editText_WriteComments);
                commentrecyclerView = itemView.findViewById(R.id.postItemCommentsRV);
                tvPostCommentCount = itemView.findViewById(R.id.postCommentCount);
                tvPostLikeCount = itemView.findViewById(R.id.postLikeCount);
            }
        }
    }
