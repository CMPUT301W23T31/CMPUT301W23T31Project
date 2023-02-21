package com.example.cmput301w23t31project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QRCodeStatsActivity extends AppCompatActivity {
    public static final String NAME = "name";
    public static final String SCORE = "score";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        String nameText = intent.getStringExtra(NAME);
        String scoreText = intent.getStringExtra(SCORE);
        TextView nameView = findViewById(R.id.QR_code_score_display);
        TextView scoreView = findViewById(R.id.QR_code_name_display);
        nameView.setText(scoreText);
        scoreView.setText(nameText);




    }





}
