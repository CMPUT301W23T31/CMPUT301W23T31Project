package com.example.cmput301w23t31project;


import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import android.app.Activity;
import android.widget.EditText;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
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
        solo.enterText((EditText) solo.getView(R.id.search_scan), "Obese Orchid Sixty");
        solo.clickOnButton("Search");
        assertTrue(solo.waitForText("obese orchid sixty", 1, 2000));

        solo.clickOnView(solo.getView(R.id.nearby_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "Obese Orch");
        solo.clickOnButton("Search");
        assertTrue(solo.waitForText("obese orchid sixty", 1, 2000));

        solo.clickOnView(solo.getView(R.id.nearby_scans_search_scan_button));

        solo.enterText((EditText) solo.getView(R.id.search_scan), "khgv");
        solo.clickOnButton("Search");
        assertTrue(solo.waitForFragmentByTag("Error Message", 2000));
    }

}
