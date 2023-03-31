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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private TextView no_picture_message;
    private TextView error_message;
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
        no_picture_message = this.findViewById(R.id.no_picture_message);
        no_picture_message.setVisibility(View.INVISIBLE);
        error_message = findViewById(R.id.error_message);
        error_message.setVisibility(View.INVISIBLE);
        try {
            images.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    int found = 0;
                    for (QueryDocumentSnapshot doc: task.getResult()) {
                        if (doc.getId().equals(username) && doc.getData().containsKey(hash)) {
                            String storage = doc.getString(hash);
                            Glide.with(CameraActivity.this)
                                    .load(storage)
                                    .into(imageView);
                            imageView.setVisibility(View.VISIBLE);
                            confirmButton.setVisibility(View.VISIBLE);
                            no_picture_message.setVisibility(View.INVISIBLE);
                            found = 1;
                        }
                    }
                    if (found == 0) {
                        no_picture_message.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        confirmButton = this.findViewById(R.id.confirm_taken_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = uploadImage();
                Log.d("HEHEHEHEHEHEHEHEHEHEHEHEHE", String.valueOf(result));
                if (result == 0) {
                    finish();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
            imageView.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
            error_message.setVisibility(View.INVISIBLE);
            no_picture_message.setVisibility(View.INVISIBLE);
        }
    }

    private int uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+username+"/"+hash+".png");
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        if (data.length > 102400) {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.INVISIBLE);
            confirmButton.setVisibility(View.INVISIBLE);
            error_message.setVisibility(View.VISIBLE);
            no_picture_message.setVisibility(View.VISIBLE);
            return -1;
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
                images.processQRImageInDatabase(username, hash, generatedFilePath);
            }
        });
        return 0;
    }
}
