package com.roninaks.hellomywork.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.activities.LoginActivity;
import com.roninaks.hellomywork.activities.MainActivity;
import com.roninaks.hellomywork.helpers.SqlHelper;
import com.roninaks.hellomywork.interfaces.SqlDelegate;
import com.roninaks.hellomywork.models.CommentsModel;

import java.util.ArrayList;

public class CommetsAdapter extends RecyclerView.Adapter<CommetsAdapter.ViewHolder> implements SqlDelegate {

    Context context;
    ArrayList<CommentsModel> commentsModels;

    public CommetsAdapter(Context context, ArrayList<CommentsModel> commentsModels, View rootview) {
        this.context = context;
        this.commentsModels = commentsModels;
    }

    @NonNull
    @Override
    public CommetsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_comments_item,parent,false);
        return new CommetsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommetsAdapter.ViewHolder holder, final int position) {
        holder.postCommnetName.setText(commentsModels.get(position).getCommentName());
        holder.postComment.setText(commentsModels.get(position).getComment());
        holder.postCommentReport.setOnClickListener(new View.OnClickListener() {
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
                    builder.setMessage("You need to login to report").setPositiveButton("Go to login?", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }else{
                    String currentUser = ((MainActivity) context).isLoggedIn();
                    if(commentsModels.get(position).getCommentU_Id().equals(currentUser)){
                        Toast.makeText(context,"You cannot report your own comment!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reportComment();
                        //                holder.commentMaster.setVisibility(View.GONE);
                        commentsModels.remove(position);
                        notifyDataSetChanged();
                    }
            }}

            private void reportComment() {
                SqlHelper sqlHelper = new SqlHelper(context, CommetsAdapter.this);
                sqlHelper.setExecutePath("postreportedpost.php");
                sqlHelper.setActionString("commentReport");
                sqlHelper.setMethod("POST");
                ContentValues contentValues = new ContentValues();
                contentValues.put("id", commentsModels.get(position).getCommentId());
                sqlHelper.setParams(contentValues);
                sqlHelper.executeUrl(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsModels.size();
    }

    @Override
    public void onResponse(SqlHelper sqlHelper) {
        String respone = sqlHelper.getStringResponse();
        if (sqlHelper.getActionString().equals("commentReport")){
            if(respone.equals("success")){

            }
            else {

            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView postCommnetName, postComment;
        ImageView postCommentReport;
        LinearLayout commentMaster;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postComment = itemView.findViewById(R.id.post_comment_comment);
            postCommnetName = itemView.findViewById(R.id.post_comment_name);
            postCommentReport = itemView.findViewById(R.id.post_comment_report);
            commentMaster = itemView.findViewById(R.id.post_comment_master);
        }
    }
}
