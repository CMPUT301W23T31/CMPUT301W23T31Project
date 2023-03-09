package com.example.cmput301w23t31project;

public class Player {
    private String userName;
    private String playerName;
    private int count;
    private int totalScore;

    private String date;

    private int highestScoringQR;
    private int lowestScoringQR;


    public Player(String userName, String playerName, int count, int score) {
        this.userName = userName;
        this.playerName = playerName;
        this.count = count;
        this.totalScore = score;
    }

    public Player(String userName, int count, int score, int highestScoringQR, int lowestScoringQR) {
        this.userName = userName;
        this.count = count;
        this.totalScore = score;
        this.highestScoringQR = highestScoringQR;
        this.lowestScoringQR = lowestScoringQR;
    }

    public Player(String userName, String date) {
        this.userName = userName;
        this.date = date;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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


