package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


/**
 * This class displays statistics for a particular QR code that has been scanned at least once
 */
public class QRCodeStatsActivity extends AppCompatActivity {
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState
     *      A bundle required to create the activity
     */
    private ArrayList<Player> playerList;
    private QRCodeStatsAdapter qrCodeStatsAdapter;
    ListView datalist;
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        String hash = intent.getStringExtra("Hash");
        // Accesses all of the text fields
        playerList = new ArrayList<>();
        datalist = findViewById(R.id.qr_code_stats_scanned_by_list);
        qrCodeStatsAdapter = new QRCodeStatsAdapter(this, playerList);
        datalist.setAdapter(qrCodeStatsAdapter);
        TextView nameView = findViewById(R.id.qr_code_stats_code_name);
        TextView scoreView = findViewById(R.id.qr_code_stats_code_score);
        TextView coordinatesView = findViewById(R.id.qr_code_stats_code_coordinates);
        TextView likesView = findViewById(R.id.qr_code_stats_code_likes_dislikes);
        TextView date = findViewById(R.id.qr_code_stats_code_last_scanned_date);
        TextView scanned = findViewById(R.id.qr_code_stats_code_total_scans);
        QRCodesCollection qr_codes = new QRCodesCollection();
        // Add the required statistics to the text fields
        QueryDocumentSnapshot document = qr_codes.getDocument(hash);
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
