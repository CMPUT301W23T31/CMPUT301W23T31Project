package com.example.cmput301w23t31project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Objects;

/**
 * Responsible for displaying the title screen to the user
 * Also responsible for checking if the user device is recognized upon entry
 */
public class TitleScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title_screen);
    }

    /**
     * Listens for a user tap on the screen and supplies an argument to determine future control flow
     */
    public void onTap(View v){
        AccountsCollection accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Intent intent;
                // Find if the user already has an account. If so, move to home screen
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getString("DeviceID"),
                            Utilities.getDeviceId(TitleScreenActivity.this))) {
                        intent = new Intent(TitleScreenActivity.
                                this, MainActivity.class);
                        intent.putExtra("username", account.getId());

                        startActivity(intent);
                        return;
                    }
                }

                // Otherwise, we proceed to log the user in for the first (only) time
                intent = new Intent(TitleScreenActivity.this, LoginActivity.class);
                intent.putExtra("username", "");
                startActivity(intent);
            }

        });
    }


}
