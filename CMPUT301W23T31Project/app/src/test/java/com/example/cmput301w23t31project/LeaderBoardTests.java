package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import android.content.res.Resources;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LeaderBoardTests {
    ArrayList<Player> players = generatePlayers();
    ArrayList<QRCode> codes = generateCodes();
    LeaderBoardFunctions func = new LeaderBoardFunctions();
    @Test
    public void sortPlayersByCountTest() {
        func.sortList("COUNT", players);
        for (int i = 0; i < players.size()-1; i++) {
            assert(players.get(i).getCount() >= players.get(i+1).getCount());
        }
    }

    @Test
    public void sortPlayersByHighTest() {
        func.sortList("HIGHSCORE", players);
        for (int i = 0; i < players.size()-1; i++) {
            assert(players.get(i).getHighestScoringQR() >= players.get(i+1).getHighestScoringQR());
        }
    }

    @Test
    public void sortPlayersByTotalTest() {
        func.sortList("TOTALSCORE", players);
        for (int i = 0; i < players.size()-1; i++) {
            assert(players.get(i).getTotalScore()) >= players.get(i+1).getTotalScore();
        }
    }

    @Test
    public void sortCodeTest() {
        func.sortCodeList(codes);
        for (int i = 0; i < codes.size()-1; i++) {
            assert(codes.get(i).getScore() >= codes.get(i+1).getScore());
        }
    }

    @Test
    public void rankTest() {
        func.sortList("COUNT", players);
        func.giveRank(players);
        for (int i = 0; i < players.size()-1; i++) {
            assert(players.get(i).getRank() < players.get(i+1).getRank());
        }
    }

    public ArrayList<Player> generatePlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("A",4,25,8,5,5));
        players.add(new Player("A",12,66,7,4,3));
        players.add(new Player("A",8,500,200,12,2));
        players.add(new Player("A",47,3844,400,27,1));
        players.add(new Player("A",1,50,50,50,4));
        return players;
    }

    public ArrayList<QRCode> generateCodes() {
        ArrayList<QRCode> codes = new ArrayList<>();
        codes.add(new QRCode("Happy", 77, "a"));
        codes.add(new QRCode("Happy", 0, "a"));
        codes.add(new QRCode("Happy", 47, "a"));
        codes.add(new QRCode("Happy", 300, "a"));
        codes.add(new QRCode("Happy", 299, "a"));
        return codes;
    }

}
