package com.example.cmput301w23t31project;
import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

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

public class addCommentsTest {
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

//    @Test
//    public void QRCodeTest() {
//        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
//
//        solo.clickOnView(solo.getView(R.id.tap_to_enter));
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//
//        solo.clickOnView(solo.getView(R.id.home_screen_my_scans_button));
//
//        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
//        assertTrue(solo.waitForView(R.id.leaderboard_list));
//        //assertEquals(1, 1);
//    }

    @Test
    public void QRCodeTestThroughProfile() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_player_info_button));

        solo.assertCurrentActivity("Wrong Activity", PlayerInfoScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.player_info_see_scans_button));

        solo.assertCurrentActivity("Wrong Activity", MyScansScreenActivity.class);
        MyScansScreenActivity activity = (MyScansScreenActivity) solo.getCurrentActivity();
        final ListView qrCodes = activity.qrcodeList; // Get the listview
        solo.clickInList((int)qrCodes.getItemIdAtPosition(1)); //Select ClEAR ALL
        solo.assertCurrentActivity("Wrong Activity", QRCodeStatsActivity.class);
        solo.clickOnView(solo.getView(R.id.qr_code_stats_comment_list_button));
        solo.assertCurrentActivity("Wrong Activity", QRCodeStatsCommentsActivity.class);
        solo.clickOnView(solo.getView(R.id.comments_add_button));
        solo.enterText((EditText) solo.getView(R.id.add_comment), "Test Comment");
        solo.clickOnView(solo.getView(R.id._add_button));
        assertTrue(solo.waitForText("Test Comment", 1, 2000));
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