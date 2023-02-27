package com.example.cmput301w23t31project;


// References
// 1. https://www.javatpoint.com/java-get-current-date
// 2. https://firebase.google.com/docs/firestore/manage-data/add-data


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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * This class creates a Dialog Fragment to show the name and score of a QR Code the user has
 * just scanned. It also processes QR Code data to be viewed in detail if the user wishes to do so
 */
public class ScanResultsFragment extends DialogFragment {
    private final String username;
    private OnFragmentInteractionListener listener;
    private final String hash;
    TextView resultView;
    TextView scoreView;
    public String[] QRNameAdjectives = new String[1010];
    public String[] QRNameColors = new String[128];
    public String[] QRNameNouns = new String[2876];

    public ScanResultsFragment(String hash, String username){
        this.hash = hash;
        this.username = username;
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
     * @param context
     *      Context information for an OnFragmentInteractionListener class
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
     * @param savedInstanceState
     *      Bundle object needed to create a Dialog fragment
     * @return
     *      A Dialog fragment displaying a QR code name and score
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //creates the Dialog and handles responses to interactions between user and layout
        View view = LayoutInflater.from(getActivity()).
                inflate(R.layout.fragment_scan_results, null);
        resultView = view.findViewById(R.id.scan_results_data);
        scoreView = view.findViewById(R.id.scan_results_score);

        // Get access to the database
        FirebaseFirestore QRdb = FirebaseFirestore.getInstance();
        CollectionReference codes = QRdb.collection("QRCodes");
        CollectionReference playerScans = QRdb.collection("PlayerScans");

        // Get access to adjectives, colors, and nouns to name scanned QR codes
        QRNameAdjectives = Utilities.retrieveFileData(this.getResources(),
                1010, R.raw.adjectives);
        QRNameColors = Utilities.retrieveFileData(this.getResources(), 128, R.raw.colors);
        QRNameNouns = Utilities.retrieveFileData(this.getResources(), 2876, R.raw.nouns);

        codes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // If query is successful (even if no results found), we process QR code data
                    processQRCodeInDatabase(task, codes);
                }
            }
        });

        playerScans.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // If query is successful, let database know particular player scanned this
                    // particular QR code
                    processPlayerScanInDatabase(task, playerScans);
                }
            }
        });

        String name = Utilities.getQRCodeName(hash, QRNameAdjectives, QRNameColors, QRNameNouns);
        int score = Utilities.getQRScore(hash);

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
                        startActivity(intent);
                    }
                }).create();
    }

    /**
     * This method processes QR Codes in the database, either adding a QR code or updating it
     * @param task
     *      The query completed that accessed QR code documents
     * @param codes
     *      The collection of QR code documents
     */
    private void processQRCodeInDatabase(Task<QuerySnapshot> task, CollectionReference codes) {
        // Go through each document and find if the scanned QR code exists in the database
        for (QueryDocumentSnapshot doc : task.getResult()) {
            if (doc.getId().equals(hash)) {
                // Update times scanned
                String timesScanned = String.valueOf(Integer.
                        parseInt(Objects.requireNonNull(doc.getString("TimesScanned")))+1);
                codes.document(hash).update("TimesScanned", timesScanned);
                DateTimeFormatter dtf;
                // Update latest date scanned
                if (android.os.Build.VERSION.SDK_INT >=
                        android.os.Build.VERSION_CODES.O) {
                    dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDateTime now = LocalDateTime.now();
                    codes.document(hash).update("LastScanned", dtf.format(now));
                }
                return;
            }
        }
        // Otherwise add scanned QR code to database
        addQRCodeToDatabase(codes);
    }

    /**
     * This method adds a QR code to the database
     * @param codes
     *      The collection of QR code documents
     */
    private void addQRCodeToDatabase(CollectionReference codes) {
        // Get QR code name and score
        String name = Utilities.getQRCodeName(hash, QRNameAdjectives, QRNameColors, QRNameNouns);
        int score = Utilities.getQRScore(hash);

        // Add necessary fields of QR code data
        HashMap<String, String> stringData = new HashMap<>();
        stringData.put("Name", name);
        stringData.put("Score", String.valueOf(score));
        stringData.put("Latitude", "0");
        stringData.put("Longitude", "0");
        stringData.put("Likes", "0");
        stringData.put("Dislikes", "0");
        stringData.put("TimesScanned", "1");
        DateTimeFormatter dtf;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            stringData.put("LastScanned", dtf.format(now));
        }
        // Add the data to the database
        codes.document(hash).set(stringData);
    }

    /**
     * This method either adds a record in the database indicating the user has scanned the
     * particular QR code, or updates that record
     * @param task
     *      The query that accessed the player scans information
     * @param playerScans
     *      The collection of player scans documents
     */
    private void processPlayerScanInDatabase(Task<QuerySnapshot> task,
                                         CollectionReference playerScans) {
        for (QueryDocumentSnapshot doc : task.getResult()) {
            if (doc.getId().equals(username)) {
                // If database knows user has previously scanned this code,
                // it increments the # of times user has scanned that code
                if (doc.getData().containsKey(hash)) {
                    String timesScanned = String.valueOf(Integer.parseInt(Objects.
                            requireNonNull(doc.getString(hash))) + 1);
                    playerScans.document(username).update(hash, timesScanned);
                } else {
                    // Otherwise, adds record to the database that player scanned this QR code
                    // only if player has scanned at least 1 other QR code
                    Map<String, Object> m = doc.getData();
                    m.put(hash, "1");
                    playerScans.document(username).set(m);
                }
                return;
            }
        }
        // Adds record indicating this code has been scanned if this is the first QR code
        // the player has scanned
        Map<String, Object> m = new HashMap<>();
        m.put(hash, "1");
        playerScans.document(username).set(m);
    }
}
