package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import android.content.res.Resources;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QRTests {

    @Test
    public void testHash() {
        String contents = "696ce4dbd7bb57cbfe58b64f530f428b74999cb37e2ee60980490cd9552de3a6";
        assertEquals(48, Utilities.getQRScore(contents));
        contents = "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489";
        assertEquals(2690, Utilities.getQRScore(contents));
        contents = "696ce4dbd7ba57cbfe58b64f530bc7f428b74984cb37e2e960980490cd9512de3";
        assertEquals(36, Utilities.getQRScore(contents));
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

    @Test
    public void scoringSystemTest() {
        byte[] b = {4, 8, 1, 4, 0, 3, 8, 2, 8, 9};
        assertEquals("04080104000308020809", Utilities.bytesToHex(b));
        byte[] b2 = {};
        assertEquals("", Utilities.bytesToHex(b2));
        byte[] b3 = {9, 9, 9, 9, 8, 8, 8, 8, 7, 7, 7, 7};
        assertEquals("090909090808080807070707", Utilities.bytesToHex(b3));
    }

    @Test
    public void codeNameTest() {
        String[] arr1 = {"Dogs", "Cats", "Bunnies", "Turtles", "Geese"};
        String[] arr2 = {"Warm", "Cold", "Frigid", "Boiling", "Calm"};
        String[] arr3 = {"Carrot", "Peas", "Lettuce", "Bok Choy", "Tomato"};
        String[] arr4 = {"Canadiens", "Rangers", "Bruins", "Leafs", "Hawks"};
        String[] arr5 = {"Daisy", "Tulip", "Rose", "Dandelion", "Pansy"};
        String hash = "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7292f7489";
        assertEquals("DogsFrigidTomatoLeafsRose", Utilities.getQRCodeName(hash, arr1, arr2, arr3, arr4, arr5));
        hash = "b138867051e7f22a7e1d4befdb1875beb17e28c6464afbdab7532dc7892f7489";
        assertEquals("DogsFrigidTomatoLeafsDandelion", Utilities.getQRCodeName(hash, arr1, arr2, arr3, arr4, arr5));
        hash = "0000000000000000000000000000000000000000000000000000000000000000";
        assertEquals("CatsColdLettuceRangersRose", Utilities.getQRCodeName(hash, arr1, arr2, arr3, arr4, arr5));
    }

}
