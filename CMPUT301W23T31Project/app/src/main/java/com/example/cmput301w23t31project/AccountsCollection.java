package com.example.cmput301w23t31project;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Objects;

public class AccountsCollection extends QRDatabase {

    public AccountsCollection() {
        super("Accounts");
    }

    /**
     * Adds account to database collection
     * @param username username of player being added
     * @param intent intent being passed with user info (email/phone/playername/path)
     * @param ID device ID associated with account being added to collection
     */
    public void addAccountToCollection(String username, Intent intent, String ID) {
        HashMap<String, String> AccountData = new HashMap<>();
        AccountData.put("DeviceID", ID);
        collection.document(username).set(AccountData);
        AccountData.put("email", intent.getStringExtra("email"));
        collection.document(username).set(AccountData);
        AccountData.put("phone", intent.getStringExtra("phone"));
        collection.document(username).set(AccountData);
        AccountData.put("playername", intent.getStringExtra("playername"));
        collection.document(username).set(AccountData);
        AccountData.put("path", intent.getStringExtra("path"));
        collection.document(username).set(AccountData);
    }

}
