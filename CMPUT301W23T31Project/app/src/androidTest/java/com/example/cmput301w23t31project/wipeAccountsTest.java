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

public class wipeAccountsTest {
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
    public void WipeAccounts() {
        solo.assertCurrentActivity("Wrong Activity", TitleScreenActivity.class);

        AccountsCollection accounts = new AccountsCollection();
        QRCodesCollection codesCollection = new QRCodesCollection();
        QRPlayerScans playerScans = new QRPlayerScans();
        PlayerInfoCollection info = new PlayerInfoCollection();

        accounts.getReference().document("TestName").delete();
        accounts.getReference().document("NewTestName").delete();
        codesCollection.getReference().document(
                "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489").
                delete();
        playerScans.getReference().document("NewTestName").delete();
        info.getReference().document("NewTestName").delete();


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