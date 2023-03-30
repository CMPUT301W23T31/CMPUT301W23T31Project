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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

public class SeeSumQRCodesTest {
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
    public void SeeSumFromPlayerInfo() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.home_screen_player_info_button));

        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        assertTrue(solo.waitForView(R.id.player_info_total_score));
        //assertEquals(1, 1);
    }
    @Test
    public void SeeSumFromHomeScreen() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertTrue(solo.waitForView(R.id.home_screen_current_points));

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

