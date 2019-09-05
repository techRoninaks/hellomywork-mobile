package com.roninaks.hellomywork.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roninaks.hellomywork.R;
import com.roninaks.hellomywork.models.CommentsModel;

import java.util.ArrayList;

public class CommetsAdapter extends RecyclerView.Adapter<CommetsAdapter.ViewHolder> {

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
    public void onBindViewHolder(@NonNull final CommetsAdapter.ViewHolder holder, int position) {
        holder.postCommnetName.setText(commentsModels.get(position).getCommentName());
        holder.postComment.setText(commentsModels.get(position).getComment());
        holder.postCommentReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.commentMaster.setVisibility(View.GONE);
                Toast.makeText(context, "Comment have been reported", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
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
