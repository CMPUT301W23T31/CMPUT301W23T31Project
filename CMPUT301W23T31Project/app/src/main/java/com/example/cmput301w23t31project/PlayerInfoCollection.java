package com.example.cmput301w23t31project;


import android.os.Handler;
import android.util.Log;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import java.util.Map;


/**
 * Class that stores a collection of a Player's Scans
 */
public class PlayerInfoCollection extends QRDatabase{
    private FirebaseFirestore db;

    private ArrayList<Player> HighScoreDataList = new ArrayList<>();
    private ArrayList<Player> CountScoreDataList = new ArrayList<>();
    private ArrayList<Player> TotalScoreDataList = new ArrayList<>();
    private String username;
    ArrayList<String> QRHash = new ArrayList<>();
    //HashMap<String, String> PlayerInfo = new HashMap<>();


    public PlayerInfoCollection() {
        super("PlayerInfo");
    }

    /**
     * Adds a player (and their info) to the database
     * @param username username of player to add
     * @param max highest scoring QR Code scan
     * @param min lowest scoring QR Code scan
     * @param count number of QR codes scanned
     * @param SumScore total score of all QR codes scanned
     * @param rank global ranking of player (by total score)
     */
    public void addPlayerInfoToCollection(String username, String max, String min, String count, String SumScore, String rank) {
        CollectionReference codes = getReference();
        String temp = "test";

        // Add necessary fields of QR code data
        HashMap<String, String> PlayerInfo = new HashMap<>();
        PlayerInfo.put("Total Score", SumScore);
        PlayerInfo.put("Total Scans", count);
        PlayerInfo.put("Lowest Scoring QR Code", min);
        //Log.i("The Rank",rank);
        PlayerInfo.put("Highest Scoring QR Code", max);
        PlayerInfo.put("Rank",rank);
        //PlayerInfo.put("field test",temp);
        PlayerInfo.put("High Score Rank",rank);
        //Log.i("TAG","field");
        PlayerInfo.put("Count Score Rank",rank);
        PlayerInfo.put("Total Score Rank",rank);

        // Add the data to the database
        codes.document(username).set(PlayerInfo);

    }
    public void addHighScoreRank(String username, String HighScoreRank) {
        String userName = username;
        CollectionReference scans = getReference();
        //Log.i("TAG","test2");
        //DocumentReference scan = QRdb.collection("PlayerInfo").document(username);
        Map<String, Object> m = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        //Log.i("The High Rank",HighScoreRank);
        m.put("High Score Rank", HighScoreRank);
        //Log.i("TAG",userName);
        db.collection("PlayerInfo").document(userName)
                .set(m, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "HighScore Rank successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "HighScore Rank  Error writing document", e);
                    }
                });
    }
    public void addCountScoreRank(String username, String CountScoreRank){
        String userName = username;
        CollectionReference scans = getReference();
        //Log.i("TAG","test2");
        //DocumentReference scan = QRdb.collection("PlayerInfo").document(username);
        Map<String, Object> m = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        //Log.i("The Count Rank",CountScoreRank);
        m.put("Count Score Rank", CountScoreRank);
        //Log.i("TAG",userName);
        db.collection("PlayerInfo").document(userName)
                .set(m, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public void addTotalScoreRank(String username, String TotalScoreRank){
        String userName = username;
        CollectionReference scans = getReference();
        //Log.i("TAG","test2");
        //DocumentReference scan = QRdb.collection("PlayerInfo").document(username);
        Map<String, Object> m = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        //Log.i("TAG",userName);
        //Log.i("The Total Rank",TotalScoreRank);
        m.put("Total Score Rank", TotalScoreRank);
        //Log.i("TAG",userName);
        db.collection("PlayerInfo").document(userName)
                .set(m, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    /**
     * Processes and gets scans from database that player has scanned
     * @param username username of relevant player
     */
    public void processPlayerScansInDatabase(String username) {

        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        //Log.i("VAL",document.getId());
                        if (document.getId().equals(username)) {

                            Map data = document.getData();
                            data.entrySet()
                                    .forEach((entry) ->
                                            Log.v(TAG,"VAL:"+entry.toString().split("=")[0]));

                            QRHash.add(String.valueOf(data));

                        }
                    }

                }
            }
        });
    }

    /**
     * Gets the players' scans from database
     */
    public void getPlayerScans(){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerScans").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    // after getting the data we are calling on success method
                    // and inside this method we are checking if the received
                    // query snapshot is empty or not.
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // if the snapshot is not empty we are
                        // hiding our progress bar and adding
                        // our data in a list.
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                        for (DocumentSnapshot document : list) {
                            Map data = document.getData();
                            data.entrySet()
                                    .forEach((entry) ->
                                            QRHash.add(entry.toString().split("=")[0]));
                            QRHash.add(String.valueOf(data));
                            username = document.getId();
                            getTotalPlayerScore(username, QRHash);
                            QRHash = new ArrayList<>();
                            }

                        }
                    }
                });
    }


    /**
     * Gets the total score of player, by QR code
     * @param username username of relevant player (whose score is being retrieved)
     * @param QRHash hash or relevant QR code
     */
    public void getTotalPlayerScore (String username, ArrayList<String> QRHash){
        db = FirebaseFirestore.getInstance();

        db.collection("QRCodes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //Log.i("TAG",username);
                        int sumScore = 0;
                        String score = "";
                        int rank = 0;
                        String rank2 = String.valueOf(rank);
                        int max = 0;
                        int min = 0;
                        int numberScore;
                        int count = 0;
                        String max2 = "0";
                        String min2 = "0";
                        String count2 = "0";
                        String TotalScore = "0";
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot document : list) {

                                    if(QRHash.contains(document.getId())){
                                        numberScore = Integer.parseInt(document.getString("Score"));
                                        //Log.i("Score",document.getString("Score"));
                                        sumScore = sumScore + numberScore;
                                        if(min == 0){
                                            min = numberScore;
                                        }
                                        if(max < numberScore){
                                            max = numberScore;
                                        }
                                        if(min > numberScore){
                                            min = numberScore;
                                        }


                                        max2 = String.valueOf(max);
                                        min2 = String.valueOf(min);
                                        count++;
                                        count2 = String.valueOf(count);
                                        TotalScore = String.valueOf(sumScore);
                                    }
                                }

                            addPlayerInfoToCollection(username,max2,min2,count2,TotalScore,rank2);

                    }
                }
        });
    }
    public void CreateLeaderBoard(){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get() .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // after getting the data we are calling on success method
                // and inside this method we are checking if the received
                // query snapshot is empty or not.
                if (!queryDocumentSnapshots.isEmpty()) {
                    // if the snapshot is not empty we are
                    // hiding our progress bar and adding
                    // our data in a list.
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot document : list) {
                        int i = 0;
                        String userName = document.getId();
                        int totalScore = Integer.parseInt(document.getString("Total Score"));
                        int totalScans = Integer.parseInt(document.getString("Total Scans"));
                        int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                        int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                        int rank = Integer.parseInt(document.getString("Rank"));
                        HighScoreDataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));
                        CountScoreDataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));
                        TotalScoreDataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));
                        i++;
                    }
                    sortByTotalScoreList();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sortByTotalScoreList();
                        }
                    },1000);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sortByCountList();

                        }
                    },1000);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            sortByHighScoreList();
                        }
                    },1000);


                }}});


    }



    public void sortByCountList() {
        for (int i = 0; i < CountScoreDataList.size() - 1; i++)
            for (int j = 0; j < CountScoreDataList.size() - i - 1; j++)
                if (CountScoreDataList.get(j).getCount() < CountScoreDataList.get(j + 1).getCount()) {
                    Player temp = CountScoreDataList.get(j);
                    CountScoreDataList.set(j, CountScoreDataList.get(j + 1));
                    CountScoreDataList.set(j + 1, temp);

                }
        giveCountRank();

    }
    public void sortByTotalScoreList() {
        for (int i = 0; i < TotalScoreDataList.size() - 1; i++)
            for (int j = 0; j < TotalScoreDataList.size() - i - 1; j++)
                if (TotalScoreDataList.get(j).getTotalScore() < TotalScoreDataList.get(j + 1).getTotalScore()) {
                    Player temp = TotalScoreDataList.get(j);
                    TotalScoreDataList.set(j, TotalScoreDataList.get(j + 1));
                    TotalScoreDataList.set(j + 1, temp);

                }
        giveTotalScoreRank();
    }
    public void sortByHighScoreList() {
        for (int i = 0; i < HighScoreDataList.size() - 1; i++)
            for (int j = 0; j < HighScoreDataList.size() - i - 1; j++)
                if (HighScoreDataList.get(j).getHighestScoringQR() < HighScoreDataList.get(j + 1).getHighestScoringQR()) {
                    Player temp = HighScoreDataList.get(j);
                    HighScoreDataList.set(j, HighScoreDataList.get(j + 1));
                    HighScoreDataList.set(j + 1, temp);

                }
        giveHighScoreRank();
    }
    public void giveCountRank(){
        for(int i = 0;i < CountScoreDataList.size();i++){
            int rank;
            rank = 1+i;
            String username = CountScoreDataList.get(i).getUsername();
            String CountRank = String.valueOf(rank);
            addCountScoreRank(username,CountRank);
        }
    }
    public void giveHighScoreRank(){
        for(int i = 0;i < HighScoreDataList.size();i++){
            int rank;
            rank = 1+i;
            String username = HighScoreDataList.get(i).getUsername();
            String HighScoreRank = String.valueOf(rank);
            addHighScoreRank(username,HighScoreRank);
        }
    }
    public void giveTotalScoreRank(){
        for(int i = 0;i < TotalScoreDataList.size();i++){
            int rank;
            rank = 1+i;
            String username = TotalScoreDataList.get(i).getUsername();
            String TotalScoreRank = String.valueOf(rank);
            addTotalScoreRank(username,TotalScoreRank);
        }
    }

}