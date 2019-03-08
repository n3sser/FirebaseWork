package com.example.workshop_firebase;

public class Posts {

    private String post, username;

    public String getPost() {
        return post;
    }

    public String getUsername() {
        return username;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Posts(String post, String username) {
        this.post = post;
        this.username = username;
    }

    public Posts(){

    }
}
