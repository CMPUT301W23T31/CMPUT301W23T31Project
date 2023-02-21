package com.example.cmput301w23t31project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cmput301w23t31project.ui.login.LoginActivity;

public class TitleScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title_screen);
    }
    public void onTap(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
