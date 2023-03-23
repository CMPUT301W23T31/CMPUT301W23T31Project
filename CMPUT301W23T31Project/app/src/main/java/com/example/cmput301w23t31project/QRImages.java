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

public class QRImages extends QRDatabase {

    public QRImages() {
        super("QRImages");
    }

    public void processQRImageInDatabase(String username, String hash, String new_uri) {
        CollectionReference images = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                QueryDocumentSnapshot document = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        // If database knows user has previously scanned this code,
                        // it increments the # of times user has scanned that code
                        document = doc;
                    }
                }
                // Otherwise, create a new document with a QR code the user scanned
                Map<String, Object> m;
                if (document != null) {
                    m = document.getData();
                } else {
                    m = new HashMap<>();
                }
                m.put(hash, new_uri);
                images.document(username).set(m);
            }
        });
    }
}
