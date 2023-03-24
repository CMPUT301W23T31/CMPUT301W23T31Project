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

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Set;
//https://stackoverflow.com/questions/3067530/how-can-i-get-minimum-and-maximum-latitude-and-longitude-using-current-location

public class NearByCodesActivity extends HamburgerMenu{

    ListView qrcodeList;
    ArrayAdapter<QRCode> qrCodeAdapter;
    ArrayList<QRCode> datalist;
    String username;
    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // functionality for when a QR code is chosen from list
        qrcodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NearByCodesActivity.this, QRCodeStatsActivity.class);
                intent.putExtra("Hash", datalist.get(i).getHash());
                intent.putExtra("username", username);
                startActivity(intent);
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

        QRCodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    double foundLat = Double.parseDouble(doc.getString("Latitude"));
                    double foundLng = Double.parseDouble(doc.getString("Longitude"));
                    if ((minLatitude<= foundLat && foundLat <= maxLatitude)&&(minLongitude<= foundLng && foundLng <= maxLongitude)) {
                        Log.d("Codes nearby:",doc.getString("Name"));
                        datalist.add(new QRCode(doc.getString("Name"), Integer.parseInt(doc.getString("Score")), doc.getId()));
                    }
                }
                qrCodeAdapter.notifyDataSetChanged();
            }
        });

    }
}
