package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class AccountsDbTest {

    @Test
    public void addAccountTest() {
        AccountsCollection accounts = new MockDbUser().accountsMock();
        accounts.addAccountToCollection("JUnitName", null, "JUnitID");
        accounts.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                int found = 0;
                for (QueryDocumentSnapshot doc: task.getResult()) {
                    if (doc.getId().equals("JUnitName")){
                        found = 1;
                    }
                }
                assertEquals(1, found);
            }
        });
    }
}
