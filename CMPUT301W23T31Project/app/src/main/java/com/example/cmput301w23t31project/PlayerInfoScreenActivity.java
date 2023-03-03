package com.example.cmput301w23t31project;

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

public class PlayerInfoScreenActivity extends AppCompatActivity {

    Button viewScanBtn;
    ImageButton myAccountBtn;
    TextView player_info_username;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info_screen);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        viewScanBtn = findViewById(R.id.player_info_see_scans_button);
        myAccountBtn = findViewById(R.id.player_info_my_account_button);
        player_info_username = findViewById(R.id.player_info_username);

        player_info_username.setText(username);
        viewScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerInfoScreenActivity.this, MyScansScreenActivity.class);
                startActivity(intent);
            }
        });

        myAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerInfoScreenActivity.this, MyAccountScreenActivity.class);
                startActivity(intent);
            }
        });
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