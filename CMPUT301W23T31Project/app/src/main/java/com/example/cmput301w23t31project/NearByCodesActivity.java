package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
//https://stackoverflow.com/questions/3067530/how-can-i-get-minimum-and-maximum-latitude-and-longitude-using-current-location


/**
 * This class represents an activity to view nearby QR codes within 20km
 */
public class NearByCodesActivity extends HamburgerMenu implements
        SearchScanFragment.OnFragmentInteractionListener{


    ListView qrcodeList;
    ArrayAdapter<QRCode> qrCodeAdapter;
    ArrayList<QRCode> datalist, datalist2;
    String username;
    String currentUser;
    NearByCodesFunctions near = new NearByCodesFunctions();

    /**
     * This function is invoked when the user tries to make a search
     * @param name
     *      The search value
     */
    @Override
    public void onDisplayOkPressed(String name) {
        int c =0;
        int l = datalist.size();
        datalist2 = new ArrayList<>();
        if(name.trim().isEmpty()){
            new UsernameNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
            c+=1;
        }
        for(int i=0;i<l;i++) {
            if((datalist.get(i).getName().toLowerCase()).contains(name.toLowerCase().trim())){
                datalist2.add(datalist.get(i));
                qrcodeList = findViewById(R.id.leaderboard_list);
                qrCodeAdapter = new NearbyScansArrayAdapter(this, datalist2,
                        "nearby",username);
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

        username = intent.getStringExtra("Username");
        currentUser = intent.getStringExtra("currentUser");
        qrcodeList = findViewById(R.id.leaderboard_list);

        // setting up listview of scans
        datalist = new ArrayList<>();
        qrCodeAdapter = new NearbyScansArrayAdapter(this, datalist,
                "nearby",username);
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

    /**
     * This method finds nearby QR codes
     * @param QRCodes
     *      Collection of QR codes
     */
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
        if (username.equals("NewTestName")) {
            crntLatitude = 0;
            crntLongitude = 0;
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
                    double dist = NearByCodesFunctions.distanceToCode(foundLat, foundLng,
                            finalCrntLatitude, finalCrntLongitude);
                    Log.d("distances in km: ", " "+dist+" "+doc.getString("Name"));
                    if ((minLatitude<= foundLat && foundLat <= maxLatitude)&&(minLongitude<=
                            foundLng && foundLng <= maxLongitude)) {
                        Log.d("Codes nearby:",doc.getString("Name"));
                        datalist.add(new QRCode(doc.getString("Name"),
                                Integer.parseInt(doc.getString("Score")), doc.getId(),dist));
                    }
                }
                near.sortList(datalist);
                qrCodeAdapter.notifyDataSetChanged();
            }
        });

    }
}
