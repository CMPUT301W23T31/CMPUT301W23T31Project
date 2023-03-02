package com.example.cmput301w23t31project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class QRDatabase {
    FirebaseFirestore QRdb;
    CollectionReference collection;
    QueryDocumentSnapshot returnDocument = null;
    QuerySnapshot[] collectionDocuments = new QuerySnapshot[1];

    public QRDatabase(String collection) {
        QRdb = FirebaseFirestore.getInstance();
        this.collection = QRdb.collection(collection);
    }

    public QueryDocumentSnapshot getDocument(String docId) {
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(docId)) {
                            returnDocument = document;
                            return;
                        }
                    }
                }
            }
        });
        return returnDocument;
    }

    public CollectionReference getReference() {
        return collection;
    }


}
