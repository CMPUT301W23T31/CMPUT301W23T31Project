package com.example.cmput301w23t31project;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Set;
//https://stackoverflow.com/questions/3067530/how-can-i-get-minimum-and-maximum-latitude-and-longitude-using-current-location


public class NearByCodesActivity extends HamburgerMenu implements SearchScanFragment.OnFragmentInteractionListener{


    ListView qrcodeList;
    ArrayAdapter<QRCode> qrCodeAdapter;
    ArrayList<QRCode> datalist, datalist2;
    String username;
    String currentUser;

    @Override
    public void onDisplayOkPressed(String name) {
        int c =0;
        int l = datalist.size();
        datalist2 = new ArrayList<>();
        for(int i=0;i<l;i++) {
            if (name.trim().equalsIgnoreCase(datalist.get(i).getName())){
                datalist2.add(datalist.get(i));
                qrcodeList = findViewById(R.id.leaderboard_list);
                qrCodeAdapter = new NearbyScansArrayAdapter(this, datalist2);
                qrcodeList.setAdapter(qrCodeAdapter);
                c+=1;
            }
            else if((datalist.get(i).getName()).startsWith(name.toLowerCase().trim())){
                datalist2.add(datalist.get(i));
                qrcodeList = findViewById(R.id.leaderboard_list);
                qrCodeAdapter = new NearbyScansArrayAdapter(this, datalist2);
                qrcodeList.setAdapter(qrCodeAdapter);
                c+=1;
            }
            else if((datalist.get(i).getName()).contains(name.toLowerCase().trim())){
                datalist2.add(datalist.get(i));
                qrcodeList = findViewById(R.id.leaderboard_list);
                qrCodeAdapter = new NearbyScansArrayAdapter(this, datalist2);
                qrcodeList.setAdapter(qrCodeAdapter);
                c+=1;
            }
        }

        if(c==0)
        {
            new QRCodeNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("Nearby Codes");
        setContentView(R.layout.activity_nearby_scans);

        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        currentUser = intent.getStringExtra("crnt_username");
        qrcodeList = findViewById(R.id.leaderboard_list);

        // setting up listview of scans
        datalist = new ArrayList<>();
        qrCodeAdapter = new NearbyScansArrayAdapter(this, datalist);
        qrcodeList.setAdapter(qrCodeAdapter);

        QRCodesCollection QRCodes = new QRCodesCollection();
        findNearbyCodes(QRCodes);

        Button searchScan;
        searchScan = findViewById(R.id.nearby_scans_search_scan_button);
        searchScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchScanFragment().show(getSupportFragmentManager(),"Search Scan");
            }
        });

    }

    /**
     * For creating the options menu
     * @param menu menu to create
     * @return boolean of whether to display or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu, menu);
        return true;
    }

    /**
     * Delegates functionality when item is chosen from menu
     * @param item item chosen from menu
     * @return boolean on whether to proceed or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return useHamburgerMenu(item, currentUser);

    }



    public void findNearbyCodes(QRCodesCollection QRCodes){
        double crntLatitude = 0;
        double crntLongitude = 0;
        GpsTracker gpsTracker = new GpsTracker(NearByCodesActivity.this);
        if(gpsTracker.canGetLocation()){
            crntLatitude = gpsTracker.getLatitude();
            crntLongitude = gpsTracker.getLongitude();
        }else{
            gpsTracker.showSettingsAlert();
        }

        double minLatitude = crntLatitude - (20/111.12);
        double maxLatitude = crntLatitude + (20/111.12);
        double minLongitude = crntLongitude - (20/111.12)*Math.cos(crntLatitude);
        double maxLongitude = crntLongitude + (20/111.12)*Math.cos(crntLatitude);

        double finalCrntLatitude = crntLatitude;
        double finalCrntLongitude = crntLongitude;
        QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    double foundLat = Double.parseDouble(doc.getString("Latitude"));
                    double foundLng = Double.parseDouble(doc.getString("Longitude"));
                    double dist = distanceToCode(foundLat, foundLng, finalCrntLatitude, finalCrntLongitude);
                    Log.d("distances in km: ", " "+dist);
                    if ((minLatitude<= foundLat && foundLat <= maxLatitude)&&(minLongitude<= foundLng && foundLng <= maxLongitude)) {
                        Log.d("Codes nearby:",doc.getString("Name"));
                        datalist.add(new QRCode(doc.getString("Name"), Integer.parseInt(doc.getString("Score")), doc.getId(),dist));
                    }
                }
                sortList();
                qrCodeAdapter.notifyDataSetChanged();
            }
        });

    }

    public void sortList() {
        for (int i = 0; i < datalist.size() - 1; i++)
            for (int j = 0; j < datalist.size() - i - 1; j++)
                if (datalist.get(j).getDistance()> datalist.get(j + 1).getDistance()) {
                    QRCode temp = datalist.get(j);
                    datalist.set(j, datalist.get(j + 1));
                    datalist.set(j + 1, temp);

                }
        qrCodeAdapter.notifyDataSetChanged();
    }

    private double distanceToCode(double lat1,double lon1,double lat2,double lon2){
        double p = 0.017453292519943295;    // Math.PI / 180

        double a = 0.5 - Math.cos((lat2 - lat1) * p)/2 +
                Math.cos(lat1 * p) * Math.cos(lat2 * p) *
                        (1 - Math.cos((lon2 - lon1) * p))/2;

        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }
}
