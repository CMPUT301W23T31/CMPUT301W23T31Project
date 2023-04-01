package com.example.cmput301w23t31project;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyDeviceID extends AppCompatActivity {
    AppCompatActivity app = this;
    static String Id = "";

    public MyDeviceID(AppCompatActivity app) {
        if (Id.equals("")) {
            Id = Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    public static String getInstance() {
        return Id;
    }

}
