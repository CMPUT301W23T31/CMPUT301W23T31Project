package com.example.cmput301w23t31project;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.ByteArrayOutputStream;

/**
 * CameraActivity displays image that has been taken and allows user to display and upload
 */
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
        hash = intent.getStringExtra("ImageDivider");
        username = intent.getStringExtra("Username");
        images = new QRImages();
        Button photoButton = (Button) this.findViewById(R.id.button1);
        no_picture_message = this.findViewById(R.id.no_picture_message);
        no_picture_message.setVisibility(View.INVISIBLE);
        error_message = findViewById(R.id.error_message);
        error_message.setVisibility(View.INVISIBLE);
        try {
            images.getReference().get().addOnCompleteListener(task -> {
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        confirmButton = this.findViewById(R.id.confirm_taken_photo);
        photoButton.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        });

        confirmButton.setOnClickListener(v -> {
            int result = uploadImage(username, hash);
            if (result == 0) {
                finish();
            }
        });
    }

    /**
     * This method is activated when the camera closes, allows us to process photo taken
     * @param requestCode
     *      The code indicating that we requested the camera
     * @param resultCode
     *      The code indicating the camera use went ok
     * @param data
     *      The data received from using the camera
     */
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

    /**
     * This method uploads an image taken by the camera to FirebaseStorage
     * @param username
     *      The username of the user taking the picture
     * @param category
     *      The type of photo we are storing for the user
     * @return
     *      0 if photo successfully uploaded, -1 otherwise
     */
    private int uploadImage(String username, String category) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/"+username+"/"+category+".png");
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        if (data.length > 1024000) {
            imageView.setImageBitmap(null);
            imageView.setVisibility(View.INVISIBLE);
            confirmButton.setVisibility(View.INVISIBLE);
            error_message.setVisibility(View.VISIBLE);
            no_picture_message.setVisibility(View.VISIBLE);
            return -1;
        }

        imagesRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String generatedFilePath = downloadUri.getResult().toString();
            images.processQRImageInDatabase(username, category, generatedFilePath);
        });

        return 0;
    }
}
