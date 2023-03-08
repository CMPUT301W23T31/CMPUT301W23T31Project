package com.example.cmput301w23t31project;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Splitter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import java.util.Map;
import java.util.Objects;

public class PlayerScansCollection extends QRDatabase{
    String hash_return;
    private FirebaseFirestore db;


    private String username;
    private String checkhash;
    ArrayList<String> qrHash = new ArrayList<>();
    public PlayerScansCollection() {
        super("PlayerInfo");
    }


    public void addPlayerInfoToCollection(String username, String max, String min ,String count,String SumScore) {
        CollectionReference codes = getReference();
        // Add necessary fields of QR code data
        HashMap<String, String> PlayerInfo = new HashMap<>();
        PlayerInfo.put("Total Score", SumScore);
        PlayerInfo.put("Total Scans", count);
        PlayerInfo.put("Lowest Scoring QR Code", min);
        PlayerInfo.put("Highest Scoring QR Code", max);

        // Add the data to the database
        codes.document(username).set(PlayerInfo);
    }
    


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

                                    qrHash.add(String.valueOf(data));

                        }
                    }

                }
            }
        });
    }
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
                            //for(int i = 0; i< list.size();i++){
                                //username = list.get(i).getId();
                                for (DocumentSnapshot document : list) {
                                    Map data = document.getData();
                                    data.entrySet()
                                            .forEach((entry) ->
                                                    qrHash.add(entry.toString().split("=")[0]));
                                                    //Log.v(TAG,"VAL:"+entry.toString().split("=")[0]));
                                    qrHash.add(String.valueOf(data));
                                    username = document.getId();

                                    //processPlayerScansInDatabase(username);
                                    getTotalPlayerScore(username,qrHash);
                                    qrHash = new ArrayList<>();
                                    }




                            }}
                });
    }


    public void getTotalPlayerScore (String userName,ArrayList<String> qrHash){
        db = FirebaseFirestore.getInstance();

        db.collection("QRCodes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i("TAG",userName);
                        int SumScore = 0;
                        String score = "";
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

                                    if(qrHash.contains(document.getId())){

                                        //score = String.valueOf(i);
                                        //Log.i("TAG", score);
                                        Log.i("TAG", "This is the doc hash");
                                        Log.i("TAG", document.getId());
                                        numberScore = Integer.parseInt(document.getString("Score"));
                                        if(min == 0){
                                            min = numberScore;}
                                        SumScore = SumScore + numberScore;
                                        if(max < numberScore){
                                            max = numberScore;
                                        }
                                        if(min > numberScore){
                                            min = numberScore;
                                        }
                                        max2 = String.valueOf(max);
                                        Log.i("MAX", max2);
                                        min2 = String.valueOf(min);
                                        Log.i("min", min2);
                                        count++;
                                        count2 = String.valueOf(count);
                                        Log.i("count", count2);
                                        TotalScore = String.valueOf(SumScore);
                                    }


                        }


                            addPlayerInfoToCollection(userName,max2,min2,count2,TotalScore);

                    }}
    });

}


}