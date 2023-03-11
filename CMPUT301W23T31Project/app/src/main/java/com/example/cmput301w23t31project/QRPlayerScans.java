package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class performs operations related to the collection of player scans
 */
public class QRPlayerScans extends QRDatabase {

    public QRPlayerScans() {
        super("PlayerScans");
    }

    /**
     * This method either adds a record in the database indicating the user has scanned the
     * particular QR code, or updates that record
     * @param username the username of the user using the app on the device
     * @param hash the hash value of the scanned QR code
     */
    public void processPlayerScanInDatabase(String username, String hash) {
        CollectionReference scans = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QueryDocumentSnapshot document = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        // If database knows user has previously scanned this code,
                        // it increments the # of times user has scanned that code
                        document = doc;
                        if (doc.getData().containsKey(hash)) {
                            String timesScanned = String.valueOf(Integer.parseInt(Objects.
                                    requireNonNull(doc.getString(hash))) + 1);
                            scans.document(username).update(hash, timesScanned);
                            return;
                        }
                    }
                }
                // Otherwise, create a new document with a QR code the user scanned
                Map<String, Object> m;
                if (document != null) {
                    m = document.getData();
                } else {
                    m = new HashMap<>();
                }
                m.put(hash, "1");
                scans.document(username).set(m);
            }
        });
    }

}
