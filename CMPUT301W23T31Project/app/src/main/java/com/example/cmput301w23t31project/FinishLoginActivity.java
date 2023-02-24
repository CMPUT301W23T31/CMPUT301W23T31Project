package com.example.cmput301w23t31project;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.IOException;

import io.grpc.okhttp.internal.Util;

public class FinishLoginActivity extends AppCompatActivity {
    private static final int GET_FROM_GALLERY = 3;
    Bitmap bitmap = null;
    String path = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_login);
        Intent oldIntent = getIntent();
        String username = oldIntent.getStringExtra("username");
        String email = oldIntent.getStringExtra("email");
        String phone = oldIntent.getStringExtra("phone");
        EditText playername = findViewById(R.id.finish_login_activity_player_name);
        Button img = findViewById(R.id.finish_login_activity_image);
        Button setUp = findViewById(R.id.finish_login_activity_button);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });
        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        FinishLoginActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("playername", playername.getText().toString());
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                path = Utilities.saveToInternalStorage(bitmap, this, Utilities.getDeviceId(this)+".png");
                } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "Wrong");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "Wrong");
            }
        }
    }
}
