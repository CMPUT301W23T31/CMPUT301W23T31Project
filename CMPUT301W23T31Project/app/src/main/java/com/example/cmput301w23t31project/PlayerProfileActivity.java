package com.example.cmput301w23t31project;


import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;


/**
 * Creates Player Profile Activity
 */
public class PlayerProfileActivity extends HamburgerMenu {
    private FirebaseFirestore db;
    Player player;
    String username;
    String crnt_username;
    TextView playerTotalScoreRank;
    TextView playerHighScoreRank;
    TextView PlayerHighestScoringQr;


    /**
     * Instantiates layout of screen
     * @param savedInstanceState previously saves instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("PLAYER INFO");
        setContentView(R.layout.activity_player_profile);
        TextView PlayerUsername, PlayerScore, PlayerLowestScoringQr, view_scans,PlayerScanCount;

        PlayerUsername = findViewById(R.id.player_profile_username);
        PlayerScore = findViewById(R.id.player_profile_total_score);
        PlayerHighestScoringQr = findViewById(R.id.player_profile_high_score);
        PlayerLowestScoringQr = findViewById(R.id.player_profile_low_score);
        PlayerScanCount = findViewById(R.id.player_profile_total_scans);
        view_scans = findViewById(R.id.player_profile_see_scans_button);
        ImageView image = findViewById(R.id.player_image);


        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        crnt_username = intent.getStringExtra("currentUser");
        Log.i("myusername",crnt_username);
        Log.i("rando",username);

        Glide.with(this)
                .load("https://api.dicebear.com/6.x/pixel-art/png?seed="+username)
                .into(image);
        /////
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
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if(document.getId().equals(username)){
                                    int totalScore = Integer.parseInt(document.getString("Total Score"));
                                    int totalScans = Integer.parseInt(document.getString("Total Scans"));
                                    int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                                    int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                                    int rank = Integer.parseInt(document.getString("Rank"));
                                    PlayerUsername.setText(username+"'s Stats!");
                                    PlayerScore.setText(String.valueOf(totalScore));
                                    PlayerHighestScoringQr.setText(String.valueOf(highestScoringQR));
                                    PlayerLowestScoringQr.setText(String.valueOf(lowestScoringQR));
                                    PlayerScanCount.setText((String.valueOf(totalScans)));
                                }
                            }
                        }
                    }
                });

        ////

        setRank();
        view_scans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerProfileActivity.this,
                        MyScansScreenActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("currentUser", crnt_username);

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
        return useHamburgerMenu(item, crnt_username);

    }
    public void setRank(){
        playerHighScoreRank = findViewById(R.id.player_info_rank_by_unique_score);
        playerTotalScoreRank = findViewById(R.id.player_info_rank_by_total_score);
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get() .addOnSuccessListener(
                queryDocumentSnapshots -> {
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
});}
}