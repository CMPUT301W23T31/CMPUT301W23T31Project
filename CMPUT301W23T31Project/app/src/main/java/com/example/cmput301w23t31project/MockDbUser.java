package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MockDbUser {
    public MockDbUser() {
        //Leave empty
    }

    public AccountsCollection accountsMock() {
        return new AccountsCollection();
    }

    public CommentsCollection commentsMock() {
        return new CommentsCollection();
    }

    public PlayerInfoCollection playerInfoMock() {
        return new PlayerInfoCollection();
    }

    public QRCodesCollection qrCodesMock() {
        return new QRCodesCollection();
    }

    public QRImages qrImagesMock() {
        return new QRImages();
    }

    public QRPlayerScans playerScansMock() {
        return new QRPlayerScans();
    }

    public int testAccount() {
        AccountsCollection accounts = new AccountsCollection();
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
                assert(found == 1);
            }
        });
        return 1;
    }
}
