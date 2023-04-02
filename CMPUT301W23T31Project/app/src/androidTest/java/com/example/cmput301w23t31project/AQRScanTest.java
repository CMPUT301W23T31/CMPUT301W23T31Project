package com.example.cmput301w23t31project;


import static org.junit.Assert.assertNotNull;
import android.app.Activity;
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


public class AQRScanTest {
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
     * Tests to see if the user can successfully scan a QR code and put it into the database
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
        solo.clickLongOnTextAndPress("BACK TO HOME", 0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489"
                    )) {
                        code.getReference().update("Latitude", "0");
                        code.getReference().update("Longitude", "0");
                        found = 1;
                    }
                }
                assert found == 1;
            }
        });
    }

    /**
     * Tests to see if the SEE CODE DETAILS selection works after scanning a code
     */
    @Test
    public void scanNumberTwo() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));
        // Allow time to scan QR code
        solo.clickLongOnTextAndPress("OFF", 0);
        solo.clickLongOnTextAndPress("SEE CODE DETAILS", 0);
        solo.assertCurrentActivity("Wrong Activity", QRCodeStatsActivity.class);
    }
}
