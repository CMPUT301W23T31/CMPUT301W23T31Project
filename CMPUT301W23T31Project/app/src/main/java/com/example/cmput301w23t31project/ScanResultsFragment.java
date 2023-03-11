package com.example.cmput301w23t31project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

    public String[] QRNameAdjectives = new String[1010];
    public String[] QRNameColors = new String[128];
    public String[] QRNameNouns = new String[2876];
    private boolean impliesScoreChange = false;

    /**
     * Creates a new 'Scan Results' Fragment from Scan
     * @param hash scanned QR code hash
     * @param username username of player who scanned
     * @param score score of QR code scanned
     * @param latitude longitude of QR code
     * @param longitude latitude of QR code
     */
    public ScanResultsFragment(String hash, String username, TextView score, double latitude, double longitude){
        this.hash = hash;
        this.username = username;
        this.homeScore = score;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Creates a new 'Scan Results' Fragment from Scan (w/ no location given)
     * @param hash scanned QR code hash
     * @param username username of player who scanned
     * @param score score of QR code scanned
     */
    public ScanResultsFragment(String hash, String username, TextView score){
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
        QRNameAdjectives = Utilities.retrieveFileData(this.getResources(), 1010, R.raw.adjectives);
        QRNameColors = Utilities.retrieveFileData(this.getResources(), 128, R.raw.colors);
        QRNameNouns = Utilities.retrieveFileData(this.getResources(), 2876, R.raw.nouns);

        // Calculates score and name from hash
        String name = Utilities.getQRCodeName(hash, QRNameAdjectives, QRNameColors, QRNameNouns);
        int score = Utilities.getQRScore(hash);

        // Get access to the database
        QRCodesCollection codes = new QRCodesCollection();
        QRPlayerScans playerScans = new QRPlayerScans();

        if(Objects.equals(location, "No Location")){
            codes.processQRCodeInDatabase(name, String.valueOf(score), hash);
            locationView.setText("No Location");
        } else {
            codes.processQRCodeInDatabase(name, String.valueOf(score), hash, latitude, longitude);
            locationView.setText("L" + latitude + " " + longitude);
        }
        playerScans.processPlayerScanInDatabase(username, hash);


        // Set the fragment screens to display the name and score of the scanned QR code
        resultView.setText(name);
        String s = "QR Code Score: " + score;
        scoreView.setText(s);


        // Build dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
            .setView(view)
            .setTitle("SCAN RESULTS")
            .setNegativeButton("BACK TO SCANNER", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Head back to main menu and close the dialog fragment
                    dialogInterface.cancel();
                    MainActivity.setHomeScore(new QRPlayerScans(), homeScore, new QRCodesCollection(), username);

                }
            })
            .setPositiveButton("SEE CODE DETAILS", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onOkPressed();
                    dialogInterface.cancel();
                    // Send Hash and Device ID to a new fragment that shows QR Code statistics
                    Intent intent = new Intent(getContext(), QRCodeStatsActivity.class);
                    intent.putExtra("Hash", hash);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }).create();
    }
}
