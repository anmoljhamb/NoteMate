package com.example.notemate.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notemate.CommentsActivity;
import com.example.notemate.Models.DoubtData;
import com.example.notemate.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DoubtsAdapter extends RecyclerView.Adapter<DoubtsAdapter.ViewHolder> {


    List<DoubtData> doubtDataList;

    public DoubtsAdapter(List<DoubtData> doubtDataList) {
        this.doubtDataList = doubtDataList;
    }

    @NonNull
    @Override
    public DoubtsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doubt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoubtsAdapter.ViewHolder holder, int position) {
        holder.updateTemplate(doubtDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return doubtDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Context context;
        TextView doubtTitle;
        TextView doubtDesc;
        TextView userName;
        TextView uploadTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            doubtTitle = itemView.findViewById(R.id.doubtItemTitle);
            doubtDesc = itemView.findViewById(R.id.doubtItemTextDesc);
            userName = itemView.findViewById(R.id.doubtItemUser);
            uploadTime = itemView.findViewById(R.id.doubtItemTime);
        }


        public void updateTemplate(DoubtData doubtData)
        {
            doubtTitle.setText(doubtData.getDoubtTitle());
            doubtDesc.setText(doubtData.getDoubtDesc());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(doubtData.getUid()).get().addOnSuccessListener(task -> {
                userName.setText(task.get("name").toString());
            });
            uploadTime.setText(DateUtils.getRelativeTimeSpanString(doubtData.getUploadTime()));

            itemView.findViewById(R.id.doubtItemComments).setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), CommentsActivity.class);
                intent.putExtra("doubtId", doubtData.getDoubtId());
                itemView.getContext().startActivity(intent);
            });

        }

    }
}
