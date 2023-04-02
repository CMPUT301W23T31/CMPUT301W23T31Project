package com.example.cmput301w23t31project;

import static org.junit.jupiter.api.Assertions.*;

import android.content.res.Resources;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class NearByCodesTest {
    ArrayList<QRCode> codes = getCodes();
    NearByCodesFunctions near = new NearByCodesFunctions();

    @Test
    public void sortCodesTest() {
        near.sortList(codes);
        for (int i = 0; i < codes.size()-1; i++) {
            assert(codes.get(i).getDistance() < codes.get(i+1).getDistance());
        }
    }

    @Test
    public void getDistanceTest() {
        double latitiude = 37;
        double longitude = -111;
        assert(NearByCodesFunctions.distanceToCode(74, -2, latitiude, longitude) > 0);
        assert(NearByCodesFunctions.distanceToCode(37.4, -111.7, latitiude, longitude) <=77);
        assert(NearByCodesFunctions.distanceToCode(37, -111, latitiude, longitude) == 0);
    }

    public ArrayList<QRCode> getCodes() {
        ArrayList<QRCode> codes = new ArrayList<>();
        double latitiude = 37;
        double longitude = -111;
        codes.add(new QRCode("Name", 44, "Hash", NearByCodesFunctions.distanceToCode(74, -2, latitiude, longitude)));
        codes.add(new QRCode("Name", 55, "Hash", NearByCodesFunctions.distanceToCode(38, -90, latitiude, longitude)));
        codes.add(new QRCode("Name", 66, "Hash", NearByCodesFunctions.distanceToCode(2, -177, latitiude, longitude)));
        codes.add(new QRCode("Name", 77, "Hash", NearByCodesFunctions.distanceToCode(90, -180, latitiude, longitude)));
        return codes;
    }
}
