package com.example.cmput301w23t31project;

import java.util.Collection;

public class Account {
    private String username;
    private String password;
    private String email;
    private Collection<QRCode> QRCodes;

    Account(String u, String p){
        username = u;
        password = p;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
