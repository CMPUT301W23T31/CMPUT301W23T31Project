package com.example.cmput301w23t31project;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerScansCollection extends PlayerScansDataBase{
    String hash_return;

    public PlayerScansCollection() {
        super("PlayerScans");
    }


    public void processPlayerScansInDatabase(String username) {
        CollectionReference codes = getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(username)) {
                                if (document != null) {
                                    Map data = document.getData();
                                    String str;
                                    data.entrySet()
                                            .forEach((entry) ->
                                                    Log.v(TAG,"VAL:"+entry.toString().split("=")[0]));
                                    }
                        }
                    }

                }
            }
        });
    }

}
