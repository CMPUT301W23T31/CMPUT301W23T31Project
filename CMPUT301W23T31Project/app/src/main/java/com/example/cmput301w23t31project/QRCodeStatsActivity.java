package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * This class displays statistics for a particular QR code that has been scanned at least once
 */
public class QRCodeStatsActivity extends AppCompatActivity {
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState
     *      A bundle required to create the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        String hash = intent.getStringExtra("Hash");
        // Accesses all of the text fields
        TextView nameView = findViewById(R.id.qr_code_stats_code_name);
        TextView scoreView = findViewById(R.id.qr_code_stats_code_score);
        TextView coordinatesView = findViewById(R.id.qr_code_stats_code_coordinates);
        TextView likesView = findViewById(R.id.qr_code_stats_code_likes_dislikes);
        TextView date = findViewById(R.id.qr_code_stats_code_last_scanned_date);
        TextView scanned = findViewById(R.id.qr_code_stats_code_total_scans);
        CollectionReference qr_codes = db.collection("QRCodes");
        // Add the required statistics to the text fields
        qr_codes.document(hash).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doc) {
                nameView.setText(doc.getString("Name"));
                scoreView.setText(doc.getString("Score"));
                String coordinates = doc.getString("Latitude") + ", " +
                        doc.getString("Longitude");
                String likes = doc.getString("Likes") + " / " +
                        doc.getString("Dislikes");
                coordinatesView.setText(coordinates);
                likesView.setText(likes);
                date.setText(doc.getString("LastScanned"));
                scanned.setText(doc.getString("TimesScanned"));
            }
        });
    }
}
