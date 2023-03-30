package com.example.cmput301w23t31project;

import com.google.firebase.firestore.CollectionReference;

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

}
