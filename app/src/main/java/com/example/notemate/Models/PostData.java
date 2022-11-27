package com.example.notemate.Models;

import android.net.Uri;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class PostData {

    private String uid;
    private String title;
    private String description;
    private Uri pdfUri;
    private long uploadTime;
    private String postId;


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public PostData(DocumentSnapshot documentSnapshot)
    {
        Map<String, Object> data = documentSnapshot.getData();
        this.uid = data.get("uid").toString();
        this.description = data.get("description").toString();
        this.title = data.get("title").toString();
        this.pdfUri = Uri.parse(data.get("pdfUri").toString());
        this.uploadTime = Long.parseLong(data.get("uploadTime").toString());
        if (data.get("postId") != null )
            this.postId = data.get("postId").toString();
    }


    public PostData(String uid, String title, String description, Uri pdfUri) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.pdfUri = pdfUri;
        this.uploadTime = System.currentTimeMillis();
    }


    @Override
    public String toString() {
        return "PostData{" +
                "uid='" + uid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pdfUri=" + pdfUri +
                ", uploadTime=" + uploadTime +
                '}';
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Uri getPdfUri() {
        return pdfUri;
    }

    public void setPdfUri(Uri pdfUri) {
        this.pdfUri = pdfUri;
    }
}
