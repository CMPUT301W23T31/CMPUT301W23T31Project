package com.example.cmput301w23t31project;

import android.app.Activity;

import org.junit.After;
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

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Objects;

public class removeCodeTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<TitleScreenActivity> rule =
            new ActivityTestRule<>(TitleScreenActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void scanBasicQRCode() {
        // To test, use the test_qr_code.png QR Code
        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489")) {
                        code.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
                    }
                }
            }
        });

        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));

        // Allow time to scan QR code

        solo.clickLongOnTextAndPress("BACK TO HOME", 0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489")) {
                        code.getReference().update("Latitude", "0");
                        code.getReference().update("Longitude", "0");
                        found = 1;
                    }
                }
                assert found == 1;
            }
        });
    }

    @Test
    public void QRCodeTest() throws InterruptedException {


        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_screen_my_scans_button));

        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
        MyScansScreenActivity activity = (MyScansScreenActivity) solo.getCurrentActivity();
        final ArrayList<QRCode> datalist = activity.datalist;
        int list_size = datalist.size();
        Thread.sleep(100);
        solo.clickOnView(solo.getView(R.id.delete));
        assertEquals(datalist.size(), list_size-1);
    }

    @Test
    public void scanBasicQRThroughProfileCode() {
        // To test, use the test_qr_code.png QR Code
        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489")) {
                        code.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });
                    }
                }
            }
        });

        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));

        // Allow time to scan QR code

        solo.waitForActivity("ScanResultsFragment", 5);
        solo.clickLongOnTextAndPress("BACK TO HOME", 0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489")) {
                        code.getReference().update("Latitude", "0");
                        code.getReference().update("Longitude", "0");
                        found = 1;
                    }
                }
                assert found == 1;
            }
        });
    }

    @Test
    public void QRCodeTestThroughProfile() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_player_info_button));

        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.player_info_see_scans_button));

        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
        MyScansScreenActivity activity = (MyScansScreenActivity) solo.getCurrentActivity();
        final ArrayList<QRCode> datalist = activity.datalist;
        int list_size = datalist.size();
        solo.clickOnView(solo.getView(R.id.delete));
        assertEquals(datalist.size(), list_size-1);
        //assertEquals(1, 1);
    }

    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
