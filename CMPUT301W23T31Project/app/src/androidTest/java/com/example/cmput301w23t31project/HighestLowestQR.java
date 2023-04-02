package com.example.cmput301w23t31project;


import android.app.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;


public class HighestLowestQR {
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
     * Tests to see if the high and low scores of QR codes are displayed
     */
    @Test
    public void QRCodeTest() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_player_info_button));
        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        assertTrue(solo.waitForText("Highest Scoring QR Code",
                1, 2000));
        assertTrue(solo.waitForText("Lowest Scoring QR Code",
                1, 2000));
        assertTrue(solo.waitForText("2690", 1, 2000));
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


