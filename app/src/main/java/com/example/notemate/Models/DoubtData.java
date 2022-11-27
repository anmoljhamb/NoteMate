package com.example.notemate.Models;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class DoubtData {

    private String doubtTitle;
    private String doubtDesc;
    private String doubtId;
    private String uid;
    private long uploadTime;


    public DoubtData(String doubtTitle, String doubtDesc, String uid) {
        this.doubtTitle = doubtTitle;
        this.doubtDesc = doubtDesc;
        this.uid = uid;
        this.uploadTime = System.currentTimeMillis();
    }

    public DoubtData(DocumentSnapshot documentSnapshot) {
        this.doubtTitle = documentSnapshot.get("doubtTitle").toString();
        this.doubtDesc = documentSnapshot.get("doubtDesc").toString();
        this.uploadTime = Long.parseLong(documentSnapshot.get("uploadTime").toString());
        this.uid = documentSnapshot.get("uid").toString();
        this.doubtId = documentSnapshot.get("doubtId").toString();
    }

    public DoubtData(Map<String, Object> data) {
        this.doubtTitle = data.get("doubtTitle").toString();
        this.doubtDesc = data.get("doubtDesc").toString();
        this.uploadTime = Long.parseLong(data.get("uploadTime").toString());
        this.uid = data.get("uid").toString();
        this.doubtId = data.get("doubtId").toString();
    }

    public String getDoubtTitle() {
        return doubtTitle;
    }

    public void setDoubtTitle(String doubtTitle) {
        this.doubtTitle = doubtTitle;
    }

    public String getDoubtDesc() {
        return doubtDesc;
    }

    public void setDoubtDesc(String doubtDesc) {
        this.doubtDesc = doubtDesc;
    }

    public String getDoubtId() {
        return doubtId;
    }

    public void setDoubtId(String doubtId) {
        this.doubtId = doubtId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }
}
