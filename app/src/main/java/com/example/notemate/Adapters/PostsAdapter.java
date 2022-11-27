package com.example.notemate.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notemate.FileUtil;
import com.example.notemate.Models.PostData;
import com.example.notemate.R;
import com.example.notemate.Utils;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {


    private List<PostData> myList;
    public PostsAdapter(List<PostData> myList) {
        this.myList = myList;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        holder.updateTemplate(myList.get(position));
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Context context;
        TextView user, title, postTime,postDesc;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            title = itemView.findViewById(R.id.postItemTitle);
            user = itemView.findViewById(R.id.postItemUser);
            postTime = itemView.findViewById(R.id.postItemTime);
            postDesc = itemView.findViewById(R.id.postItemTextDesc);
            imageView = itemView.findViewById(R.id.postItemImageView);
        }


        public void updateTemplate(PostData postData)
        {
            title.setText(postData.getTitle());
            postTime.setText(DateUtils.getRelativeTimeSpanString(postData.getUploadTime()));
            postDesc.setText(postData.getDescription());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(postData.getUid()).get().addOnSuccessListener(task ->{
                user.setText(task.getData().get("name").toString());
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "There was some error while fetching details.", Toast.LENGTH_SHORT).show();
                Log.i("PostAdapter", "updateTemplate: " + e.getMessage());
            });


            String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6c/PDF_icon.svg/1792px-PDF_icon.svg.png";
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            storageReference.child("images/"+postData.getPostId()+".jpg").getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(context).load(uri).into(imageView);
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Error+"+e.getMessage(), Toast.LENGTH_SHORT).show();
                Glide.with(context).load(imageUrl).into(imageView);
            });




            imageView.setClickable(true);
            imageView.setOnClickListener(view -> {
                Utils.openPdf(context, postData.getPdfUri());
            });

        }
    }


}
