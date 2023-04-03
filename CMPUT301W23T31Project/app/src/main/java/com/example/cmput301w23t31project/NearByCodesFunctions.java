package com.example.cmput301w23t31project;


import java.util.ArrayList;


/**
 * This class holds functions to help facilitate sorting of nearby QR codes
 */
public class NearByCodesFunctions {

    /**
     * This method sorts nearby QR codes by distance
     * @param datalist
     *      List of nearby QR codes
     */
    public void sortList(ArrayList<QRCode> datalist) {
        for (int i = 0; i < datalist.size() - 1; i++)
            for (int j = 0; j < datalist.size() - i - 1; j++)
                if (datalist.get(j).getDistance()> datalist.get(j + 1).getDistance()) {
                    QRCode temp = datalist.get(j);
                    datalist.set(j, datalist.get(j + 1));
                    datalist.set(j + 1, temp);
                }
    }

    /**
     * This method calculates the distance to a QR code from the phone
     * @param lat1
     *      Latitude of first point
     * @param lon1
     *      Longitude of first point
     * @param lat2
     *      Latitude of second point
     * @param lon2
     *      Longitude of second point
     * @return
     *      The distance between phone location and QR code
     */
    public static double distanceToCode(double lat1,double lon1,double lat2,double lon2){
        double p = 0.017453292519943295;    // Math.PI / 180

        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }
}
