package com.example.cmput301w23t31project;


import java.util.ArrayList;


/**
 * This class holds methods regarding sorting data for player info statistics
 */
public class PlayerInfoSorts {

    /**
     * This function sorts player's by number of QR codes scanned
     * @param CountScoreDataList
     *      The list of players
     */
    public void sortByCountList(ArrayList<Player> CountScoreDataList) {
        for (int i = 0; i < CountScoreDataList.size() - 1; i++)
            for (int j = 0; j < CountScoreDataList.size() - i - 1; j++)
                if (CountScoreDataList.get(j).getCount() <
                        CountScoreDataList.get(j + 1).getCount()) {
                    Player temp = CountScoreDataList.get(j);
                    CountScoreDataList.set(j, CountScoreDataList.get(j + 1));
                    CountScoreDataList.set(j + 1, temp);
                }
    }

    /**
     * This function sorts player's total score
     * @param TotalScoreDataList
     *      The list of players
     */
    public void sortByTotalScoreList(ArrayList<Player> TotalScoreDataList) {
        for (int i = 0; i < TotalScoreDataList.size() - 1; i++)
            for (int j = 0; j < TotalScoreDataList.size() - i - 1; j++)
                if (TotalScoreDataList.get(j).getTotalScore() <
                        TotalScoreDataList.get(j + 1).getTotalScore()) {
                    Player temp = TotalScoreDataList.get(j);
                    TotalScoreDataList.set(j, TotalScoreDataList.get(j + 1));
                    TotalScoreDataList.set(j + 1, temp);
                }
    }

    /**
     * This function sorts player's highest scoring QR card
     * @param HighScoreDataList
     *      The list of players
     */
    public void sortByHighScoreList(ArrayList<Player> HighScoreDataList) {
        for (int i = 0; i < HighScoreDataList.size() - 1; i++)
            for (int j = 0; j < HighScoreDataList.size() - i - 1; j++)
                if (HighScoreDataList.get(j).getHighestScoringQR() <
                        HighScoreDataList.get(j + 1).getHighestScoringQR()) {
                    Player temp = HighScoreDataList.get(j);
                    HighScoreDataList.set(j, HighScoreDataList.get(j + 1));
                    HighScoreDataList.set(j + 1, temp);
                }
    }
}
