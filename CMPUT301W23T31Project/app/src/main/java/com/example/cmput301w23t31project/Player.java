package com.example.cmput301w23t31project;


import java.io.Serializable;


/**
 * Represents a single account of someone using the app (anyone who plays the game)
 * Also stores relevant quick-access stats to improve database efficiency
 *
 */
public class Player implements Serializable {
    private String username;
    private String playerName;

    private int count;
    private int totalScore;
    private int rank;
    private String date;

    private int highestScoringQR;
    private int lowestScoringQR;

    /**
     * Instantiates new instance of Player (standard)
     * @param username username of player (uniquely identifiable)
     * @param playerName public name of player associated with account
     * @param count number of lifetime QR codes scanned by player
     * @param score total score of player (sum of all scanned QR codes' scores)
     */
    public Player(String username, String playerName, int count, int score) {
        this.username = username;
        this.playerName = playerName;
        this.count = count;
        this.totalScore = score;
    }

    /**
     * Instantiates new instance of Player (full)
     * @param username username of player (uniquely identifiable)
     * @param count number of lifetime QR codes scanned by player
     * @param score total score of player (sum of all scanned QR codes' scores)
     * @param highestScoringQR score of highest-scoring QR code scanned by player
     * @param lowestScoringQR score of lowest-scoring QR code scanned by player
     * @param rank rank position by total score (position in global leaderboard)
     */
    public Player(String username, int count, int score, int highestScoringQR, int lowestScoringQR,int rank) {
        this.username = username;
        this.count = count;
        this.totalScore = score;
        this.highestScoringQR = highestScoringQR;
        this.lowestScoringQR = lowestScoringQR;
        this.rank = rank;
    }

    /**
     * Instantiates new instance of Player (basic)
     * @param username username of player (uniquely identifiable)
     * @param date date account was created
     */
    public Player(String username, String date) {
        this.username = username;
        this.date = date;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getDate() {
        return date;
    }

    public int getHighestScoringQR() {
        return highestScoringQR;
    }

    public int getLowestScoringQR() {
        return lowestScoringQR;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
