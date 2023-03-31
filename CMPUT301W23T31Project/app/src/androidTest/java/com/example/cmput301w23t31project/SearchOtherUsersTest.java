package com.example.cmput301w23t31project;

import android.app.Activity;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.EditText;

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

public class SearchOtherUsersTest {
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
    public void SeeOthers() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.title));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnView(solo.getView(R.id.imageView4));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Test");
        WindowMetrics w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX()+270, w.getBounds().centerY()+100);
        Log.d("Coords", w.getBounds().centerX() + "" + w.getBounds().centerY());
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Te");
        WindowMetrics w2 = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w2.getBounds().centerX()+270, w2.getBounds().centerY()+100);
        Log.d("Coords", w2.getBounds().centerX() + "" + w2.getBounds().centerY());
        assertTrue(solo.waitForText("NewTestName", 1, 2000));


        solo.clickOnView(solo.getView(R.id.leaderboard_by_high_score_button));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Test");
        w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX()+270, w.getBounds().centerY()+100);
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Te");
        w2 = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w2.getBounds().centerX()+270, w2.getBounds().centerY()+100);
        Log.d("Coords", w2.getBounds().centerX() + "" + w2.getBounds().centerY());
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        solo.clickOnView(solo.getView(R.id.leaderboard_by_count_button));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Test");
        w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX()+270, w.getBounds().centerY()+100);
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Te");
        w2 = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w2.getBounds().centerX()+270, w2.getBounds().centerY()+100);
        Log.d("Coords", w2.getBounds().centerX() + "" + w2.getBounds().centerY());
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        solo.clickOnView(solo.getView(R.id.leaderboard_by_total_score_button));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Test");
        w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX()+270, w.getBounds().centerY()+100);
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Te");
        w2 = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w2.getBounds().centerX()+270, w2.getBounds().centerY()+100);
        Log.d("Coords", w2.getBounds().centerX() + "" + w2.getBounds().centerY());
        assertTrue(solo.waitForText("NewTestName", 1, 2000));

        /* Done when regional is implemented
        solo.clickOnView(solo.getView(R.id.imageView4));

        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);

        solo.clickOnView(solo.getView(R.id.leaderboard_search_user_button));

        solo.enterText((EditText) solo.getView(R.id.search_user), "Test");
        WindowMetrics w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX()+270, w.getBounds().centerY()+100);
        assertTrue(solo.waitForText("NewTestName", 1, 2000));
        */


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
