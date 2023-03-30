package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * The LoginActivity class is responsible for obtaining a user's username, email, and phone number
 */
public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText email;
    EditText phone;
    TextView badUsername;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CollectionReference accounts = db.collection("Accounts");
        // Access the relevant fields from the xml
        username = findViewById(R.id.login_activity_username);
        email = findViewById(R.id.login_activity_email);
        phone  = findViewById(R.id.login_activity_phone);
        badUsername = findViewById(R.id.login_activity_bad_username);
        Button login_button = findViewById(R.id.login_activity_button);

        // Let button listen for a click
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accounts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loginUser(task);
                    }
                });
            }
        });
    }

    /**
     * This method verifies that the username the user provided is not previously taken, and
     * subsequently logs in the user
     * @param task the operation that holds a snapshot of the query
     */
    public void loginUser(Task<QuerySnapshot> task) {
        int found = 0;
        if (username.getText().toString().equals("")) {
            found = 1;
            badUsername.setVisibility(View.VISIBLE);
        }
        // Check if the query contains a document with a matching username
        for (QueryDocumentSnapshot doc : task.getResult()) {
            if (doc.getId().equals(username.getText().toString())) {
                found = 1;
                // If matching username found, let the user know they can't use that username
                username.setText("");
                badUsername.setVisibility(View.VISIBLE);
            }
        }
        // If no match, move on to finish logging in the user
        if (found == 0) {
            Intent intent = new Intent(
                    LoginActivity.this, FinishLoginActivity.class);
            intent.putExtra("username", username.getText().toString());
            intent.putExtra("email", email.getText().toString());
            intent.putExtra("phone", phone.getText().toString());
            startActivity(intent);
        }
    }

    /**
     * This method returns the Device ID (used here for testing)
     * @return
     *      Device ID as a String
     */
    public String getDeviceID() {
        return Utilities.getDeviceId(this);
    }
}
