package com.example.cmput301w23t31project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class PlayerProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        Player player =  (Player) getIntent().getSerializableExtra("Player_Data");

        TextView PlayerName, PlayerUsername, PlayerScore;

        PlayerName = findViewById(R.id.player_name);
        PlayerUsername = findViewById(R.id.player_username);
        PlayerScore = findViewById(R.id.player_score);

        PlayerName.setText(player.getPlayerName());
        PlayerUsername.setText(player.getUserName());
        PlayerScore.setText(String.valueOf(player.getTotalScore()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item2: {
                finish();
                return true;
            }
            /*
            case R.id.item3: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.item5: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }

            */
            case R.id.item4: {
                Intent intent = new Intent(this, ExploreScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item6: {
                Intent intent = new Intent(this, PlayerInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item7: {
                Intent intent = new Intent(this, MyAccountScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item8: {
                Intent intent = new Intent(this, AppInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}