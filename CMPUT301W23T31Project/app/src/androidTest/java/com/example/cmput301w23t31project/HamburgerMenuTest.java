package com.example.cmput301w23t31project;


import static org.junit.Assert.assertNotNull;
import android.app.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;


public class HamburgerMenuTest {
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
     * Tests to see if the hamburger menu functionality is correct
     */
    @Test
    public void MenuTest() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("Scan Code");
        solo.goBackToActivity("MainActivity");
        solo.clickLongOnTextAndPress("OFF", 0);
        solo.clickLongOnTextAndPress("BACK TO HOME", 0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("Explore");
        solo.assertCurrentActivity("Wrong Activity", ExploreScreenActivity.class);
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("Leaderboard");
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("Player Info");
        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("My Account");
        solo.assertCurrentActivity("Wrong Activity", MyAccountScreenActivity.class);
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnMenuItem("App Info");
        solo.assertCurrentActivity("Wrong Activity", AppInfoScreenActivity.class);
        solo.clickOnMenuItem("Home");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
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
