package com.example.cmput301w23t31project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CollectionReference accounts = db.collection("Accounts");
        EditText username = findViewById(R.id.login_activity_username);
        EditText email = findViewById(R.id.login_activity_email);
        EditText phone  = findViewById(R.id.login_activity_phone);
        TextView badUsername = findViewById(R.id.login_activity_bad_username);
        Button login_button = findViewById(R.id.login_activity_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accounts.document(username.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        username.setText("");
                        badUsername.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent intent = new Intent(
                                LoginActivity.this, FinishLoginActivity.class);
                        intent.putExtra("username", username.getText().toString());
                        intent.putExtra("email", email.getText().toString());
                        intent.putExtra("phone", phone.getText().toString());
                        startActivity(intent);
                    }
                });
            }
        });
    }

}
