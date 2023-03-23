package com.example.cmput301w23t31project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.internal.StorageReferenceUri;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class CameraActivity extends Activity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private Button confirmButton;
    private String hash;
    private String username;
    private QRImages images;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        this.imageView = this.findViewById(R.id.imageView1);
        Intent intent = getIntent();
        hash = intent.getStringExtra("Hash");
        username = intent.getStringExtra("Username");
        images = new QRImages();
        Button photoButton = (Button) this.findViewById(R.id.button1);
        try {
            images.getReference().document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String storage = task.getResult().getString(hash);
                    Glide.with(CameraActivity.this)
                            .load(storage)
                            //.load("https://firebasestorage.googleapis.com/v0/b/cmput301w23t31.appspot.com/o/images%2Ffrench%2F356c9cccf52cafbe5bad5273da43c8038e1ca53b6648cf24cceabd36809394f0.png?alt=media&token=f669cd73-9db9-4db1-8c26-e5baf207608a").sizeMultiplier((float) 0.28)
                            .into(imageView);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setVisibility(View.VISIBLE);

        confirmButton = this.findViewById(R.id.confirm_taken_photo);
        confirmButton.setVisibility(View.VISIBLE);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                imageView.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                finish();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    private void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        Log.d("HEHEHEHEHEHEHEHEHE", "HI");
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+username+"/"+hash+".png");
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
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
                Log.d("## Stored path is ",generatedFilePath);
                images.processQRImageInDatabase(username, hash, generatedFilePath);
            }
        });
    }
}
