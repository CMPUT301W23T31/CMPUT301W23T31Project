package com.example.cmput301w23t31project;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import android.app.Activity;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


public class SearchQRCodeTest {
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
     * Tests searching nearby QR codes
     */
    @Test
    public void SearchNearbyQRCodeTest(){
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_explore_button));
        solo.assertCurrentActivity("Wrong Activity", ExploreScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.nearby_button));
        solo.assertCurrentActivity("Wrong Activity", NearByCodesActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));

        solo.clickOnView(solo.getView(R.id.nearby_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "BonyPikeGashDigUser");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("BonyPikeGashDigUser", 1, 2000));

        solo.clickOnView(solo.getView(R.id.nearby_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "gashd");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("BonyPikeGashDigUser", 1, 2000));

        solo.clickOnView(solo.getView(R.id.nearby_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "khgvx");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("Error!",1, 2000));

        solo.clickOnButton("SEARCH AGAIN");

        solo.enterText((EditText) solo.getView(R.id.search_scan), " ");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("Error!",1, 2000));
    }

    @Test
    public void SearchMyScansQRCodeTest(){
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_my_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));

        solo.clickOnView(solo.getView(R.id.my_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "BonyPikeGashDigUser");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("BonyPikeGashDigUser", 1, 2000));

        solo.clickOnView(solo.getView(R.id.my_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "gashd");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("BonyPikeGashDigUser", 1, 2000));

        solo.clickOnView(solo.getView(R.id.my_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "khgvx");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("Error!",1, 2000));

        solo.clickOnButton("SEARCH AGAIN");

        solo.enterText((EditText) solo.getView(R.id.search_scan), " ");
        solo.clickOnButton("SEARCH");
        assertTrue(solo.waitForText("Error!",1, 2000));
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
