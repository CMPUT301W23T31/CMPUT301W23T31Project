package com.example.cmput301w23t31project;


import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static junit.framework.Assert.assertNotNull;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.robotium.solo.Solo;
import java.io.ByteArrayOutputStream;


public class AddPhotoTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<TitleScreenActivity> rule = new ActivityTestRule<>
                    (TitleScreenActivity.class, true, true);

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
        assertNotNull(activity);
    }

    /**
     * Tests to ensure image of size > 100KB will not be uploaded
     * @throws Throwable if image not added successfully to screen
     */
    @Test
    public void failToAddLocationTest() throws Throwable {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(R.drawable.back);
                image.setVisibility(View.VISIBLE);
            }
        });
        solo.clickOnView(solo.getView(R.id.confirm_taken_photo));
        solo.waitForText("Error occurred");
    }

    /**
     * Tests to ensure image of size <= 100KB will not be uploaded
     * @throws Throwable if image not added successfully to screen
     */
    @Test
    public void successfullyAddLocationTest() throws Throwable {
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(R.drawable.test_qr_code);
                image.setVisibility(View.VISIBLE);
            }
        });
        solo.clickOnView(solo.getView(R.id.confirm_taken_photo));
    }
}
