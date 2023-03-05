package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

public class PlayerScansCollection extends PlayerScansDataBase{
    String hash_return;

    public PlayerScansCollection() {
        super("PlayerScans");
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
    public void processPlayerScansInDatabase(String hash) {
        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(hash)) {

                            return;
                        }
                    }

                }
            }
        });
    }

}
