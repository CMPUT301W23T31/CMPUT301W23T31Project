package com.example.cmput301w23t31project;


import static androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread;
import static org.junit.Assert.assertNotNull;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;
import java.util.Objects;


public class ALoginActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule = new ActivityTestRule<>
                    (LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
        assertNotNull(activity);
    }

    /**
     * Tests to see if the user can successfully login
     */
    @Test
    public void enterLoginTest() {
        AccountsCollection accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getId(),
                            "TestName")) {
                        account.getReference().delete().addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Nothing needed here
                            }
                        });
                    }
                }
            }
        });
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "TestName");
        solo.enterText((EditText) solo.getView(R.id.login_activity_email), "TestEmail");
        solo.enterText((EditText) solo.getView(R.id.login_activity_phone), "TestPhone");
        solo.clickOnView(solo.getView(R.id.login_activity_button));
        solo.assertCurrentActivity("Wrong Activity", FinishLoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.finish_login_activity_player_name),
                "TestName");
        solo.clickOnView(solo.getView(R.id.finish_login_activity_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getId(),
                            "TestName")) {
                        found = 1;
                    }
                }
                assert found == 1;
            }
        });
    }

    /**
     * Tests to see if the user can successfully login, given they have some input troubles
     * @throws Throwable if image not added successfully to screen
     */
    @Test
    public void enterLoginWithIssueTest() throws Throwable {
        AccountsCollection accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getId(),
                            "NewTestName")) {
                        account.getReference().delete().addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // Nothing needed here
                            }
                        });
                    }
                }
            }
        });
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "TestName");
        solo.enterText((EditText) solo.getView(R.id.login_activity_email), "TestEmail");
        solo.enterText((EditText) solo.getView(R.id.login_activity_phone), "TestPhone");
        solo.clickOnView(solo.getView(R.id.login_activity_button));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "");
        solo.clickOnView(solo.getView(R.id.login_activity_button));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_activity_username), "NewTestName");
        solo.clickOnView(solo.getView(R.id.login_activity_button));
        solo.assertCurrentActivity("Wrong Activity", FinishLoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.finish_login_activity_player_name),
                "TestName");
        solo.clickOnView(solo.getView(R.id.finish_login_activity_take_image));
        solo.assertCurrentActivity("Wrong Activity", CameraActivity.class);
        ImageView image = (ImageView) solo.getView(R.id.imageView1);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                image.setImageResource(R.drawable.test_qr_code);
                image.setVisibility(View.VISIBLE);
            }
        });
        solo.clickOnView(solo.getView(R.id.confirm_taken_photo));
        solo.clickOnView(solo.getView(R.id.finish_login_activity_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getId(),
                            "NewTestName")) {
                        found = 1;
                    }
                }
                assert found == 1;
            }
        });
    }
}
