package com.example.cmput301w23t31project;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * The Utilities class contains methods needed throughout the entire program that
 * need are used to assist in more important tasks
 */
// References
// 1. https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
// 2. https://www.baeldung.com/sha-256-hashing-java
// 3. https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
public class Utilities {

    public static int getQRScore(String hash) {
        int stringLength = hash.length();
        int score = 0;
        for (int i = 0; i < stringLength - 2;) {
            int number = 1;
            while (hash.charAt(i) == hash.charAt(i+1)) {
                i++;
                number += 1;
            }
            if (number > 1) {
                score += Math.pow(Integer.parseInt(String.valueOf(hash.charAt(i-1)),
                        16), number-1);
            } else {
                i++;
            }
        }
        return score;
    }

    public static String hashQRCode(String contents) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashbytes = digest.digest(
                contents.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashbytes);
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getQRCodeName(String hash, Context context) throws IOException {
        String[] words = {"dog", "Cat", "pup"};
        String name = "";
        for (int i = 0; i < hash.length(); i += 8) {
            int tempScore = 0;
            for (int j = 0; j < i + 8; j++) {
                tempScore += hash.charAt(j);
            }
            tempScore %= words.length;
            name = name + words[tempScore];
        }
        return name;
    }

    public static String[] ReadFile(Context context) throws IOException {
        BufferedReader br
                = new BufferedReader(new InputStreamReader(context.getAssets().open("filename.txt")));
        String st;
        String[] contents = new String[100];
        int count = 0;
        while ((st = br.readLine()) != null) {
            contents[count] = st;
            count++;
        }
        return contents;
    }

}
