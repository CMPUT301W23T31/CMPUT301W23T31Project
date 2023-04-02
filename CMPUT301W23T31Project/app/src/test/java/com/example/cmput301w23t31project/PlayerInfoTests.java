package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import android.content.res.Resources;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class PlayerInfoTests {
    PlayerInfoSorts players = new PlayerInfoSorts();
    ArrayList<Player> peeps = generatePlayers();
    @Test
    public void highSortTest() {
        players.sortByHighScoreList(peeps);
        for (int i = 0; i < peeps.size()-1; i++) {
            assert(peeps.get(i).getHighestScoringQR() > peeps.get(i+1).getHighestScoringQR());
        }
    }

    @Test
    public void totalSortTest() {
        players.sortByTotalScoreList(peeps);
        for (int i = 0; i < peeps.size()-1; i++) {
            assert(peeps.get(i).getTotalScore() > peeps.get(i+1).getTotalScore());
        }
    }

    @Test
    public void countSortTest() {
        players.sortByCountList(peeps);
        for (int i = 0; i < peeps.size()-1; i++) {
            assert(peeps.get(i).getCount() > peeps.get(i+1).getCount());
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
}
