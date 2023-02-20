package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class QRTests {

    @Test
    public void testHash() {
        String contents = "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6";
        assertEquals(111, Utilities.getQRScore(contents));
        contents = "696ce4dbd7bb57cbfe58b64f530000f428b74999cb37e2ee60980490cd9552de3";
        assertEquals(8111, Utilities.getQRScore(contents));
        contents = "696ce4dbd7ba57cbfe58b64f530bc7f428b74984cb37e2e960980490cd9512de3";
        assertEquals(0, Utilities.getQRScore(contents));
    }

    @Test
    public void testHashLength() throws NoSuchAlgorithmException {
        String contents = "He went to the store to grab some eggs and bacon";
        int expectedLength = 64;
        assertEquals(expectedLength, Utilities.hashQRCode(contents).length());
        contents = "I can't believe it";
        assertEquals(expectedLength, Utilities.hashQRCode(contents).length());
        contents = "";
        assertEquals(expectedLength, Utilities.hashQRCode(contents).length());
        contents = "He went to the store to grab some eggs and bacon" +
                "He went to the store to grab some eggs and bacon" +
                "He went to the store to grab some eggs and bacon" +
                "He went to the store to grab some eggs and bacon" +
                "He went to the store to grab some eggs and bacon" +
                "He went to the store to grab some eggs and bacon";
        assertEquals(expectedLength, Utilities.hashQRCode(contents).length());
    }

}
