package com.example.notemate.Adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemate.Models.CommentData;
import com.example.notemate.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {


    List<CommentData> commentDataList;

    public CommentsAdapter(List<CommentData> commentDataList) {
        this.commentDataList = commentDataList;
    }


    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        holder.updateTemplate(commentDataList.get(position));
    }


    @Override
    public int getItemCount() {
        return commentDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        Context context;
        TextView userName, uploadTime, content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.itemCommentUserName);
            uploadTime = itemView.findViewById(R.id.itemCommentUploadTime);
            content = itemView.findViewById(R.id.itemCommentContent);
        }

        public void updateTemplate(CommentData commentData) {
            uploadTime.setText(DateUtils.getRelativeTimeSpanString(commentData.getUploadTime()));
            content.setText(commentData.getContent());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(commentData.getUid()).get().addOnSuccessListener(t -> {
                userName.setText(t.get("name").toString());
            });
        }
    }
}
