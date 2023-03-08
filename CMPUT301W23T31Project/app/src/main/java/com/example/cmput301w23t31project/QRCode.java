package com.example.cmput301w23t31project;

public class QRCode {
    private String name;
    private int score;
    private String hash;

    // private QRCodeImage;

    QRCode(String name, int score, String hash) {
        this.name = name;
        this.score = score;
        this.hash =hash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
