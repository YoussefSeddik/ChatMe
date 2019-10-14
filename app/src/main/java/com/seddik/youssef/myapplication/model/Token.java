package com.seddik.youssef.myapplication.model;

public class Token {

    String id;
    String apiKey;

    public Token(String id, String apiKey) {
        this.id = id;
        this.apiKey = apiKey;
    }

    public Token() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
