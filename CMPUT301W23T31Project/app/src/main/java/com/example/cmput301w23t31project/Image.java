package com.example.cmput301w23t31project;

/**
 * A class which represents an Image, one of whose attributes is an url to an image png
 */
public class Image {
    private final String link;
    private String user;

    /**
     *
     * @param storage- link of the image
     * @param user- username of the person who took the image
     */
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
