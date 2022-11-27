package com.example.notemate.Models;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.Map;

public class UserData {

    private String name;
    private String uid;
    private String email;
    private String college;
    private String course;

    public UserData(String name, String uid, String email, String college, String course) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.college = college;
        this.course = course;
    }

    public UserData(QueryDocumentSnapshot documentSnapshot)
    {
        this.name = documentSnapshot.get("name").toString();
        this.email = documentSnapshot.get("email").toString();
        this.college = documentSnapshot.get("college").toString();
        this.course = documentSnapshot.get("course").toString();
        this.uid = documentSnapshot.getId();
    }


    public UserData(Map<String, Object> userMap)
    {
        this.name = userMap.get("name").toString();
        this.email = userMap.get("email").toString();
        this.college = userMap.get("college").toString();
        this.course = userMap.get("course").toString();
        this.uid = userMap.get("uid").toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", college='" + college + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
