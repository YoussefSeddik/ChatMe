package com.seddik.youssef.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("sender")
    @Expose
    private String sender;

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    @SerializedName("isseen")
    @Expose
    private boolean isseen;

    public Chat(String message, String receiver, String sender, boolean isseen) {
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
        this.isseen = isseen;
    }

    public Chat() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}