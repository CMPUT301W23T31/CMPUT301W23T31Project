package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activity for "Player Info" Screen
 */


    

public class PlayerInfoScreenActivity extends HamburgerMenu {

    private FirebaseFirestore db;
    Button viewScanBtn;
    ImageButton myAccountBtn;
    TextView player_info_username;
    TextView player_scans_textview;
    TextView playerTotalScoreRank;
    TextView playerHighScoreRank;
    TextView high_score;
    TextView low_score;
    TextView score;
    private String username;
    String password;

    /**
     * On Create method
     * Sets up button functionality and player stats to display
     * @param savedInstanceState previously saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info_screen);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        // getting needed attributes
        viewScanBtn = findViewById(R.id.player_info_see_scans_button);
        myAccountBtn = findViewById(R.id.player_info_my_account_button);
        player_info_username = findViewById(R.id.player_info_username);
        player_scans_textview = findViewById(R.id.player_info_total_scans);
        score = findViewById(R.id.player_info_total_score);
        high_score  = findViewById(R.id.player_info_high_score);
        low_score = findViewById(R.id.player_info_low_score);

        //set total scans
        QRPlayerScans playerScans = new QRPlayerScans();
        setTotalScans(playerScans, player_scans_textview, username);

        //set total score
        QRCodesCollection QRCodes = new QRCodesCollection();
        setHomeScore(playerScans, score, QRCodes, username);

        //set high score
        setHighScore(playerScans, high_score, QRCodes, username);
        setLowScore(playerScans, low_score, QRCodes, username);

        //set ranks
        setRank();

        // set player username
        player_info_username.setText(username);

        // 'my scans' button functionality
        viewScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PlayerScansCollection scans = new PlayerScansCollection();
                //scans.processPlayerScansInDatabase(username);
                Intent intent = new Intent(PlayerInfoScreenActivity.this, MyScansScreenActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("crnt_username", username);
                startActivity(intent);
            }
        });

        // 'my account' button functionality
        myAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerInfoScreenActivity.this, MyAccountScreenActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * For creating the options menu
     * @param menu menu to create
     * @return boolean of whether to display or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    /**
     * Delegates functionality when item is chosen from menu
     * @param item item chosen from menu
     * @return boolean on whether to proceed or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);

    }

    /**
     * Handles the setting of the player's score (displayed on the home screen)
     * @param playerScans QRPlayerScans object of player's scans
     * @param score textview to update value of (w/ user's score)
     * @param QRCodes QRCodesCollection object of possible QR codes
     * @param username username of player whose score is being updated
     */
    public static void setHomeScore(QRPlayerScans playerScans, TextView score,
                                    QRCodesCollection QRCodes, String username) {
        playerScans.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Set<String> codes = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        codes = doc.getData().keySet();
                    }
                }
                if (codes != null) {
                    Set<String> finalCodes = codes;
                    QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int total_score = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (finalCodes.contains(doc.getId())) {
                                    total_score += Integer.parseInt(doc.getString("Score"));
                                }
                            }
                            score.setText(String.valueOf(total_score));
                        }
                    });
                }
            }
        });
    }

    /**
     * Handles the setting of the player's total number of scans (displayed on the home screen)
     * @param scans PlayerScansCollection object of the player's scans
     * @param player_scans_textview textview to update value of (w/ total number of scans)
     * @param username username of player whose score is being updated
     */
    public static void setTotalScans(QRPlayerScans scans, TextView player_scans_textview,  String username) {
        scans.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(username)) {
                            player_scans_textview.setText(String.format("%d", document.getData().keySet().size()));
                            return;
                        }
                    }
                }
            }
        });
    }

    /**
     * Handles the setting of the player's highest score (displayed in profile details)
     * @param playerScans QRPlayerScans object of player's scans
     * @param high_score textview to update value of (w/ user's highest score)
     * @param QRCodes QRCodesCollection object of possible QR codes
     * @param username username of player whose score is being updated
     */
    public static void setHighScore(QRPlayerScans playerScans, TextView high_score,
                                    QRCodesCollection QRCodes, String username) {
        playerScans.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Set<String> codes = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        codes = doc.getData().keySet();
                    }
                }
                if (codes != null) {
                    Set<String> finalCodes = codes;
                    QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int max_score = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (finalCodes.contains(doc.getId())) {
                                    if(Integer.parseInt(doc.getString("Score"))>max_score){
                                        max_score = Integer.parseInt(doc.getString("Score"));
                                    }
                                }
                            }
                            high_score.setText(String.valueOf(max_score));
                        }
                    });
                }
            }
        });
    }

    /**
     * Handles the setting of the player's lowest score (displayed in profile details)
     * @param playerScans QRPlayerScans object of player's scans
     * @param low_score textview to update value of (w/ user's lowest score)
     * @param QRCodes QRCodesCollection object of possible QR codes
     * @param username username of player whose score is being updated
     */
    public static void setLowScore(QRPlayerScans playerScans, TextView low_score,
                                    QRCodesCollection QRCodes, String username) {
        playerScans.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Set<String> codes = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        codes = doc.getData().keySet();
                    }
                }
                if (codes != null) {
                    Set<String> finalCodes = codes;
                    QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int min_score = 1;
                            int count = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (finalCodes.contains(doc.getId())) {
                                    if(count == 0){
                                        min_score = Integer.parseInt(doc.getString("Score"));
                                    }
                                    if(Integer.parseInt(doc.getString("Score"))<min_score){
                                        min_score = Integer.parseInt(doc.getString("Score"));
                                    }
                                    count++;
                                }
                            }
                            low_score.setText(String.valueOf(min_score));
                        }
                    });
                }
            }
        });
    }
    public void setRank(){
        playerHighScoreRank = findViewById(R.id.player_info_rank_by_unique_score);
        playerTotalScoreRank = findViewById(R.id.player_info_rank_by_total_score);
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
                        if(document.getId().equals(username)){
                            playerHighScoreRank.setText(document.get("High Score Rank").toString());
                            playerTotalScoreRank.setText(document.get("Total Score Rank").toString());
                        }

                    }
                }
            }});}
}