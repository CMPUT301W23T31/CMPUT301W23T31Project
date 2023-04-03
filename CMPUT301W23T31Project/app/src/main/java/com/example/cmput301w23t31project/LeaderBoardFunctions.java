package com.example.cmput301w23t31project;


import java.util.ArrayList;


public class LeaderBoardFunctions {
    /**
     * This method goes through each player in the list and gives them a rank
     */
    public void giveRank(ArrayList<Player> dataList){

        for(int i = 0;i < dataList.size();i++){
            int rank;
            rank = 1+i;
            dataList.get(i).setRank(rank);}
    }
    /**
     * sort the list depending on state
     */
    public void sortList(String state, ArrayList<Player> dataList) {
        for (int i = 0; i < dataList.size() - 1; i++)
            for (int j = 0; j < dataList.size() - i - 1; j++)
                if (state.equals("COUNT")&&dataList.get(j).getCount() <
                        dataList.get(j + 1).getCount()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);
                } else if (state.equals("HIGHSCORE")&& dataList.get(j).getHighestScoringQR() <
                        dataList.get(j + 1).getHighestScoringQR()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);
                } else if (state.equals("TOTALSCORE")&&dataList.get(j).getTotalScore() <
                        dataList.get(j + 1).getTotalScore()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);
                }
    }
    /**
     * If the state is Top codes then sort this list
     */
    public void sortCodeList(ArrayList<QRCode> codeList) {
        for (int i = 0; i < codeList.size() - 1; i++) {
            for (int j = 0; j < codeList.size() - i - 1; j++) {
                if (codeList.get(j).getScore() < codeList.get(j + 1).getScore()) {
                    QRCode temp = codeList.get(j);
                    codeList.set(j, codeList.get(j + 1));
                    codeList.set(j + 1, temp);
                }
            }
        }
    }
}
