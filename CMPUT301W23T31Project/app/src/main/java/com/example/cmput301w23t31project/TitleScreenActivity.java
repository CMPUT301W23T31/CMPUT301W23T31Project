package com.example.cmput301w23t31project;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TitleScreenActivity extends AppCompatActivity {
    FirebaseFirestore QRdb = FirebaseFirestore.getInstance();
    boolean found = false;
    String username = "";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title_screen);
    }
    public void onTap(View view){
        DocumentReference doc = QRdb.collection("Accounts").
                document(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!(documentSnapshot == null)) {
                    username = documentSnapshot.getString("username");
                }
            }
        });
        QRdb.collection("Accounts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))) {
                                    Intent intent = new Intent(TitleScreenActivity.this, MainActivity.class);
                                    intent.putExtra("username", "");

                                    intent.putExtra("username_present", (String)document.getData().get("username"));
                                    found = true;
                                    startActivity(intent);
                                }
                            }
                            if (!found) {
                                Intent intent = new Intent(TitleScreenActivity.this, LoginActivity.class);
                                intent.putExtra("username", "");
                                startActivity(intent);
                            }
                        }
                    }
                });
    }
}
