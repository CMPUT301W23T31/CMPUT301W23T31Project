package com.example.cmput301w23t31project;

public class Image {
    private final String link;
    private String user;
    public Image(String storage, String user) {
        this.link = storage;
        this.user = user;
    }

    public String getLink() {
        return link;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
