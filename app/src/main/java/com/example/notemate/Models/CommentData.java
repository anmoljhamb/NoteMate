package com.example.notemate.Models;

import com.google.firebase.firestore.DocumentSnapshot;

public class CommentData {

    private String uid;
    private String doubtId;
    private long uploadTime;
    private String content;


    public CommentData(String uid, String doubtId, String content) {
        this.uid = uid;
        this.uploadTime = System.currentTimeMillis();
        this.doubtId = doubtId;
        this.content = content;
    }

    public CommentData(DocumentSnapshot documentSnapshot) {
        this.uid = documentSnapshot.get("uid").toString();
        this.uploadTime = Long.parseLong(documentSnapshot.get("uploadTime").toString());
        this.doubtId = documentSnapshot.get("doubtId").toString();
        this.content = documentSnapshot.get("content").toString();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDoubtId() {
        return doubtId;
    }

    public void setDoubtId(String doubtId) {
        this.doubtId = doubtId;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
