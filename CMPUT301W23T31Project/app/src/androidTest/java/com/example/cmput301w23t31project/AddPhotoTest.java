package com.example.cmput301w23t31project;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.view.WindowMetrics;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

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
    public void addLocationTest() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));

        // Allow time to scan QR code
        solo.clickOnView(solo.getView(R.id.scan_results_camera_button));
        solo.assertCurrentActivity("Wrong Activity", CameraActivity.class);
        assertNotNull(solo.getView(R.id.button1));
        assertNotNull(solo.getView(R.id.confirm_taken_photo));
    }
}
