package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState
     *      A bundle required to create the activity
     */
    private ArrayList<Player> playerList;
    private QRCodeStatsAdapter qrCodeStatsAdapter;
    String username;
    ListView datalist;
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
        playerList = new ArrayList<>();
        datalist = findViewById(R.id.qr_code_stats_scanned_by_list);
        qrCodeStatsAdapter = new QRCodeStatsAdapter(this, playerList);
        datalist.setAdapter(qrCodeStatsAdapter);
        QRCodesCollection qr_codes = new QRCodesCollection();

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
                                                      if(document.getId().equals(hash)){
                                                            setStats(document);}
                                                  }
                                              }
                                          }
                                      });
        setList(hash);
    }

        //QueryDocumentSnapshot document = qr_codes.getDocument(hash);

    public void setStats(DocumentSnapshot document) {

        // Add the required statistics to the text fields

        if (document != null) {
            //Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_SHORT).show();
            nameView.setText(document.getString("Name"));
            scoreView.setText(document.getString("Score"));
            String coordinates = document.getString("Latitude") + ", " +
                    document.getString("Longitude");
            String likes = document.getString("Likes") + " / " +
                    document.getString("Dislikes");
            coordinatesView.setText(coordinates);
            likesView.setText(likes);
            date.setText(document.getString("LastScanned"));
            scanned.setText(document.getString("TimesScanned"));
        }
    }

    public void setList(String hash){
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
                            //List<DocumentSnapshot> list = ;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if(document.getData().containsKey(hash)){
                                    int score =0;
                                    if(scoreView.getText().toString()!=""){
                                        score = Integer.parseInt(scoreView.getText().toString());
                                    }
                                    playerList.add(new Player(document.getId(),document.getId(),1,score));
                                }
                            }
                            qrCodeStatsAdapter.notifyDataSetChanged();
                        }
                    }});}


}
