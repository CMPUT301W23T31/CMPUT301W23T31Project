package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


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
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState
     *      A bundle required to create the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        String hash = intent.getStringExtra("hash");
        // Accesses all of the text fields
        nameView = findViewById(R.id.qr_code_stats_code_name);
        scoreView = findViewById(R.id.qr_code_stats_code_score);
        coordinatesView = findViewById(R.id.qr_code_stats_code_coordinates);
        likesView = findViewById(R.id.qr_code_stats_code_likes_dislikes);
        date = findViewById(R.id.qr_code_stats_code_last_scanned_date);
        scanned = findViewById(R.id.qr_code_stats_code_total_scans);

        QRCodesCollection qr_codes = new QRCodesCollection();

        ///
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
                                                      document.getId().equals(hash);
                                                      setStats(document);
                                                  }
                                              }
                                          }
                                      });
    }
        ////


        // Add the required statistics to the text fields
        //QueryDocumentSnapshot document = qr_codes.getDocument(hash);

    public void setStats(DocumentSnapshot document) {
        if (document != null) {
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
}
