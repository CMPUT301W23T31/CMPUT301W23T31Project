package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Objects;


/**
 * The TitleScreenActivity class is responsible for displaying the title screen to the user.
 * It is also responsible for checking if the user device is recognized upon entry
 */
public class TitleScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title_screen);
    }

    /**
     * This method listens for a user tap on the screen and supplies an argument to determine
     * future control flow
     */
    public void onTap(View v){
        FirebaseFirestore QRdb = FirebaseFirestore.getInstance();
        // Access the accounts in the database
        QRdb.collection("Accounts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            determineControl(task);
                        }
                    }
                });
    }

    /**
     * This method determines whether the user needs to login for the first and only time,
     * or if the user can bypass the login screen(s)
     * @param task
     *      The operation that holds a snapshot of the query
     */
    public void determineControl(Task<QuerySnapshot> task) {
        // If there exists a document that has a device ID matching the
        // current user device ID, then we transfer control to main menu
        for (QueryDocumentSnapshot document : task.getResult()) {
            if (Objects.equals(document.getString("DeviceID"),
                    Utilities.
                            getDeviceId(TitleScreenActivity.this))) {
                Intent intent = new Intent(
                        TitleScreenActivity.
                                this, MainActivity.class);
                intent.putExtra("username", "");
                intent.putExtra("username_present",
                        document.getId());
                startActivity(intent);
                return;
            }
        }
        // Otherwise, we proceed to log the user in for the first (only) time
        Intent intent = new Intent(
                TitleScreenActivity.this,
                LoginActivity.class);
        intent.putExtra("username", "");
        startActivity(intent);
    }
}
