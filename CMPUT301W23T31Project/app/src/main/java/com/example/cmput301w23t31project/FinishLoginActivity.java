package com.example.cmput301w23t31project;


import static android.content.ContentValues.TAG;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * The FinishLoginActivity class finishes login in the user, asking for a
 * player name and a profile picture
 */
public class FinishLoginActivity extends AppCompatActivity {
    // Note the value of GET_FROM_GALLERY is random, it is just used as a request code to
    // access the user's photo gallery
    private static final int GET_FROM_GALLERY = 3;
    String path = "";
    String DeviceID;
    TextView photo_text;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("LOGIN");
        setContentView(R.layout.activity_finish_login);
        Intent oldIntent = getIntent();
        DeviceID = new MyDeviceID(this).getInstance();
        // Get the previous login page's information, and access necessary xml fields
        username = oldIntent.getStringExtra("username");
        String email = oldIntent.getStringExtra("email");
        String phone = oldIntent.getStringExtra("phone");
        EditText player_name = findViewById(R.id.finish_login_activity_player_name);
        Button img = findViewById(R.id.finish_login_activity_image);
        Button take_photo = findViewById(R.id.finish_login_activity_take_image);
        Button setUp = findViewById(R.id.finish_login_activity_button);
        photo_text = findViewById(R.id.finish_login_activity_image_exists);
        // Listen for a click on the upload image button. Once a click occurs, start an
        // activity to access the user's photo gallery to choose a photo
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
            }
        });

        // Listen for a click to finish setting up the account. Once a click is recognized,
        // push the information to the main activity and transfer control to main menu
        setUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        FinishLoginActivity.this, MainActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("playername", player_name.getText().toString());
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });

        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        FinishLoginActivity.this, CameraActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("ImageDivider", "profile");
                startActivity(intent);
                photo_text.setText("Image successfully taken");
                photo_text.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * This method handles a request to upload a particular image from the user's photo gallery
     * @param requestCode the code indicating the type of request made
     * @param resultCode the code indicating what response to provide after the request is handled
     * @param data the data provided along with the request code (in this case the image data)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes and creates a bitmap and path for the selected image
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                // Create a bitmap to store image data, and extract its path in the user's device
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                        selectedImage);
                uploadImage(username, bitmap);
                photo_text.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.d(TAG, "Wrong");
            }
        }
    }

    private void uploadImage(String username, Bitmap bitmap) {
        QRImages images = new QRImages();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+username+"/"+ "profile" +".png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        if (data.length > 1024000) {
            photo_text.setText("Error occurred uploading image");
            return;
        }
        imagesRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String generatedFilePath = downloadUri.getResult().toString();
                Log.d("## Stored path is ", generatedFilePath);
                images.processQRImageInDatabase(username, "profile", generatedFilePath);
            }
        });
        photo_text.setText("Image successfully uploaded");
    }
}
