package com.example.cmput301w23t31project;


import android.util.Log;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.Objects;


/**
 * Organizes a collection of QR codes
 */
public class QRCodesCollection extends QRDatabase {
    double latitude;
    double longitude;

    public QRCodesCollection() {
        super("QRCodes");
    }

    public void setLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        Log.d("RECIEVED:", " "+ latitude+ longitude);
    }
    public double getLocation(){
        return latitude;
    }

    /**
     * This method updates # of times scanned and date last scanned for a provided QR code. If
     * QR code is not found, transfer control to add it to the database
     * @param name human-readable name of the QR code
     * @param score score of the scanned QR code
     * @param hash SHA-256 hash of the QR code
     */
    public void processQRCodeInDatabase(String name, String score, String hash) {
        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document: task.getResult()) {
                    if (document.getId().equals(hash)) {
                        String timesScanned = String.valueOf(Integer.
                                parseInt(Objects.requireNonNull(
                                        document.getString("TimesScanned"))) + 1);
                        if(!(latitude == 200)){
                            Log.d("Updates: ", ""+latitude);
                            codes.document(hash).update("Latitude", String.valueOf(latitude));
                            codes.document(hash).update("Longitude",String.valueOf(longitude));
                        } else {
                            codes.document(hash).update("Latitude", String.valueOf(200));
                            codes.document(hash).update("Longitude", String.valueOf(200));
                        }
                        codes.document(hash).update("TimesScanned", timesScanned);
                        codes.document(hash).update("LastScanned", Utilities.getCurrentDate());
                        return;
                    }
                }
                Log.d("RECORDED:", ""+latitude+longitude);
                addQRCodeToDatabase(name, score, hash, latitude, longitude);
            }
        });
    }




    /**
     * Adds a QR code to the database
     * @param name human-readable name of the QR code
     * @param score score of the scanned QR code
     * @param hash SHA-256 hash of the QR code
     * @param latitude latitude of QR code
     * @param longitude longitude of QR code
     */
    public void addQRCodeToDatabase(String name, String score, String hash, double latitude,
                                    double longitude) {
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
