package com.example.cmput301w23t31project;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.Objects;


/**
 * This class is responsible for displaying the title screen to the user
 * Also responsible for checking if the user device is recognized upon entry
 */
public class TitleScreenActivity extends AppCompatActivity {
    String DeviceID;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("QR HUNTER");
        setContentView(R.layout.fragment_title_screen);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    android.Manifest.permission.CAMERA}, 100);
        }
        DeviceID = new MyDeviceID(this).getInstance();


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
                            DeviceID)) {
                        intent = new Intent(TitleScreenActivity.
                                this, MainActivity.class);
                        intent.putExtra("Username", account.getId());
                        intent.putExtra("currentUser", account.getId());

                        startActivity(intent);
                        return;
                    }
                }

                // Otherwise, we proceed to log the user in for the first (only) time
                intent = new Intent(TitleScreenActivity.this, LoginActivity.class);
                intent.putExtra("Username", "");
                intent.putExtra("currentUser", "");
                startActivity(intent);
            }

        });
    }


}
