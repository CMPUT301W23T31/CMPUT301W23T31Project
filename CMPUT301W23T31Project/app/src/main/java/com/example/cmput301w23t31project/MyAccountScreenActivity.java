package com.example.cmput301w23t31project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * Main class for "My Account" screen
 */
public class MyAccountScreenActivity extends HamburgerMenu {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String username;
    /**
     * On Create method
     * Sets up button (and other) functionality
     * Determines correct account based off of saved device ID
     * @param savedInstanceState saved instance state from past
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_screen);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        TextView player_name = findViewById(R.id.account_info_name);
        TextView username = findViewById(R.id.account_info_total_username);
        TextView email = findViewById(R.id.account_info_email);
        TextView phone_number = findViewById(R.id.account_info_phone_number);

        db.collection("Accounts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getString("DeviceID").equals(Utilities.
                                getDeviceId(MyAccountScreenActivity.this))) {

                            player_name.setText(document.getString("playername"));
                            username.setText(document.getId());
                            email.setText(document.getString("email"));
                            phone_number.setText(document.getString("phone"));
                            loadImageFromStorage(document.getString("path"));
                        }
                    }
                }
            }

        });
    }

    /**
     * For hamburger menu creation
     * @param menu relevant menu
     * @return boolean of whether all has gone well or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    /**
     * Handles how to proceed after option item is selected
     * @param item item selected from menu
     * @return boolean of whether to proceed
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);

    }

    /**
     * Gets image for account photo
     * @param path image file path
     */
    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, Utilities.getDeviceId(this)+".png");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView) findViewById(R.id.imgPicker);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}