package com.example.cmput301w23t31project;


/**
 * Represents a single QR Code object
 */
public class QRCode {
    private String name;
    private int score;
    private String hash;
    private double distance;

    /**
     * Creates a new instance of a QR code
     * @param name human-readable name of QR code
     * @param score QR code's score
     * @param hash SHA-256 hash of QR code
     */
    QRCode(String name, int score, String hash) {
        this.name = name;
        this.score = score;
        this.hash =hash;
    }
    QRCode(String name, int score, String hash, double distance) {
        this.name = name;
        this.score = score;
        this.hash =hash;
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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
