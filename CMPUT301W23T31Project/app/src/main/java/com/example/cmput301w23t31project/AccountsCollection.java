package com.example.cmput301w23t31project;


import android.content.Intent;
import java.util.HashMap;


/**
 * This class handles accounts in the database in the program
 */
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
        if (intent == null) {
            AccountData.put("DeviceID", ID);
            collection.document(username).set(AccountData);
            AccountData.put("email", "JUnitEmail");
            collection.document(username).set(AccountData);
            AccountData.put("phone", "JUnitPhone");
            collection.document(username).set(AccountData);
            AccountData.put("playername", "JUnitName");
            collection.document(username).set(AccountData);
            AccountData.put("path", "JUnitPath");
        } else {
            AccountData.put("DeviceID", ID);
            collection.document(username).set(AccountData);
            AccountData.put("email", intent.getStringExtra("email"));
            collection.document(username).set(AccountData);
            AccountData.put("phone", intent.getStringExtra("phone"));
            collection.document(username).set(AccountData);
            AccountData.put("playername", intent.getStringExtra("playername"));
            collection.document(username).set(AccountData);
            AccountData.put("path", intent.getStringExtra("path"));
        }
        collection.document(username).set(AccountData);
    }
}
