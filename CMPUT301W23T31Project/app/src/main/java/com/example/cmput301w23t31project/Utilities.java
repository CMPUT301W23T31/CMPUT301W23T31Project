package com.example.cmput301w23t31project;


// I decided to put some references for the program here because Utilities class is general
// and I didn't want them scattered
// References (for many functions / methods / code in many classes)
// https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
// https://stackoverflow.com/questions/4743116/get-screen-width-and-height-in-android
// https://www.javatpoint.com/java-get-current-date
// https://firebase.google.com/docs/firestore/manage-data/add-data
// https://www.geeksforgeeks.org/how-to-read-qr-code-using-zxing-library-in-android/
// https://www.baeldung.com/sha-256-hashing-java
// https://www.youtube.com/watch?v=UIIpCt2S5Ls
// https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
// https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android/35827955#35827955
// https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
// https://stackoverflow.com/questions/9107900/how-to-upload-image-from-gallery-in-android
// https://stackoverflow.com/questions/2785485/is-there-a-unique-android-device-id
// https://firebase.google.com/docs/firestore/query-data/queries
// https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown
// https://www.youtube.com/watch?v=oh4YOj9VkVE


import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


/**
 * The Utilities class contains methods needed throughout the entire program that need
 * or are used to assist in more important tasks
 */
public class Utilities {

    /**
     * This function obtains a score for a scanned QR Code
     * @param hash the hash value of a scanned QR code
     * @return the score assigned to a particular QR Code
     */
    public static int getQRScore(String hash) {
        boolean isNum = false;
        int lowBound = 0;
        int uppBound = 15000;

        int score = 0;
        int partialProduct = 1;
        int stringLength = hash.length();

        for (int i = 0; i < stringLength; i++) {
            if ((hash.charAt(i) <= '9')  ^ isNum) {
                isNum = !isNum;
                score = score + (partialProduct / 1000);
                partialProduct = 1;
            }
            partialProduct = partialProduct * Integer.parseInt("" + hash.charAt(i), 16);
        }
        // adjustments
        score = (score + lowBound) % uppBound;
        if (score > 5000) {
            score = score / 2;
        }


        return score;
    }

    /**
     * Obtains a SHA-256 hash for provided QR Code contents
     * @param contents ohe contents of the QR Code as a String of characters
     * @return the SHA-256 hash for a QR Code
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
     * Converts bytes to their corresponding hex value
     * @param hash the hash for a particular QR Code
     * @return String of hexadecimal values representing a QR Code
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
     * Assigns a unique name to a QR code based on it's hash
     * @param hash the hash of the given QR code
     * @param names list of potential words for first word slot
     * @param names2 list of potential words for second word slot
     * @param names3 list of potential words for third word slot
     * @param names4 list of potential words for fourth word slot
     * @param names5 list of potential words for fifth word slot
     * @return the name generated from the QR code's hash
     */
    public static String getQRCodeName(String hash, String[] names, String[] names2, String[] names3, String[] names4, String[] names5) {

         String name = "";
         int [ ] list = new int[5];
         int index =0;

            for(int i=0;i<hash.length();i++){
                if(i<12){
                        list[0] += (int) (hash.charAt(i));
                        list[0] %= names.length;
                    }
                else if(i>=12 && i<24){
                    list[1] +=  (int) (hash.charAt(i));
                    list[1] %= names2.length;
                }
                else if(i>=24 && i<38){
                    list[2] +=  (int) (hash.charAt(i));
                    list[2] %= names3.length;
                }
                else if(i>=38 && i<50){
                    list[3] +=  (int) (hash.charAt(i));
                    list[3] %= names4.length;
                }
                else if(i>=50 && i<64){
                    list[4] +=  (int) (hash.charAt(i));
                    list[4] %= names5.length;
                }
            }


            name = name.concat(names[list[0]]);
            name = name.concat(names2[list[1]]);
            name = name.concat(names3[list[2]]);
            name = name.concat(names4[list[3]]);
            name = name.concat(names5[list[4]]);


            for(int i=0;i<5;i++){
                Log.d("List: ",list[i]+" ");
            }
          return name;
    }

    /**
     * Retrieves data from a file for MainActivity
     * @param resources references the resources in AndroidStudio project
     * @param length length of the data file/max # of elements that can be read
     * @return a string array of file elements, at most 'length' of them
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
     * Saves the image to internal storage, in other words, saves the image to a path
     * in the users device that is called upon each time we need that image
     * @param bitmapImage the bitmap of the image selected by the user
     * @param context the context of the application at time of method call
     * @param img_file_name the name of the image file provided (something like image.png)
     * @return the absolute path in the user's device to the image for future usages
     */
    public static String saveToInternalStorage(Bitmap bitmapImage, Context context, String img_file_name){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir, which is a directory on the user's device for photos
        File mypath = new File(directory,img_file_name);

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

    /**
     * This function returns the current date in yyyy-MM-dd format
     * @return the current date in yyyy-MM-dd format
     */
    public static String getCurrentDate() {
        DateTimeFormatter dtf;
        // Update latest date scanned
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            return dtf.format(now);
        }
        return "";
    }

    public static float getMarkerColor(int QRScore) {
        float markerColor;
        if (QRScore < 20) {
            markerColor = 170.0f;
        } else if (QRScore < 200) {
            markerColor = 205.0f;
        } else if (QRScore < 2000) {
            markerColor = 270.0f;
        } else {
            markerColor = 320.0f;
        }
        return markerColor;
    }
}
