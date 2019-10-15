package com.roninaks.hellomywork.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.activities.RegisterActivity;
import com.roninaks.hellomywork.fragments.AboutFragment;
import com.roninaks.hellomywork.fragments.CareersFragment;
import com.roninaks.hellomywork.fragments.ContactFragment;
import com.roninaks.hellomywork.fragments.ProfileFragment;
import com.roninaks.hellomywork.helpers.ModelHelper;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.OnLoadMoreListener;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CommentsModel;
import com.roninaks.hellomywork.models.ProfilePostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ActivityFeedAdapter extends RecyclerView.Adapter<ActivityFeedAdapter.ViewHolder> implements SqlDelegate {
        private Context context;
        private View rootview;
        private ArrayList<ProfilePostModel> profilePostModels;
        private ArrayList<CommentsModel> commentsModels;
        private RequestOptions requestOptions;
        private String baseImagePostUrl, userName;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;

        RecyclerView commnetsRecyclerView,commentRecyclerView;
        CommetsAdapter commetsAdapter;
        private String user_id;
//        private int colorList[] = {R.color.palette_orange, R.color.palette_brown, R.color.palette_blue, R.color.palette_green};


        public ActivityFeedAdapter(Context context, ArrayList<ProfilePostModel> profilePostModels, View rootview) {
            this.context = context;
            this.profilePostModels = profilePostModels;
            this.rootview = rootview;
            baseImagePostUrl = "https://www.hellomywork.com/";
            baseImagePostUrl = "http://understandable-blin.hostingerapp.com/helloMyWork-Mobile/php/";
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_image);
            requestOptions.error(R.drawable.icon_image);
        }
        public ActivityFeedAdapter(Context context, ArrayList<ProfilePostModel> profilePostModels, View rootview, String user_id,RecyclerView recyclerView) {
            this.context = context;
            this.profilePostModels = profilePostModels;
            this.rootview = rootview;
            this.user_id = user_id;
            baseImagePostUrl = "https://www.hellomywork.com/";
            baseImagePostUrl = "http://understandable-blin.hostingerapp.com/helloMyWork-Mobile/php/";
            requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.icon_image);
            requestOptions.error(R.drawable.icon_image);

            try {
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        totalItemCount = getItemCount();
                        lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                        if (!loading && (totalItemCount - lastVisibleItem - 1) == 0) {

                            if (onLoadMoreListener != null) {
                                onLoadMoreListener.onLoadMore();
                            }
                            loading = true;
                        }
                    }
                });
