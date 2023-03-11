package com.example.cmput301w23t31project;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//TODO: DETERMINE IF WE NEED THIS CLASS (CLAIMED TO BE UNUSED)
public class SortLeaderBoard {
    private FirebaseFirestore db;

    /**
     * Creates a leaderboard of player profiles that can be sorted on any attribute
     * @param dataList list to add data-filled players to (to use for leaderboard)
     */
    public void CreateLeaderBoard(ArrayList<Player> dataList){
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
                        String userName = document.getId();
                        //Log.i("TAG", userName);
                        //Log.i("TAG", document.getId());
                        int totalScore = Integer.parseInt(document.getString("Total Score"));
                        int totalScans = Integer.parseInt(document.getString("Total Scans"));
                        int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                        int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                        int rank = Integer.parseInt(document.getString("Rank"));
                        dataList.add(new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));

                    }
                }

            }
        });

    }
}