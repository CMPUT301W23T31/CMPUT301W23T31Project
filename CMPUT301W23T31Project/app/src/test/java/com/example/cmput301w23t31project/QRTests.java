package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class QRTests {

    @Test
    public void testHash() throws NoSuchAlgorithmException {
        String contents = "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6";
        int expected = 111;
        assertEquals(expected, Utilities.getQRScore(contents));
    }
}
