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

public class SeeGameScoresTest {
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
    public void SeeHighScores() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.imageView4));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        assertTrue(solo.waitForView(R.id.leaderboard_content_user_name));
    }

    @Test
    public void SeeHighsInDifferentOrders() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.imageView4));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_by_high_score_button));
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_by_count_button));
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_by_total_score_button));
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);

        // Tested once we implement it
        //solo.clickOnView(solo.getView(R.id.leaderboard_by_high_score_button));

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
