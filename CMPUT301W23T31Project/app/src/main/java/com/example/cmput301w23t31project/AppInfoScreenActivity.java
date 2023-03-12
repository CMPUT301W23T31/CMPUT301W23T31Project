package com.example.cmput301w23t31project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;


/**
 * This class is for the "App Info" Screen and Menu
 * Screen is fairly static, so functions are limited
 */
public class AppInfoScreenActivity extends HamburgerMenu {
    private String username;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_app_info_screen);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);

    }
}