//        }
            }catch (Exception e){
            }
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
                date = date.split(String.valueOf(' '))[0];
                String [] datepost = date.split("-");//Converting date to standard form.
                holder.tvPostProfileDate.setText(datepost[2]+"-"+datepost[1]+"-"+datepost[0]);
                holder.tvPostProfileTime.setText(profilePostModels.get(position).getTime());
                holder.tvPostLikeCount.setText(profilePostModels.get(position).getLikeCount());
                holder.tvPostCommentCount.setText(profilePostModels.get(position).getCommentCount());
                holder.commentsModels = new ArrayList<>();
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions
                            .centerCrop()
                            )
                        .asBitmap()
                        .load(baseImagePostUrl + profilePostModels.get(position).getImageUri())
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                int width = holder.ivPostProfileImage.getWidth();
                                int height = holder.ivPostProfileImage.getHeight();
                                Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                image.eraseColor(getColor());
                                holder.ivPostProfileImage.setImageBitmap(image);
                                holder.tvNoImageDesc.setVisibility(View.VISIBLE);
                                holder.tvNoImageDesc.setText(profilePostModels.get(position).getDescription());
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(holder.ivPostProfileImage);

                if(profilePostModels.get(position).getIsLiked().equals("0")){
                    holder.filled = false;
                    holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_lik_post_min));
                }
                else if(profilePostModels.get(position).getIsLiked().equals("1")){
                    holder.filled = true;
                    holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_like_post_fill_min));
                }
                if(profilePostModels.get(position).getIsBoomarked().equals("0")){
                    holder.bookmarkFilled = false;
                    holder.ivBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkpost_min));
                }
                else if(profilePostModels.get(position).getIsBoomarked().equals("1")){
                    holder.bookmarkFilled = true;
                    holder.ivBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkfill_idcard));
                }
                String label = profilePostModels.get(position).getImageLabel();

                holder.btnProfilePostOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(context, v);
                        Menu m = popupMenu.getMenu();
                        MenuInflater inflater = popupMenu.getMenuInflater();
                        inflater.inflate(R.menu.post_option_menu, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()){
                                    case R.id.post_option_report:{
                                        if(((MainActivity) context).isLoggedIn().isEmpty()){
                                            context.startActivity(new Intent(context, LoginActivity.class));
                                        }else{
                                            reportPost(profilePostModels.get(position).getId());
                                            profilePostModels.remove(position);
                                            notifyDataSetChanged();
                                        }
                                        break;
                                    }
                                    case R.id.post_option_delete:{
                                        if(((MainActivity) context).isLoggedIn().isEmpty()){
                                            context.startActivity(new Intent(context, RegisterActivity.class));
                                        }else{
                                            if(user_id != ((MainActivity) context).isLoggedIn()){
                                                Toast.makeText(context, "You can't delete this post", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                deletePost(profilePostModels.get(position).getId());
                                                profilePostModels.remove(position);
                                                notifyDataSetChanged();
                                            }
                                        }
                                        break;
                                    }
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }

                    private void reportPost(String id) {
                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
                        sqlHelper.setExecutePath("postReport.php");
                        sqlHelper.setActionString("postReport");
                        sqlHelper.setMethod("POST");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("postid", id);
                        sqlHelper.setParams(contentValues);
                        sqlHelper.executeUrl(false);
                    }

                    private void deletePost(String id) {
                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
                        sqlHelper.setExecutePath("deletePost.php");
                        sqlHelper.setActionString("postDelete");
                        sqlHelper.setMethod("POST");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("postid", id);
                        sqlHelper.setParams(contentValues);
                        sqlHelper.executeUrl(false);
                    }
                });

                switch (label){
                    case "assets/img/icon/ic_Random-min.png":
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_random_min));
                        break;
                    case "assets/img/icon/ic_ForSale-min.png":
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_for_sale_min));
                        break;
                    case "assets/img/icon/ic_Required-min.png":
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_required_min));
                        break;
                    case "assets/img/icon/ic_Achievement-min.png":
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_achievement_min));
                        break;
                    case "assets/img/icon/ic_Appreciations-min.png":
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_appreciation_min));
                        break;
                    case "assets/img/icon/ic_Offers-min.png":
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_offers_min));
                        break;
                    default:
                        holder.ivPostImageLabel.setImageDrawable(context.getDrawable(R.drawable.ic_random_min));
                        break;
                }
                holder.ivLike.setOnClickListener(new View.OnClickListener() {
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
                                holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_lik_post_min));
                                holder.filled = false;
                                int  likeCount =Integer.parseInt(holder.tvPostLikeCount.getText().toString());
                                likeCount = likeCount - 1;
                                holder.tvPostLikeCount.setText(Integer.toString(likeCount));
                                updateLike("delete");
                            } else {
                                holder.ivLike.setImageDrawable(context.getDrawable(R.drawable.ic_like_post_fill_min));
                                holder.filled = true;
                                int  likeCount =Integer.parseInt(holder.tvPostLikeCount.getText().toString());
                                likeCount =likeCount + 1;
                                holder.tvPostLikeCount.setText(Integer.toString(likeCount));
                                updateLike("add");
                            }
                        }
                    }

                    private void updateLike(String action) {
                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
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

                if (Integer.valueOf(profilePostModels.get(position).getCommentCount()) <= 3){
                    holder.viewMoreComments.setVisibility(View.GONE);
                }

                holder.viewMoreComments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String post_id = profilePostModels.get(position).getId();

                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
                        sqlHelper.setExecutePath("getcomments.php");
                        sqlHelper.setActionString("postComments");
                        sqlHelper.setMethod("POST");
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("id", post_id);
                        sqlHelper.setParams(contentValues);
                        HashMap<String,String> extras = new HashMap<>();
                        extras.put("position", ""+position);
                        sqlHelper.setExtras(extras);
                        sqlHelper.executeUrl(true);
                    }
                });

                holder.ivBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mapping_id = profilePostModels.get(position).getId();
                        String is_active = profilePostModels.get(position).getIsBoomarked();
                        if(holder.bookmarkFilled) {
                            holder.ivBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkpost_min));
                            Toast.makeText(context, "Post have been removed from bookmark", Toast.LENGTH_SHORT).show();
                            holder.bookmarkFilled= false;
                        }
                        else {
                            holder.ivBookmark.setImageDrawable(context.getDrawable(R.drawable.ic_bookmarkfill_idcard));
                            Toast.makeText(context, "Post have been added to bookmark", Toast.LENGTH_SHORT).show();
                            holder.bookmarkFilled= true;
                        }
                        bookmark(mapping_id,is_active);
                        profilePostModels.get(position).setIsBoomarked(holder.bookmarkFilled ? "1" : "0");
                        notifyDataSetChanged();
                    }

                    private void bookmark(String mapping_id,String is_active) {

                        SqlHelper sqlHelper = new SqlHelper(context, ActivityFeedAdapter.this);
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

                holder.tvSendComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //post comment
                        commentRecyclerView = holder.commentrecyclerView;
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
                        userName = profilePostModels.get(position).getName();
                        HashMap<String,String> extras = new HashMap<>();
                        extras.put("position", ""+position);
                        sqlHelper.setExtras(extras);
                        sqlHelper.setParams(contentValues);
                        sqlHelper.executeUrl(false);
                        holder.writeComments.clearFocus();
                        holder.writeComments.setText("");
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
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
                        commentRecyclerView = holder.commentrecyclerView;
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
                        userName = profilePostModels.get(position).getName();
                        HashMap<String,String> extras = new HashMap<>();
                        extras.put("position", ""+position);
                        sqlHelper.setExtras(extras);
                        sqlHelper.setParams(contentValues);
                        sqlHelper.executeUrl(false);
                        holder.writeComments.clearFocus();
                        holder.writeComments.setText("");
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    }

                });
                commentsModels = profilePostModels.get(position).getCommentsModels();
                commentRecyclerView = holder.commentrecyclerView;
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

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
        public int getItemCount() {
            return profilePostModels.size();
        }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        String response = sqlHelper.getStringResponse();
        if(sqlHelper.getActionString() == "commentPost"){
            if (response.equals("Success ")){
                Toast.makeText(context, "Comments Added", Toast.LENGTH_SHORT).show();
                ContentValues contentValues = sqlHelper.getParams();
                String userComment = contentValues.get("comment").toString();
                String userId = contentValues.getAsString("u_id");
                String pId = contentValues.getAsString("p_id");
                int pos = Integer.parseInt(sqlHelper.getExtras().get("position"));
                CommentsModel commentsModel = new CommentsModel();
                commentsModel.setComment(userComment);
                commentsModel.setCommentName(userName);
                commentsModel.setCommentU_Id(userId);
                commentsModel.setCommentP_Id(pId);
                commentsModels.add(0,commentsModel);
                profilePostModels.get(pos).getCommentsModels().add(0, commentsModel);
                commentRecyclerView.getAdapter().notifyDataSetChanged();

//                populateRecycler(commentRecyclerView,commentsModels);
            }
        }
        else if(sqlHelper.getActionString() == "bookmark"){
//            if(response.equals("1"))
//                Toast.makeText(context, "bookmarked", Toast.LENGTH_SHORT).show();
//            else
//                Toast.makeText(context, "bookmark removed", Toast.LENGTH_SHORT).show();
        }
        else if(sqlHelper.getActionString() == "like"){
//            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
        else if(sqlHelper.getActionString() == "postDelete"){
//            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
        else if(sqlHelper.getActionString() == "postReport"){
//            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
        }
        else  if (sqlHelper.getActionString() == "postComments"){
            String responseFrom = sqlHelper.getStringResponse();
            if(responseFrom.equals("0")){
                Toast.makeText(context, "No more comments", Toast.LENGTH_SHORT).show();
            }
            else{
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int pos = Integer.parseInt(sqlHelper.getExtras().get("position"));
                    commentsModels.clear();
                    getCommentList(jsonArray,pos);
                    notifyDataSetChanged();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void getCommentList(JSONArray jsonArray,int pos) {
        try {
            for (int i= 3; i< jsonArray.length(); i++) {
                CommentsModel commentsModel = new CommentsModel();
                commentsModel.setComment(jsonArray.getJSONObject(i).getString("comment"));
                commentsModel.setCommentName(jsonArray.getJSONObject(i).getString("name"));
                commentsModel.setCommentId(jsonArray.getJSONObject(i).getString("id"));
                commentsModel.setCommentU_Id(jsonArray.getJSONObject(i).getString("u_id"));
                commentsModel.setCommentP_Id(jsonArray.getJSONObject(i).getString("p_id"));
                commentsModel.setCommentIsReported(jsonArray.getJSONObject(i).getString("IsReported"));
                commentsModel.setCommentIsActive(jsonArray.getJSONObject(i).getString("IsActive"));
                commentsModels.add(commentsModel);
                profilePostModels.get(pos).getCommentsModels().add(commentsModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvPostProfileName, tvPostProfileDes, tvPostProfileLocation, tvPostProfileDate, tvPostProfileTime, tvPostLikeCount, tvPostCommentCount,tvSendComment, tvNoImageDesc;
        ImageView ivPostProfileImage,ivPostImageLabel;
        ImageButton btnProfilePostOptions;
        TextView viewMoreComments;
        EditText writeComments;
        boolean filled= false;
        boolean bookmarkFilled= false;
        RecyclerView commentrecyclerView;
        ArrayList<CommentsModel> commentsModels;
        private ImageView ivLike,ivComment,ivShare,ivBookmark;
            public ViewHolder(View itemView) {
                super(itemView);
                tvPostProfileName = itemView.findViewById(R.id.post_item_name);
                tvPostProfileDes = itemView.findViewById(R.id.post_item_descprition);
                tvPostProfileLocation = itemView.findViewById(R.id.post_item_loaction);
                tvPostProfileDate = itemView.findViewById(R.id.post_item_date);
                tvPostProfileTime = itemView.findViewById(R.id.post_item_time);
                tvNoImageDesc = itemView.findViewById(R.id.tvNoImageTextDesc);
                ivPostProfileImage = itemView.findViewById(R.id.post_item_post_image);
                btnProfilePostOptions = itemView.findViewById(R.id.post_item_shareBTN);
                ivLike=itemView.findViewById(R.id.iv_LikeButton);
                ivComment=itemView.findViewById(R.id.iv_Comment);
                ivShare=itemView.findViewById(R.id.iv_Share);
                ivBookmark=itemView.findViewById(R.id.iv_Bookmark);
                writeComments = itemView.findViewById(R.id.editText_WriteComments);
                commentrecyclerView = itemView.findViewById(R.id.postItemCommentsRV);
                tvPostCommentCount = itemView.findViewById(R.id.postCommentCount);
                tvPostLikeCount = itemView.findViewById(R.id.postLikeCount);
                ivPostImageLabel = itemView.findViewById(R.id.post_item_image_label);
                tvSendComment = itemView.findViewById(R.id.addComment);
                viewMoreComments = itemView.findViewById(R.id.view_more_comments);
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
