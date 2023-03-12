package com.example.cmput301w23t31project;




import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowMetrics;
import android.widget.EditText;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.platform.view.inspector.WindowInspectorCompat;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.emulators.EmulatedServiceSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import java.util.Objects;


public class OtherProfilesTest {
    private Solo solo;


    @Rule
    public ActivityTestRule<TitleScreenActivity> rule =
            new ActivityTestRule<>(TitleScreenActivity.class, true, true);

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
    }

    @Test
    public void viewFromLeaderboardTest() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.imageView4));
        solo.assertCurrentActivity("Wrong Activity", LeaderboardActivity.class);
        solo.clickOnView(solo.getView(R.id.leaderboard_content_profile_button));
        solo.assertCurrentActivity("Wrong Activity", PlayerProfileActivity.class);
        solo.waitForText("Player Info", 1, 2000);
    }

    @Test
    public void viewFromScanQRCode() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));
        solo.waitForActivity("ScanResultsFragment", 5);
        solo.clickLongOnTextAndPress("SEE CODE DETAILS", 0);
        solo.assertCurrentActivity("Wrong Activity", QRCodeStatsActivity.class);
        solo.clickOnView(solo.getView(R.id.player_detail_view_profile_button));
        solo.assertCurrentActivity("Wrong Activity",PlayerProfileActivity.class);
    }

    @Test
    public void viewFromExplore() throws WindowInspectorCompat.ViewRetrievalException {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        solo.clickOnView(solo.getView(R.id.tap_to_enter));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_explore_button));
        solo.assertCurrentActivity("Wrong Activity",  ExploreScreenActivity.class);
        WindowMetrics w = rule.getActivity().getWindowManager().getCurrentWindowMetrics();
        solo.clickOnScreen(w.getBounds().centerX(), w.getBounds().centerY());
        solo.clickOnView(solo.getView(R.id.player_detail_view_profile_button));
        solo.assertCurrentActivity("Wrong Activity",PlayerProfileActivity.class);
    }

}
