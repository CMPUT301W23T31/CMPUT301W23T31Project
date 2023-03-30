package com.example.cmput301w23t31project;

import static junit.framework.TestCase.assertTrue;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Objects;

public class PlayerInfoTest {
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
    public void TestValues(){
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);
        solo.clickOnView(solo.getView(R.id.title));

//        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "TestPlayerInfoName");
//        solo.enterText((EditText) solo.getView(R.id.login_activity_email), "TestPlayerInfoEmail");
//        solo.enterText((EditText) solo.getView(R.id.login_activity_phone), "TestPlayerInfoPhone");
//
//        solo.clickOnView(solo.getView(R.id.login_activity_button));
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "");
//        solo.clickOnView(solo.getView(R.id.login_activity_button));
//        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
//
//        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "NewTestPlayerInfoName");
//        solo.clickOnView(solo.getView(R.id.login_activity_button));
//
//        solo.assertCurrentActivity("Wrong Activity", FinishLoginActivity.class);
//        solo.enterText((EditText) solo.getView(R.id.finish_login_activity_player_name), "TestPlayerInfoName");
//        solo.clickOnView(solo.getView(R.id.finish_login_activity_button));
//        solo.clickLongOnTextAndPress("While using the app", 0);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.home_screen_scan_code_button));
        // Allow time to scan QR code
        solo.waitForActivity("ScanResultsFragment", 1);
        //solo.clickLongOnTextAndPress("BACK TO HOME", -1);
        solo.clickLongOnText("BACK TO HOME");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);



        PlayerInfoCollection playerInfoCollection = new PlayerInfoCollection();
        /*playerInfoCollection.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot code : task.getResult()) {
                    if (Objects.equals(code.getId(),
                            "NewTestPlayerInfoName")) {
                        Log.i("TAG",code.get("Highest Scoring QR Code").toString());
                        assertTrue((code.get("Highest Scoring QR Code").toString()).equals("106"));
                        assertTrue((code.get("Lowest Scoring QR Code").toString()).equals("10"));
                        assertTrue((code.get("Total Scans").toString()).equals("3"));
                        assertTrue((code.get("Total Score").toString()).equals("150"));


                        found = 1;
                    }
                }
                assert found == 1;
            }
        });*/





    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
