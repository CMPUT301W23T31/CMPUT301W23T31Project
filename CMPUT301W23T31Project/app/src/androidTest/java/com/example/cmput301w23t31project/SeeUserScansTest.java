package com.example.cmput301w23t31project;


import android.app.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


public class SeeUserScansTest{
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
     * Tests to see if user can view their scans list
     */
    @Test
    public void QRCodeTest() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_my_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
    }

    /**
     * Tests to see if user can see their list of scans originating from their player info page
     */
    @Test
    public void QRCodeTestThroughProfile() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_player_info_button));
        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.player_info_see_scans_button));
        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_list));
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
