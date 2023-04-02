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
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.Objects;


public class removeCodeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<TitleScreenActivity> rule = new ActivityTestRule<>
                    (TitleScreenActivity.class, true, true);

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
        assertNotNull(activity);
    }

    /**
     * Tests to ensure that a QR code is added correctly to database by user
     */
    @Test
    public void scanBasicQRCode() {
        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot code : task.getResult()) {
                if (Objects.equals(code.getId(),
                        "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489")) {
                    code.getReference().delete().addOnSuccessListener(unused -> {
                    });
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
        qrCodes.getReference().get().addOnCompleteListener(task -> {
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
        });
    }

    /**
     * Tests removal of QR code through MyScansScreen
     * @throws InterruptedException if issue occurs with Thread.sleep
     */
    @Test
    public void QRCodeTest() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_my_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
        MyScansScreenActivity activity = (MyScansScreenActivity) solo.getCurrentActivity();
        final ArrayList<QRCode> datalist = activity.datalist;
        int list_size = datalist.size();
        solo.clickOnView(solo.getView(R.id.delete_button));
        Thread.sleep(100);
        assertEquals(datalist.size(), list_size-1);
    }

    /**
     * Tests scan of QR code through player's profile
     */
    @Test
    public void scanBasicQRThroughProfileCode() {
        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot code : task.getResult()) {
                if (Objects.equals(code.getId(),
                        "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489")) {
                    code.getReference().delete().addOnSuccessListener(unused -> {
                    });
                }
            }
        });
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));
        // Allow time to scan QR code
        solo.waitForActivity("ScanResultsFragment", 5);
        solo.clickLongOnTextAndPress("BACK TO HOME", 0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(task -> {
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
        });
    }

    /**
     * Tests if QR code is correctly removed when accessed through player's info screen
     * @throws InterruptedException if issue occurs with Thread.sleep
     */
    @Test
    public void QRCodeTestThroughProfile() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_player_info_button));
        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.player_info_see_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
        MyScansScreenActivity activity = (MyScansScreenActivity) solo.getCurrentActivity();
        final ArrayList<QRCode> datalist = activity.datalist;
        int list_size = datalist.size();
        solo.clickOnView(solo.getView(R.id.delete_button));
        Thread.sleep(100);
        assertEquals(datalist.size(), list_size-1);
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
