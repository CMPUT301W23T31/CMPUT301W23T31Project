package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import java.util.ArrayList;



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
    Button gotoComments;
    String hash;

    private ArrayList<Player> playerList;
    private QRCodeStatsAdapter qrCodeStatsAdapter;
    String username;
    ListView datalist;
    DrawRepresentation visualRepresentation;

    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState a bundle required to create the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        hash = intent.getStringExtra("Hash");
        username = intent.getStringExtra("username");

        // Accesses all of the text fields
        nameView = findViewById(R.id.qr_code_stats_code_name);
        scoreView = findViewById(R.id.qr_code_stats_code_score);
        coordinatesView = findViewById(R.id.qr_code_stats_code_coordinates);
        likesView = findViewById(R.id.qr_code_stats_code_likes_dislikes);
        date = findViewById(R.id.qr_code_stats_code_last_scanned_date);
        scanned = findViewById(R.id.qr_code_stats_code_total_scans);
        gotoComments = findViewById(R.id.qr_code_stats_comment_list_button);
        datalist = findViewById(R.id.qr_code_stats_scanned_by_list);

        // Setting up listview
        playerList = new ArrayList<>();
        qrCodeStatsAdapter = new QRCodeStatsAdapter(this, playerList, username);
        datalist.setAdapter(qrCodeStatsAdapter);
        QRCodesCollection qr_codes = new QRCodesCollection();

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

        ///
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
                                  if(document.getData().containsKey(hash)){
                                      setList(document.getId());
                                      setStats(hash);}

                              }
                          }
                      }
                });
        //setList(username);
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
                                    nameView.setText(document.getString("Name"));
                                    scoreView.setText(document.getString("Score"));

                                    String coordinates = document.getString("Latitude") + ", " + document.getString("Longitude");
                                    String likes = document.getString("Likes") + " / " + document.getString("Dislikes");
                                    coordinatesView.setText(coordinates);
                                    likesView.setText(likes);}}

            date.setText(document.getString("LastScanned"));
            scanned.setText(document.getString("TimesScanned"));
        }
    }}});}

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


}
