package com.example.cmput301w23t31project;


import android.util.Log;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import java.util.Map;

/**
 * Class that stores a collection of a Player's Scans
 */
public class PlayerScansCollection extends QRDatabase{
    private FirebaseFirestore db;
    private String username;
    ArrayList<String> QRHash = new ArrayList<>();

    public PlayerScansCollection() {
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

        // Add necessary fields of QR code data
        HashMap<String, String> PlayerInfo = new HashMap<>();
        PlayerInfo.put("Total Score", SumScore);
        PlayerInfo.put("Total Scans", count);
        PlayerInfo.put("Lowest Scoring QR Code", min);
        PlayerInfo.put("Highest Scoring QR Code", max);
        PlayerInfo.put("Rank",rank);

        // Add the data to the database
        codes.document(username).set(PlayerInfo);
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
                        Log.i("VAL",document.getId());
                        if (document.getId().equals(username)) {

                            Map data = document.getData();
                            String str;
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
                                            //Log.v(TAG,"VAL:"+entry.toString().split("=")[0]));
                            QRHash.add(String.valueOf(data));
                            username = document.getId();

                            //processPlayerScansInDatabase(username);
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
                        String max2 = "";
                        String min2 = "";
                        String count2 = "";
                        String TotalScore = "";
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

                                        //score = String.valueOf(i);
                                        //Log.i("TAG", score);
                                        //Log.i("TAG", "This is the doc hash");
                                        //Log.i("TAG", document.getId());
                                        numberScore = Integer.parseInt(document.getString("Score"));
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

                                        // TODO: Clean up old debugging mess
                                        max2 = String.valueOf(max);
                                        //Log.i("MAX", max2);
                                        min2 = String.valueOf(min);
                                        //Log.i("min", min2);
                                        count++;
                                        count2 = String.valueOf(count);
                                        //Log.i("count", count2);
                                        TotalScore = String.valueOf(sumScore);
                                    }
                                }

                            addPlayerInfoToCollection(username,max2,min2,count2,TotalScore,rank2);

                    }
                }
        });
    }


}