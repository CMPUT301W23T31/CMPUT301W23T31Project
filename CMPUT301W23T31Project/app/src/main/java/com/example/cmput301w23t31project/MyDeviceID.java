package com.example.cmput301w23t31project;


import android.content.Context;
import android.provider.Settings;


/**
 * This class allows user to access the device ID using the singleton pattern
 */
public class MyDeviceID {
    String Id;
    public MyDeviceID(Context context) {
        Id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getInstance() {
        return Id;
    }

}
