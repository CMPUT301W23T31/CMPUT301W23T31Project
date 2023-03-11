package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

// TODO: Determine if this class is even needed
public class PlayerScansDataBase {
    FirebaseFirestore playerDB;
    CollectionReference collection;
    QueryDocumentSnapshot returnDocument = null;
    QuerySnapshot[] collectionDocuments = new QuerySnapshot[1];

    public PlayerScansDataBase(String collection) {
        playerDB = FirebaseFirestore.getInstance();
        this.collection = playerDB.collection(collection);
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

