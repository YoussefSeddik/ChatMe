package com.seddik.youssef.myapplication.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("imageURL")
    @Expose
    private String imageURL;
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("notificationKey")
    @Expose
    private String notificationKey;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @SerializedName("status")
    @Expose
    private String status;


    public User(String id, String username, String image_url, String status, String notificationKey) {
        this.id = id;
        this.username = username;
        this.imageURL = image_url;
        this.status = status;
        this.notificationKey = notificationKey;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public User() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}