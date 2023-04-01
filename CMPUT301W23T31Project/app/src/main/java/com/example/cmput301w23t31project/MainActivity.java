package com.example.cmput301w23t31project;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.security.NoSuchAlgorithmException;
import java.util.Set;


// implements onClickListener for the onclick behaviour of button
// https://www.youtube.com/watch?v=UIIpCt2S5Ls

// For style stuff
// https://stackoverflow.com/questions/32671004/how-to-change-the-color-of-a-button
// https://stackoverflow.com/questions/10266595/how-to-make-a-round-button


/**
 * Main Activity Class for home screen of app (main menu)
 */
public class MainActivity extends HamburgerMenu implements ScanResultsFragment.OnFragmentInteractionListener {
    String username;
    TextView score;
    private GpsTracker gpsTracker;
    double latitude;
    double longitude;
    boolean recordLocation= true;
    String DeviceID = MyDeviceID.getInstance();
    /**
     * On create method
     * Handles the setup of home screen button and other functionalities
     * @param savedInstanceState saved instance state from past
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("HOME");
        setContentView(R.layout.fragment_home_screen);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        AccountsCollection collectionReferenceAccount = new AccountsCollection();
        QRCodesCollection QRCodes = new QRCodesCollection();
        QRPlayerScans playerScans = new QRPlayerScans();
        PlayerInfoCollection playerScansCollection = new PlayerInfoCollection();
        playerScansCollection.getPlayerScans();
        playerScansCollection.CreateLeaderBoard();

        findNearbyCodes(QRCodes);

        //get login details

        score = findViewById(R.id.home_screen_current_points);
        if (intent.hasExtra("path")) {
            collectionReferenceAccount.addAccountToCollection(username, intent, DeviceID);
        } else {
            username = intent.getStringExtra("username");
        }

        setHomeScore(playerScans, score, QRCodes, username);

        // Reference and initialize the Button and TextViews
        TextView home_screen_username = findViewById(R.id.home_screen_welcome_text);
        ImageView scanBtn = findViewById(R.id.home_screen_scan_code_button);
        ImageView playerInfoBtn = findViewById(R.id.home_screen_player_info_button);
        ImageView exploreBtn = findViewById(R.id.home_screen_explore_button);
        ImageView myScanBtn = findViewById(R.id.home_screen_my_scans_button);

        String home_username = "Welcome " + username + "!";
        home_screen_username.setText(home_username);

        // fine location access request
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // new scan button functionality
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                if (username.equals("NewTestName")) {
                    integrator.setTimeout(50);
                }
                integrator.initiateScan();
            }
        });

        // player info button functionality
        playerInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        PlayerInfoScreenActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // explore (map) button functionality
        exploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getting current location
                gpsTracker = new GpsTracker(MainActivity.this);
                if(gpsTracker.canGetLocation()){
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                }else{
                    gpsTracker.showSettingsAlert();
                }
                Intent intent = new Intent(MainActivity.this,
                        ExploreScreenActivity.class);
                intent.putExtra("latitude",String.valueOf(latitude));
                intent.putExtra("longitude",String.valueOf(longitude));
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        // "my scans" button functionality
        myScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        MyScansScreenActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("crnt_username", username);
                startActivity(intent);
            }
        });



    }

    /**
     * Handles functionality for App Info button
     * @param view relevant view
     */
    public void onClickAppInfo(View view){
        Intent intent = new Intent(this, AppInfoScreenActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * Hamburger menu clicking functionality
     * @param menu menu to open
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    /**
     * Functionality for Leaderboard Button click
     * @param view relevant view
     */
    public void onClickLeaderboard(View view){
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("state", "HIGHSCORE");
        startActivity(intent);
    }

    /**
     * Functionality for option menu interaction
     * @param item item being selected within menu
     * @return boolean based on whether item is valid or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);
    }

    /**
     * Used to initiate player when logged in
     * @param requestCode request code
     * @param resultCode result code
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                if (username.equals("NewTestName")) {
                    new ScanResultsFragment("b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489"
                            , username, score, 0, 0).
                            show(getSupportFragmentManager(), "SCAN RESULTS");
                } else {
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                }
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                //new AllowLocationFragment().show(getSupportFragmentManager(), "Ask location permission");
                String hash = "";
                try {
                    hash = Utilities.hashQRCode(intentResult.getContents());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                gpsTracker = new GpsTracker(MainActivity.this);
                if(gpsTracker.canGetLocation()){
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                    new ScanResultsFragment(hash, username, score, latitude, longitude).
                            show(getSupportFragmentManager(), "SCAN RESULTS");
                    Toast.makeText(this, "l"+latitude+longitude, Toast.LENGTH_SHORT)
                            .show();
                }else{
                    gpsTracker.showSettingsAlert();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            String n = "";

            new ScanResultsFragment(n, "", score,0,0).show(getSupportFragmentManager(), "SCAN RESULTS");

        }


    }

    /**
     * When OK is pressed
     */
    @Override
    public void onOkPressed(){
    }

    /**
     * Handles the live updates to the home screen score and other details
     * @param playerScans relevant QRPlayerScans object
     * @param score textview to put player's score in
     * @param QRCodes relevant QRCodesCollection
     * @param username player's username
     */
    public static void setHomeScore(QRPlayerScans playerScans, TextView score,
                                    QRCodesCollection QRCodes, String username) {
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
                    QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    public void findNearbyCodes(QRCodesCollection QRCodes){
        double crntLatitude = 0;
        double crntLongitude = 0;
        gpsTracker = new GpsTracker(MainActivity.this);
        if(gpsTracker.canGetLocation()){
            crntLatitude = gpsTracker.getLatitude();
            crntLongitude = gpsTracker.getLongitude();
        }else{
            gpsTracker.showSettingsAlert();
        }

        double minLatitude = crntLatitude - (20/111.12);
        double maxLatitude = crntLatitude + (20/111.12);
        double minLongitude = crntLongitude - (20/111.12)*Math.cos(crntLatitude);
        double maxLongitude = crntLongitude + (20/111.12)*Math.cos(crntLatitude);

        QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    double foundLat = Double.parseDouble(doc.getString("Latitude"));
                    double foundLng = Double.parseDouble(doc.getString("Longitude"));
                    if ((minLatitude<= foundLat && foundLat <= maxLatitude)&&(minLongitude<= foundLng && foundLng <= maxLongitude)) {
                        Log.d("Codes nearby:",doc.getString("Name"));
                    }
                }

            }
        });

    }



}
