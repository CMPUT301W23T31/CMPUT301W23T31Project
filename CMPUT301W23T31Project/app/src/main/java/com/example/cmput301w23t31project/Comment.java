package com.example.cmput301w23t31project;

public class Comment {
    private String userName;
    private String comment;
    private String date;
    private String QRHash;

    public Comment(String userName, String comment, String date, String QRHash) {
        this.userName = userName;
        this.comment = comment;
        this.date = date;
        this.QRHash = QRHash;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQRHash() {
        return QRHash;
    }

    public void setQRHash(String QRHash) {
        this.QRHash = QRHash;
    }
}