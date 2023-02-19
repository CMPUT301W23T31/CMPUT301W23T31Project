package com.example.cmput301w23t31project;


import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

// implements onClickListener for the onclick behaviour of button

public class MainActivity extends AppCompatActivity implements ScanResultsFragment.OnFragmentInteractionListener {
    Button scanBtn;
    TextView messageText, messageFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_screen);
        Intent intent = getIntent();

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.home_screen_scan_code_button);
        //messageText = findViewById(R.id.textContent);
        //messageFormat = findViewById(R.id.textFormat);

        // adding listener to the button

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need to create the object
                // of IntentIntegrator class
                // which is the class of QR library
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
            }
        });

    }
    public void onClickAppInfo(View view){
        Intent intent = new Intent(this, AppInfoScreenActivity.class);
        startActivity(intent);
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
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            /*
            case R.id.item3: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item4: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item5: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item6: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item7: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            */
            case R.id.item8: {
                Intent intent = new Intent(this, AppInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String hash = "";
                try {
                    hash = Utilities.hashQRCode(intentResult.getContents());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                int score = Utilities.getQRScore(hash);
                String n = ""+hash;
                new ScanResultsFragment(n, score).show(getSupportFragmentManager(), "SCAN RESULTS");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            String n = "";
            new ScanResultsFragment(n, 0).show(getSupportFragmentManager(), "SCAN RESULTS");
        }
    }

    @Override
    public void onOkPressed(){
    }
}
