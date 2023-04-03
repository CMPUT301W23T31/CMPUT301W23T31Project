package com.example.cmput301w23t31project;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import java.util.Objects;


/**
 * This class is for the "App Info" Screen and Menu
 * Screen is fairly static, so functions are limited
 */
public class AppInfoScreenActivity extends HamburgerMenu {
    private String username;
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).
                setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("App Info");
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