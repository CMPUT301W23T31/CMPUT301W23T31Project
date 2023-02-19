package com.example.cmput301w23t31project;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

<<<<<<< Updated upstream
=======
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
>>>>>>> Stashed changes
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

// implements onClickListener for the onclick behaviour of button
// https://www.youtube.com/watch?v=UIIpCt2S5Ls
public class MainActivity extends AppCompatActivity implements ScanResultsFragment.OnFragmentInteractionListener {
    Button scanBtn;
    TextView messageText, messageFormat;
<<<<<<< Updated upstream

=======
    FirebaseFirestore QRdb;
    CollectionReference collectionReference;
    String[] QRNames = new String[100];
>>>>>>> Stashed changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_screen);
        Intent intent = getIntent();
<<<<<<< Updated upstream

=======
        QRdb = FirebaseFirestore.getInstance();
        collectionReference = QRdb.collection("QRCodes");
>>>>>>> Stashed changes
        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.home_screen_scan_code_button);
        //messageText = findViewById(R.id.textContent);
        //messageFormat = findViewById(R.id.textFormat);

        // adding listener to the button
<<<<<<< Updated upstream
=======

        //referencing and initializing the Player Info Button
        playerInfoBtn = findViewById(R.id.home_screen_player_info_button);

        //referencing and initializing the Explore Button
        exploreBtn = findViewById(R.id.home_screen_explore_button);
        String data = "";
        InputStream is = this.getResources().openRawResource(R.raw.qrcode);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int count = 0;
        if (is != null) {
            try {
                while ((data = reader.readLine()) != null) {
                    QRNames[count] = data;
                    count++;
                }
                is.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
                new ScanResultsFragment(n, score).show(getSupportFragmentManager(), "SCAN RESULTS");
=======
                String name = null;
                try {
                    name = getQRCodeName(hash);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<String, QRCode> QRData = new HashMap<>();
                QRData.put("Code Info", new QRCode(name, score));
                collectionReference.document(name).set(QRData);
                new ScanResultsFragment(name, score).show(getSupportFragmentManager(), "SCAN RESULTS");
>>>>>>> Stashed changes
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

    public String getQRCodeName(String hash) throws IOException {
        String name = "";
        for (int i = 0; i < hash.length(); i += 8) {
            int tempScore = 0;
            for (int j = 0; j < i + 8; j++) {
                tempScore += hash.charAt(j);
            }
            tempScore %= QRNames.length;
            name = name + QRNames[tempScore];
        }
        return name;
    }

}
