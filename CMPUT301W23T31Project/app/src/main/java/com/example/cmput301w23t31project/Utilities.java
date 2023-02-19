package com.example.cmput301w23t31project;
// The Utilities class contains methods needed throughout the entire program that
// need are used to assist in more important tasks

// References
// 1. https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
// 2. https://www.baeldung.com/sha-256-hashing-java
// 3. https://www.geeksforgeeks.org/different-ways-reading-text-file-java/


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Utilities {

    /**
     * This function obtains a score for a scanned QR Code
     * @param hash
     *      The hash value of a scanned QR code
     * @return
     *      The score assigned to a particular QR Code
     */
    public static int getQRScore(String hash) {
        int stringLength = hash.length();
        int score = 0;
        for (int i = 0; i < stringLength - 2; i++) {
            int number = 1;
            while (hash.charAt(i) == hash.charAt(i+1)) {
                i++;
                number += 1;
            }
            if (number > 1) {
                if (hash.charAt(i-1) == '0') {
                    score += Math.pow(20, number - 1);
                } else {
                    score += Math.pow(Integer.parseInt(String.
                            valueOf(hash.charAt(i - 1)), 16), number - 1);
                    i--;
                }
            }
        }
        return score;
    }

    /**
     * This function obtains a SHA-256 hash for provided QR Code contents
     * @param contents
     *      The contents of the QR Code as a String of characters
     * @return
     *      The SHA-256 hash for a QR Code
     * @throws NoSuchAlgorithmException
     *      Throws such an exception when a "SHA-256" algorithm is
     *      not found in getInstance
     */
    public static String hashQRCode(String contents) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hashBytes = digest.digest(
                contents.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }

    /**
     * This function converts bytes to their corresponding hex value
     * @param hash
     *      The hash for a particular QR Code
     * @return
     *      A string of hexadecimal values representing a QR Code
     */
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
}
