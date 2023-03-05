package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

public class QRCodesCollection extends QRDatabase {
    String hash_return;

    public QRCodesCollection() {
        super("QRCodes");
    }

    /**
     * This method updates # of times scanned and date last scanned for a provided QR code. If
     * QR code is not found, transfer control to add it to the database
     * @param name
     *      The name of the QR code
     * @param score
     *      The score of the scanned QR code
     * @param hash
     *      The hash of the QR code
     */
    public void processQRCodeInDatabase(String name, String score, String hash, double latitude, double longitude) {
        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(hash)) {
                            String timesScanned = String.valueOf(Integer.
                                    parseInt(Objects.requireNonNull(document.getString("TimesScanned"))) + 1);
                            codes.document(hash).update("TimesScanned", timesScanned);
                            codes.document(hash).update("LastScanned", Utilities.getCurrentDate());
                            return;
                        }
                    }
                    addQRCodeToDatabase(name, score, hash, latitude, longitude);
                }
            }
        });
    }

    public String getHashFromName(String name) {
        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getString("Name").equals(name)) {
                            hash_return =  document.getId();

                        }
                    }
                }
            }
        });
        return hash_return;
    }

    /**
     * This method adds a QR code to the database
     * @param name
     *      The name of the QR code
     * @param score
     *      The score of the scanned QR code
     * @param hash
     *      The hash of the scanned QR code
     */
    public void addQRCodeToDatabase(String name, String score, String hash, double latitude, double longitude) {
        // Get QR code name and score
        CollectionReference codes = getReference();

        // Add necessary fields of QR code data
        HashMap<String, String> stringData = new HashMap<>();
        stringData.put("Name", name);
        stringData.put("Score", score);
        stringData.put("Latitude", String.valueOf(latitude));
        stringData.put("Longitude", String.valueOf(longitude));
        stringData.put("Likes", "0");
        stringData.put("Dislikes", "0");
        stringData.put("TimesScanned", "1");
        stringData.put("LastScanned", Utilities.getCurrentDate());

        // Add the data to the database
        codes.document(hash).set(stringData);
    }
}
