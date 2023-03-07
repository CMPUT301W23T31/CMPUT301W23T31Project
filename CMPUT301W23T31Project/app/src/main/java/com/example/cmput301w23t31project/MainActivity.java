package com.example.cmput301w23t31project;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.security.NoSuchAlgorithmException;
import java.util.Set;


import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


// implements onClickListener for the onclick behaviour of button
// https://www.youtube.com/watch?v=UIIpCt2S5Ls
public class MainActivity extends AppCompatActivity implements ScanResultsFragment.OnFragmentInteractionListener {
    String username;
    TextView score;
    private GpsTracker gpsTracker;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home_screen);

        AccountsCollection collectionReferenceAccount = new AccountsCollection();
        QRCodesCollection QRcodes = new QRCodesCollection();
        QRPlayerScans playerScans = new QRPlayerScans();

        String ID = Utilities.getDeviceId(this);
        //get login details
        Intent intent = getIntent();
        score = findViewById(R.id.home_screen_current_points);
        username = intent.getStringExtra("username");
        if (!username.equals("")) {
            collectionReferenceAccount.addAccountToCollection(username, intent, ID);
        } else {
            username = intent.getStringExtra("username_present");
        }

        setHomeScore(playerScans, score, QRcodes, username);

        // Reference and initialize the Button and TextViews
        TextView home_screen_username = findViewById(R.id.home_screen_welcome_text);
        Button scanBtn = findViewById(R.id.home_screen_scan_code_button);
        Button playerInfoBtn = findViewById(R.id.home_screen_player_info_button);
        Button exploreBtn = findViewById(R.id.home_screen_explore_button);
        Button myScanBtn = findViewById(R.id.home_screen_my_scans_button);
        String home_username = "Welcome "+username+"!";
        home_screen_username.setText(home_username);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // we need to create the object
                // of IntentIntegrator class
                // which is the class of QR library
                /*
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.initiateScan();
                */

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
                  
            }
        });


        playerInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        PlayerInfoScreenActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ExploreScreenActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        myScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        MyScansScreenActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
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
    public void onClickLeaderboard(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
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


             */
            case R.id.item5: {
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                return true;
            }
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
                //getting location
                gpsTracker = new GpsTracker(MainActivity.this);
                if(gpsTracker.canGetLocation()){
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                    Toast.makeText(this, "l"+latitude+longitude, Toast.LENGTH_SHORT)
                            .show();
                }else{
                    gpsTracker.showSettingsAlert();
                }
                new ScanResultsFragment(hash, username, score, latitude, longitude).
                        show(getSupportFragmentManager(), "SCAN RESULTS");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            String n = "";

            new ScanResultsFragment(n, "", score,0,0).show(getSupportFragmentManager(), "SCAN RESULTS");

        }
    }

    @Override
    public void onOkPressed(){
    }

    public static void setHomeScore(QRPlayerScans playerScans, TextView score,
                                    QRCodesCollection QRcodes, String username) {
        playerScans.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Set<String> codes = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        codes = doc.getData().keySet();
                    }
                }
                if (codes != null) {
                    Set<String> finalCodes = codes;
                    QRcodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                int total_score = 0;
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    if (finalCodes.contains(doc.getId())) {
                                        total_score += Integer.parseInt(doc.getString("Score"));
                                    }
                                }
                                score.setText(String.valueOf(total_score));
                            }
                    });
                }
            }
        });
    }
}
