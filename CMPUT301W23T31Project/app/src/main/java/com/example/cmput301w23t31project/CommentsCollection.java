package com.example.cmput301w23t31project;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;

import java.util.HashMap;

public class CommentsCollection extends QRDatabase{
    public int docId;
    public CommentsCollection() {
        super("Comments");
    }

    public void addCommentToCollection(String username, String comment, String date, String hash) {

        AggregateQuery countQuery = collection.count();
        HashMap<String, String> CommentData = new HashMap<>();
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Count fetched successfully
                    AggregateQuerySnapshot snapshot = task.getResult();
                    docId = (int) snapshot.getCount();
                    docId += 1;
                    CommentData.put("comment", comment);
                    collection.document(Integer.toString(docId)).set(CommentData);
                    CommentData.put("user", username);
                    collection.document(Integer.toString(docId)).set(CommentData);
                    CommentData.put("date", date);
                    collection.document(Integer.toString(docId)).set(CommentData);
                    CommentData.put("QRhash", hash);
                    collection.document(Integer.toString(docId)).set(CommentData);
                }
            }
        });


    }
}
