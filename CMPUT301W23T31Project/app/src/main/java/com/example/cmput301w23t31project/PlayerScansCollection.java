package com.example.cmput301w23t31project;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

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

    public void addPlayerInfoToCollection(String username, Intent intent, int max, int min ,int count,int SumScore) {
        HashMap<String, String> PlayerInfo = new HashMap<>();
        PlayerInfo.put("Username", username);
        collection.document(username).set(PlayerInfo);
                PlayerInfo.put("email", intent.getStringExtra("email"));
        collection.document(username).set(PlayerInfo);
        PlayerInfo.put("phone", intent.getStringExtra("phone"));
        collection.document(username).set(PlayerInfo);
        PlayerInfo.put("playername", intent.getStringExtra("playername"));
        collection.document(username).set(PlayerInfo);
        PlayerInfo.put("path", intent.getStringExtra("path"));
        collection.document(username).set(PlayerInfo);
    }
    public void processPlayerScansInDatabase(String hash) {
        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(hash)) {

                            return;
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

                                    //username = document.getId();
                                    for(int i = 0 ;i < document.getData().size();i++){
                                        String hashish = document.getData().toString();
                                    //Log.i("TAG", hashish);
                                        qrHash.add(hashish);
                                        Log.i("TAG","this is the hashish");
                                        Log.i("TAG",hashish);}
                                //Log.i("TAG", username);
                                    username = document.getId();
                                    //getTotalPlayerScore(username,qrHash);
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
                        int SumScore = 0;
                        String score = "";
                        int max;
                        int min;
                        int numberScore;
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
                                    max = 0;
                                    min = 0;
                                    String XD = qrHash.get(i);
                                    Log.i("TAG", userName);
                                    Log.i("TAG", "This is the player hash list");
                                    Log.i("TAG", XD);
                                    Log.i("TAG", "This is the doc hash");
                                    Log.i("TAG", document.getId());
                                    if(i < list.size())
                                        i++;
                                    if(qrHash.contains(document.getId())){
                                        //score = String.valueOf(i);
                                        //Log.i("TAG", score);
                                        Log.i("TAG", "This is the doc hash");
                                        Log.i("TAG", document.getId());
                                        numberScore = Integer.parseInt(document.getString("Score"));
                                        SumScore = SumScore + numberScore;
                                        if(max < numberScore){
                                            max = numberScore;
                                        }
                                        if(min > numberScore){
                                            min = numberScore;
                                        }
                                        score = String.valueOf(SumScore);}


                        }
                                Log.i("TAG", score);

                    }}
    });

}


}