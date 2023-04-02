package com.example.cmput301w23t31project;


import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;


public class MyDeviceID {
    String Id;
    public MyDeviceID(Context context) {
        Id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getInstance() {
        return Id;
    }

}
