package com.example.cmput301w23t31project;


import android.app.Activity;
import android.widget.EditText;
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
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

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
    }

    @Test
    public void enterLoginTest() {
        AccountsCollection accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getId(),
                            "TestName")) {
                        account.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
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

        solo.enterText((EditText) solo.getView(R.id.finish_login_activity_player_name), "TestName");
        solo.clickOnView(solo.getView(R.id.finish_login_activity_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    @Test
    public void enterLoginWithIssueTest() {
        AccountsCollection accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot account : task.getResult()) {
                    if (Objects.equals(account.getId(),
                            "NewTestName")) {
                        account.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
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
        solo.enterText((EditText) solo.getView(R.id.finish_login_activity_player_name), "TestName");
        solo.clickOnView(solo.getView(R.id.finish_login_activity_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        accounts = new AccountsCollection();
        accounts.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
