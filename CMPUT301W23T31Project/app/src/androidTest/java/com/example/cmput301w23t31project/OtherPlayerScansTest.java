package com.example.cmput301w23t31project;


import static org.junit.Assert.assertNotNull;
import android.app.Activity;
import android.view.WindowMetrics;
import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.platform.view.inspector.WindowInspectorCompat;
import androidx.test.rule.ActivityTestRule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Objects;


public class OtherPlayerScansTest {
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
     * Tests to see if user can see other player's scans entering from the leaderboard
     */
    @Test
    public void viewFromLeaderboardTest() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.imageView4));
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_content_profile_button));
        solo.assertCurrentActivity("Wrong Activity", PlayerProfileActivity.class);
        solo.waitForText("Player Info", 1, 2000);
        solo.clickOnView(solo.getView(R.id.player_profile_see_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
    }

    /**
     * Tests to see if user can see other player's scans after scanning a QR code
     */
    @Test
    public void viewFromScanQRCode() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));
        solo.waitForActivity("ScanResultsFragment", 5);
        solo.clickOnView(solo.getView(R.id.location_button));
        solo.clickLongOnTextAndPress("SEE CODE DETAILS", 0);
        solo.assertCurrentActivity("Wrong Activity", QRCodeStatsActivity.class);
    }

    /**
     * Tests to see if user can see other player's scans entering from the map
     */
    @Test
    public void viewFromExplore() {
        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get().addOnCompleteListener(task -> {
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
        });
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_explore_button));
        solo.assertCurrentActivity("Wrong Activity",  ExploreScreenActivity.class);
        WindowMetrics w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX(), w.getBounds().centerY());
        solo.assertCurrentActivity("Wrong Activity",  QRCodeStatsActivity.class);
        solo.clickOnView(solo.getView(R.id.player_detail_view_profile_button));
        solo.assertCurrentActivity("Wrong Activity",PlayerProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.player_profile_see_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
    }

    /**
     * Close activity after each test
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}

