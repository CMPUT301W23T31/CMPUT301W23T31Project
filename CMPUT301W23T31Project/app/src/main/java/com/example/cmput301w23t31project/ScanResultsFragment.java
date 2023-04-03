package com.example.cmput301w23t31project;


import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * This class creates a Dialog Fragment to show the name and score of a QR Code the user has
 * just scanned. It also processes QR Code data to be viewed in detail if the user wishes to do so
 * References:
 *   1. https://www.javatpoint.com/java-get-current-date
 *   2. https://firebase.google.com/docs/firestore/manage-data/add-data
 */
public class ScanResultsFragment extends DialogFragment {
    private final String username;
    public double latitude;
    public double longitude;
    private OnFragmentInteractionListener listener;
    private final String hash;

    TextView resultView;
    TextView scoreView;
    TextView locationView;
    TextView homeScore;
    String location;
    Boolean recordlocation;
    TextView set_on_off;

    String crnt_date;
    String timesScanned;
    String likes;
    String dislikes;
    String lat;
    String lng;

    public String[] QRName = new String[351];
    public String[] QRName2 = new String[349];
    public String[] QRName3 = new String[350];
    public String[] QRName4 = new String[235];
    public String[] QRName5 = new String[350];
    private boolean impliesScoreChange = false;

    public ToggleButton toggleButton;
    public Button cameraButton;
    public TextView stateOnOff;

