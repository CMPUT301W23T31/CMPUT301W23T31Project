package com.example.cmput301w23t31project;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;


/**
 * This class displays statistics for a particular QR code that has been scanned at least once
 */
public class QRCodeStatsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    TextView nameView;
    TextView scoreView;
    TextView coordinatesView;
    TextView likesView;
    TextView date;
    TextView scanned;
    TextView dislikesView;
    Button gotoComments;
    String hash;
    Button dislikebtn;
    Button likebtn;
    String coordinates;
    ArrayList<Player> playerList;
    private QRCodeStatsAdapter qrCodeStatsAdapter;
    String username;
    ListView datalist;
    DrawRepresentation visualRepresentation;
    Button viewSurroundings;
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState a bundle required to create the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("QR STATS");
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        hash = intent.getStringExtra("Hash");
        username = intent.getStringExtra("username");
        Log.d("TAG", hash+" stats  "+ username);

        // Accesses all of the text fields
        nameView = findViewById(R.id.qr_code_stats_code_name);
        scoreView = findViewById(R.id.qr_code_stats_code_score);
        coordinatesView = findViewById(R.id.qr_code_stats_code_coordinates);
        likesView = findViewById(R.id.qr_code_stats_code_likes_dislikes);
        date = findViewById(R.id.qr_code_stats_code_last_scanned_date);
        scanned = findViewById(R.id.qr_code_stats_code_total_scans);
        gotoComments = findViewById(R.id.qr_code_stats_comment_list_button);
        datalist = findViewById(R.id.qr_code_stats_scanned_by_list);
        dislikebtn = findViewById(R.id.qr_code_stats_comment_dislike_button);
        likebtn = findViewById(R.id.qr_code_stats_comment_like_button);
        // Setting up listview
        playerList = new ArrayList<>();
        qrCodeStatsAdapter = new QRCodeStatsAdapter(this, playerList, username);
        datalist.setAdapter(qrCodeStatsAdapter);
        QRCodesCollection qr_codes = new QRCodesCollection();
        viewSurroundings = findViewById(R.id.qr_code_stats_comments_view_surroundings);
        // generating and displaying visual representation
        View representationView = findViewById(R.id.qr_code_stats_visual_representation_view);
        visualRepresentation = new DrawRepresentation(hash, 80);
        representationView.setForeground(visualRepresentation);

        // handles functionality for going to QR code info comment view
        gotoComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeStatsActivity.this,
                        QRCodeStatsCommentsActivity.class);
                intent.putExtra("Hash", hash);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        dislikebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HitLikeDislike(1);
            }
        });
        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HitLikeDislike(0);
            }
        });

        setStats(hash);
        //Toast.makeText(getApplicationContext(),"hashstats: "+hash,Toast.LENGTH_SHORT).show();
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
                                  Log.d("TAG",  document.getData().keySet()+"   "+hash);
                                  if(document.getData().containsKey(hash)){
                                      Log.d("TAG", "Reached inside setting stats found doc");
                                      setList(document.getId());}

                              }
                          }
                      }
                });
        //setList(username);
        viewSurroundings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeStatsActivity.this, SurroundingsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates all text fields about all QR code stats
     * @param hash DocumentSnapshot object containing all needed information
     */
    public void setStats(String hash) {
        db = FirebaseFirestore.getInstance();
        db.collection("QRCodes").get()
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
                                // Adding the required statistics to the text fields
                                if (document != null) {
                                    if(document.getId().equals(hash)){
                                        Log.d("TAG", "Reached inside setting statsijgsoigjaslkg");
                                    nameView.setText(document.getString("Name"));
                                    scoreView.setText(document.getString("Score"));
                                    if((Double.valueOf(document.getString("Latitude"))==200)){
                                        coordinates = "No Location";

                                    }else{
                                        coordinates = document.getString("Latitude") + ", " + document.getString("Longitude");
                                    }
                                    String likes = document.getString("Likes") + " / " + document.getString("Dislikes");
                                    coordinatesView.setText(coordinates);
                                    likesView.setText(likes);
                                    date.setText(document.getString("LastScanned"));
                                    scanned.setText(document.getString("TimesScanned"));
                                    }


        }
    }}}});
    }

    /**
     * sets and updates relevant listview for given QR code
     * @param username relevant QR code's identifying username
     */
    public void setList(String username){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get()
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
                            //List<DocumentSnapshot> list = ;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if(document.getId().equals(username)){
                                    Log.i("TAG","TESTING SCAN");
                                    int score = 0;
                                    if(!scoreView.getText().toString().equals("")){
                                        score = Integer.parseInt(scoreView.getText().toString());
                                    }
                                    int totalScore = Integer.parseInt(document.getString("Total Score"));
                                    int totalScans = Integer.parseInt(document.getString("Total Scans"));
                                    int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                                    int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                                    int rank = Integer.parseInt(document.getString("Rank"));
                                    playerList.add(new Player(document.getId(), totalScans,
                                            totalScore, highestScoringQR, lowestScoringQR, rank));
                                }
                            }
                            qrCodeStatsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public void HitLikeDislike(int number){


        //DocumentReference scan = QRdb.collection("PlayerInfo").document(username);
        Map<String, Object> m = new HashMap<>();
        db = FirebaseFirestore.getInstance();
        TextView LikesDislikesText = findViewById(R.id.qr_code_stats_code_likes_dislikes);
        String numberOfLikes = LikesDislikesText.getText().toString();
        String likesStr = "0";
        String LikesFinished = processString(numberOfLikes,number);
        String DisLikesFinished = processString(numberOfLikes,number);


        switch (number){
            case 0: {
                int likes1 = Integer.parseInt(LikesFinished);
                int likes2 = likes1 + 1;
                likesStr = (LikesFinished + " / " + DisLikesFinished);
                String likes = String.valueOf(likes2);
                m.put("Likes", likes);
                break;
            }
            case 1:{
                int dislikes1 = Integer.parseInt(DisLikesFinished);
                int dislikes2 = dislikes1 + 1;
                likesStr = (LikesFinished + " / " + DisLikesFinished);
                String dislikes = String.valueOf(dislikes2);
                m.put("Dislikes", dislikes);
                break;
            }

        }
        String finalLikesStr = likesStr;
        db.collection("QRCodes").document(hash)
                .set(m, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setStats(hash);
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
    public String processString(String string,int number) {

        String LikesFinished = "0";
        String DisLikesFinished ="0";


        switch (number) {
            case 0:{
                String[] numberOflikesArr = string.split("/");
                String numberOfLikesWSpace = numberOflikesArr[number];
                String[] numberOfLikesWOSpace = numberOfLikesWSpace.split(" ");
                LikesFinished = numberOfLikesWOSpace[number];
                break;}
            case 1:{
                String[] numberOfDislikesArr = string.split("/");
                String numberOfDisLikesWSpace = numberOfDislikesArr[1];
                String[] numberOfDisLikesWOSpace = numberOfDisLikesWSpace.split(" ");
                DisLikesFinished = numberOfDisLikesWOSpace[1];
                break;}
            }
        if(number==0){
            return LikesFinished;
        }
        else{
            return DisLikesFinished;
        }

    }



}
