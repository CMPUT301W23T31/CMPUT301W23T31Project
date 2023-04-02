package com.example.cmput301w23t31project;

import java.util.ArrayList;

public class NearByCodesFunctions {
    public void sortList(ArrayList<QRCode> datalist) {
        for (int i = 0; i < datalist.size() - 1; i++)
            for (int j = 0; j < datalist.size() - i - 1; j++)
                if (datalist.get(j).getDistance()> datalist.get(j + 1).getDistance()) {
                    QRCode temp = datalist.get(j);
                    datalist.set(j, datalist.get(j + 1));
                    datalist.set(j + 1, temp);
                }
    }

    public static double distanceToCode(double lat1,double lon1,double lat2,double lon2){
        double p = 0.017453292519943295;    // Math.PI / 180

        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }
}
