package com.example.cmput301w23t31project;

import java.util.Collection;

public class MyAccount {
    private String username;
    private String deviceNumber;
    private Collection<QRCode> QRCodes;

    MyAccount(String u, String d){
        username = u;
        deviceNumber = d;
    }

    MyAccount() {

    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
