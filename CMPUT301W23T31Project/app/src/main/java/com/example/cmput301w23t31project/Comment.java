package com.example.cmput301w23t31project;


/**
 * Class represents a single instance of a comment made on a QR code
 * Stores all relevant info about comment (including comment contents)
 */
public class Comment {
    private String userName;
    private final String comment;
    private String date;

    /**
     * Instantiates new Comment object (created when comment is added to code)
     * @param username username of player that made/added comment
     * @param comment body of comment
     * @param date date comment is made (will be current date at time of creation)
     */
    public Comment(String username, String comment, String date) {
        this.userName = username;
        this.comment = comment;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}