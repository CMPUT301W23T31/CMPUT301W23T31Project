package com.example.cmput301w23t31project;

import static androidx.test.InstrumentationRegistry.getContext;
import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.ImageView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.robotium.solo.Solo;

import java.io.ByteArrayOutputStream;
import java.util.Objects;


public class AddPhotoTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<TitleScreenActivity> rule =
            new ActivityTestRule<>(TitleScreenActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void addLocationTest() throws Throwable {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));

        // Allow time to scan QR code
        solo.clickOnView(solo.getView(R.id.scan_results_camera_button));
        solo.assertCurrentActivity("Wrong Activity", CameraActivity.class);
        assertNotNull(solo.getView(R.id.button1));
        assertNotNull(solo.getView(R.id.confirm_taken_photo));
        ImageView image = (ImageView) solo.getView(R.id.imageView1);
//        Glide.with(getContext())
//                .load(storage)
//                .into(image);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(R.drawable.back);
                image.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        QRImages images = new QRImages();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images/NewTestName/b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489.png");
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        if (!(data.length > 102400)) {
            QRImages finalImages = images;
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
                    finalImages.processQRImageInDatabase("NewTestName", "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489", generatedFilePath);
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    solo.getView(R.id.error_message).setVisibility(View.VISIBLE);
                }
            });
        }
        solo.waitForText("Error occurred");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(R.drawable.test_qr_code);
                image.setVisibility(View.VISIBLE);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        images = new QRImages();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imagesRef = storageRef.child("images/NewTestName/b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489.png");
        bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        data = baos.toByteArray();
        if (!(data.length > 102400)) {
            QRImages finalImages1 = images;
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
                    finalImages1.processQRImageInDatabase("NewTestName", "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489", generatedFilePath);
                }
            });
        }
    }
}
