package com.example.cmput301w23t31project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Creates Player Profile Activity
 */
public class PlayerProfileActivity extends HamburgerMenu {
    String username;
    /**
     * Instantiates layout of screen
     * @param savedInstanceState previously saves instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        Player player =  (Player) getIntent().getSerializableExtra("Player_Data");

        TextView PlayerUsername, PlayerScore, PlayerHighestScoringQr, PlayerLowestScoringQr, view_scans;

        PlayerUsername = findViewById(R.id.player_profile_username);
        PlayerScore = findViewById(R.id.player_profile_total_score);
        PlayerHighestScoringQr = findViewById(R.id.player_profile_high_score);
        PlayerLowestScoringQr = findViewById(R.id.player_profile_low_score);
        view_scans = findViewById(R.id.player_profile_see_scans_button);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        PlayerUsername.setText(player.getUsername());
        PlayerScore.setText(String.valueOf(player.getTotalScore()));
        PlayerHighestScoringQr.setText(String.valueOf(player.getHighestScoringQR()));
        PlayerLowestScoringQr.setText(String.valueOf(player.getLowestScoringQR()));

        view_scans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PlayerScansCollection scans = new PlayerScansCollection();
                //scans.processPlayerScansInDatabase(username);
                Intent intent = new Intent(PlayerProfileActivity.this, MyScansScreenActivity.class);
                intent.putExtra("username", player.getUsername());
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
}