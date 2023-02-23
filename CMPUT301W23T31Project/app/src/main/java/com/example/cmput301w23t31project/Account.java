package com.example.cmput301w23t31project;

import java.util.Collection;

public class Account {
    private String username;
    private String email;
    private Collection<QRCode> QRCodes;

    Account(String u){
        username = u;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
