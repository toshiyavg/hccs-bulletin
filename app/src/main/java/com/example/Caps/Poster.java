package com.example.Caps;

public class Poster {
    private String imglink;
    private String title;
    private String details;
    private String date;
    private String type;
    private String auth;
    private String postid;

    public Poster() {
    }

    public Poster(String imglink, String title, String details, String date, String type, String auth, String postid) {
        this.imglink = imglink;
        this.title = title;
        this.details = details;
        this.date = date;
        this.type = type;
        this.auth = auth;
        this.postid = postid;
    }

    public String getImglink() {
        return imglink;
    }

    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