    /**
     * Creates a new 'Scan Results' Fragment from Scan
     *
     * @param hash      scanned QR code hash
     * @param username  username of player who scanned
     * @param score     score of QR code scanned
     * @param latitude  longitude of QR code
     * @param longitude latitude of QR code
     */
    public ScanResultsFragment(String hash, String username, TextView score, double latitude,
                               double longitude) {
        this.hash = hash;
        this.username = username;
        this.homeScore = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates a new 'Scan Results' Fragment from Scan (w/ no location given)
     *
     * @param hash     scanned QR code hash
     * @param username username of player who scanned
     * @param score    score of QR code scanned
     */
    public ScanResultsFragment(String hash, String username, TextView score) {
        this.hash = hash;
        this.username = username;
        this.homeScore = score;
        this.location = "No Location";

    }

    /**
     * This interface has the onOkPressed() method, which checks if any clicks to jump to
     * other fragments occur in the ScanResults Fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    /**
     * This method checks if user has implemented the required callback listener to the fragment
     *
     * @param context context information for an OnFragmentInteractionListener class
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context +
                    " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method displays a pop up dialog fragment and shows the user a QR code name and score
     *
     * @param savedInstanceState bundle object needed to create a Dialog fragment
     * @return d Dialog fragment displaying a QR code name and score
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //creates the Dialog and handles responses to interactions between user and layout
        View view = getLayoutInflater().inflate(R.layout.fragment_scan_results, null);
        resultView = view.findViewById(R.id.scan_results_data);
        scoreView = view.findViewById(R.id.scan_results_score);
        locationView = view.findViewById(R.id.scan_results_location);


        // Get access to adjectives, colors, and nouns to name scanned QR codes
        QRName = Utilities.retrieveFileData(this.getResources(), 351, R.raw.names);
        QRName2 = Utilities.retrieveFileData(this.getResources(), 349, R.raw.namestwo);
        QRName3 = Utilities.retrieveFileData(this.getResources(), 350, R.raw.namesthree);
        QRName4 = Utilities.retrieveFileData(this.getResources(), 235, R.raw.namesfour);
        QRName5 = Utilities.retrieveFileData(this.getResources(), 350, R.raw.namesfive);

        // Calculates score and name from hash
        String name = Utilities.getQRCodeName(hash, QRName, QRName2, QRName3, QRName4, QRName5);
        int score = Utilities.getQRScore(hash);

        // Get access to the database
        QRCodesCollection codes = new QRCodesCollection();
        QRPlayerScans playerScans = new QRPlayerScans();
        playerScans.processPlayerScanInDatabase(username, hash);

        // Set the fragment screens to display the name and score of the scanned QR code
        resultView.setText(name);
        String s = "QR Code Score: " + score;
        scoreView.setText(s);

        toggleButton = (ToggleButton) view.findViewById(R.id.location_button);
        cameraButton = view.findViewById(R.id.scan_results_camera_button);
        set_on_off = view.findViewById(R.id.set_on_off);
        recordlocation = false;
        Log.v(TAG, "no location");
        codes.setLocation( 300, 300);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    codes.setLocation(latitude, longitude);
                    Log.d("TAG", "ALLOWED LOCATION RECORD"+latitude+longitude);
                } else {
                    Log.v(TAG, "TOGGLE CHECK FALSE");
                    codes.setLocation( 200, 200);
                }
            }
        });
        Log.d("Called:","now");

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CameraActivity.class);
                intent.putExtra("ImageDivider", hash);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });


        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("SCAN RESULTS")
                .setPositiveButton("SEE CODE DETAILS", null) //Set to null. We override the onclick
                .setNegativeButton("BACK TO HOME", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(codes.getLocation()==300){
                        Toast.makeText(getContext(),"Please select location permission",
                                Toast.LENGTH_SHORT).show();
                        if (username.equals("NewTestName")) {
                            codes.processQRCodeInDatabase(name, String.valueOf(score), hash);
                        }
                        }else {
                            listener.onOkPressed();
                            codes.processQRCodeInDatabase(name, String.valueOf(score), hash);
                            dialogInterface.cancel();
                            // Intent is used to switch from one activity to another.
                            processQRCode( name,  String.valueOf(score), hash);
                            Intent intent = new Intent(getContext(), QRCodeStatsActivity.class);
                            intent.putExtra("Hash", hash);
                            intent.putExtra("username", username);
                            intent.putExtra("currentUser", username);
                            intent.putExtra("score", String.valueOf(score));
                            intent.putExtra("name", name);
                            intent.putExtra("timesScanned", timesScanned);
                            intent.putExtra("crnt_date", crnt_date);
                            PlayerInfoCollection players = new PlayerInfoCollection();
                            players.getPlayerScans();
                            players.CreateLeaderBoard();
                            players.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot doc: task.getResult()) {
                                        Map<String, Object> m = doc.getData();
                                        m.put("Total Score", doc.getString("Total Score")+score);
                                        players.getReference().document(username).set(m);
                                    }
                                }
                            });
                            if(codes.getLocation()==200){
                                intent.putExtra("lat", "200");
                            }else{
                                intent.putExtra("lat", "0");
                            }
                            intent.putExtra("likes", likes);
                            intent.putExtra("dislikes", dislikes);
                            startActivity(intent);

                        }
                    }
                });

                Button negative_button = ((AlertDialog) dialog).getButton(AlertDialog.
                        BUTTON_NEGATIVE);
                negative_button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        PlayerInfoCollection players = new PlayerInfoCollection();
                        players.getPlayerScans();
                        players.CreateLeaderBoard();
                        players.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (QueryDocumentSnapshot doc: task.getResult()) {
                                    Map<String, Object> m = doc.getData();
                                    m.put("Total Score", doc.getString("Total Score")+score);
                                    players.getReference().document(username).set(m);
                                }
                            }
                        });
                        // Head back to main menu and close the dialog fragment
                        if (codes.getLocation() == 300) {
                            Toast.makeText(getContext(), "Please select location permission",
                                    Toast.LENGTH_SHORT).show();
                            if (username.equals("NewTestName")) {
                                codes.processQRCodeInDatabase(name, String.valueOf(score), hash);
                            }
                        } else {
                            codes.processQRCodeInDatabase(name, String.valueOf(score), hash);
                            MainActivity.setHomeScore(new QRPlayerScans(), homeScore,
                                    new QRCodesCollection(), username);
                            dialogInterface.cancel();
                        }
                    }
                });
            }
        });

        dialog.show();
        return dialog;
    }

    /**
     * This method looks for a QR code matching hash and gets relevant data from it
     * @param name
     *      Name of a QR code
     * @param score
     *      Score of a QR code
     * @param hash
     *      Hash of a QR code
     */
    public void processQRCode(String name, String score, String hash) {
        QRCodesCollection codes = new QRCodesCollection();
        codes.getReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    if (document.getId().equals(hash)) {
                        likes = String.valueOf(Integer.
                                parseInt(Objects.requireNonNull(document.getString("Likes"))));
                        dislikes = String.valueOf(Integer.
                                parseInt(Objects.requireNonNull(document.
                                        getString("Dislikes"))));
                        lat = Objects.requireNonNull(document.getString("Latitude"));
                        lng = Objects.requireNonNull(document.getString("Longitude"));
                        timesScanned = String.valueOf(Integer.
                                parseInt(Objects.requireNonNull(document.
                                        getString("TimesScanned"))) + 1);
                        crnt_date = Utilities.getCurrentDate();
                        return;
                    }
                }
            }
        });
    }
}

