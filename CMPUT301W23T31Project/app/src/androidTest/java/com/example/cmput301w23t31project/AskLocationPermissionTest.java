package com.example.cmput301w23t31project;


import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.Objects;


public class AskLocationPermissionTest {
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
     * Tests if the location toggle button works after scanning a QR code
     */
    @Test
    public void scanBasicQRCode() {
        // To test, use the test_qr_code.png QR Code
        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489"
                    )) {
                        code.getReference().delete().addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
                    }
                }
            }
        });
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));
        // Allow time to scan QR code
        solo.waitForActivity("ScanResultsFragment", 5);
        solo.clickOnView(solo.getView(R.id.location_button));
        solo.clickOnView(solo.getView(R.id.location_button));
        solo.clickLongOnText("BACK TO HOME");
        solo.clickOnView(solo.getView(R.id.home_screen_my_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
        solo.clickOnView(solo.getView(R.id.code_info_button));
        solo.assertCurrentActivity("Wrong Activity", QRCodeStatsActivity.class);
        assertTrue(solo.waitForText("No Location", 1, 3000));
    }
}
