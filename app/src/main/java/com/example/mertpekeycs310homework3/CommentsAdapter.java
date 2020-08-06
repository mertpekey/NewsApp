package com.example.mertpekeycs310homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mertpekeycs310homework3.model.CommentItem;

import java.util.List;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {


    List<CommentItem> commentsItems;
    Context context;
    CommentsItemClickListener listener;

    public CommentsAdapter(List<CommentItem> commentsItems, Context context, CommentsItemClickListener listener) {
        this.commentsItems = commentsItems;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comments_row_layout,parent,false);
        return new CommentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        holder.txtCommentTitle.setText(commentsItems.get(position).getMessage());
        holder.txtCommentText.setText(commentsItems.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return commentsItems.size();
    }

    public interface CommentsItemClickListener{

        public void CommentItemClicked(CommentItem selectedCommentItem);
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{

        TextView txtCommentTitle;
        TextView txtCommentText;
        ConstraintLayout root;


        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCommentTitle = itemView.findViewById(R.id.txtcommenttitle);
            txtCommentText = itemView.findViewById(R.id.txtcommenttext);
            root = itemView.findViewById(R.id.containerComment);

        }

    }
}
