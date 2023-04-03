package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import android.content.res.Resources;

import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

public class MarkerTest {

    @Test
    public void testMarker() {
        assertEquals(170.0f, Utilities.getMarkerColor(-102));
        assertEquals(170.0f, Utilities.getMarkerColor(0));
        assertEquals(205.0f, Utilities.getMarkerColor(197));
        assertEquals(270.0f, Utilities.getMarkerColor(1999));
        assertEquals(320.0f, Utilities.getMarkerColor(2100));
        assertEquals(320.0f, Utilities.getMarkerColor(5000));
    }
}
