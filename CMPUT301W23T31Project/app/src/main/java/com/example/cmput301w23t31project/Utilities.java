package com.example.cmput301w23t31project;
// The Utilities class contains methods needed throughout the entire program that
// need are used to assist in more important tasks

// References
// 1. https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
// 2. https://www.baeldung.com/sha-256-hashing-java
// 3. https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
// 4. https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android/35827955#35827955
// 5. https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
// 6. https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
// 4, 5, 6 used for profile pictures
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.grpc.okhttp.internal.Util;


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
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * This function assigns a unique name to a QR code based on it's hash
     * @param hash
     *      The hash of the given QR code
     * @param adjectives
     *      The list of adjectives to choose from for the QR code name
     * @param colors
     *      The list of colors to choose from for the QR code name
     * @param nouns
     *      The list of nouns to choose from for the QR code name
     * @return
     *      A name generated from the QR code's hash
     */
    public static String getQRCodeName(String hash, String[] adjectives, String[] colors, String[] nouns) {
        String name = "";
        int groupingSize = 16;
        String[][] words = {adjectives, colors, nouns};
        int tempScore;

        for (int i = 0; i < 3; i++) {
            if (i > 0) {name = name.concat(" ");}
            // adjective
            tempScore = 0;
            for (int j = 0; j < groupingSize; j++) {
                tempScore += hash.charAt(j + i * groupingSize) * 2 ^ j;
            }
            tempScore %= words[i].length;
            name = name.concat(words[i][tempScore]);
        }
        return name;
    }

    /**
     * This function retrieves data from a file for MainActivity
     * @param resources
     *      References the resources in AndroidStudio project
     * @param length
     *      Length of the data file/max # of elements that can be read
     * @return
     *      A string array of file elements, at most 'length' of them
     */
    public static String[] retrieveFileData(Resources resources, int length, int file) {
        String data;
        String[] fileData = new String[length];
        InputStream is = resources.openRawResource(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        int count = 0;
        if (is != null) {
            try {
                while ((data = reader.readLine()) != null) {
                    fileData[count] = data;
                    count++;
                }
                is.close();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return fileData;
    }

    /**
     * This function returns the ID of the android device
     * @param app
     *      References the activity that needs to access the device ID
     * @return
     *      The ID of the android device
     */
    public static String getDeviceId(AppCompatActivity app) {
        return Settings.Secure.getString(app.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static String saveToInternalStorage(Bitmap bitmapImage, Context context, String img_path){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,img_path);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

}
