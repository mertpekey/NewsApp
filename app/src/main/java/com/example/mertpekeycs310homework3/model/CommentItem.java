package com.example.mertpekeycs310homework3.model;



public class CommentItem {

    private int id;
    private int newsId;
    private String name;
    private String message;


    public CommentItem() {
    }

    public CommentItem(int id, int newsId, String name, String message) {
        this.name = name;
        this.message = message;
        this.id = id;
        this.newsId = newsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
