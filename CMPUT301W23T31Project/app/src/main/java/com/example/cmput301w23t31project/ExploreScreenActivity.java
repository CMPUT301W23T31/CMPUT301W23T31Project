package com.example.cmput301w23t31project;

//Reference: https://developers.google.com/maps/documentation/android-sdk/map#view_the_code

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.List;


//https://www.geeksforgeeks.org/how-to-add-dynamic-markers-in-google-maps-with-firebase-firstore/?ref=lbp

/**
 * Handles the Explore Screen (with map view, allowing players to view local QR codes nearby)
 */
public class ExploreScreenActivity extends HamburgerMenu
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback  {

    private GpsTracker gpsTracker;
    private FirebaseFirestore db;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_screen);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    /**
     * Gets a handle to the GoogleMap object and display marker.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()

                .position(new LatLng(35.252491,-77.569633))
                .title("University of Alberta"));
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        db = FirebaseFirestore.getInstance();
        db.collection("QRCodes").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // if the snapshot is not empty we are
                            // hiding our progress bar and adding
                            // our data in a list.
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot document : list) {
                                Log.d("TAG",document.getString("Latitude"));
                                if((Double.valueOf(document.getString("Latitude"))==200)){
                                    String coordinates = "No Location";
                                }else{

                                    LatLng location = new LatLng(Double.valueOf(document.getString("Latitude")),Double.valueOf(document.getString("Longitude")));
                                    googleMap.addMarker(new MarkerOptions().position(location).title(document.getString("Name")));
                                }
                            }
                        }
                    }
                });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                QRCodesCollection qr_codes = new QRCodesCollection();
                String codeName = marker.getTitle();
                QRCodesCollection QR_codes = new QRCodesCollection();
                CollectionReference codes = QR_codes.getReference();
                codes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document: task.getResult()) {
                                if (document.getString("Name").equals(codeName)) {
                                    String hash_return =  document.getId();
                                    Intent intent = new Intent(ExploreScreenActivity.this, QRCodeStatsActivity.class);
                                    intent.putExtra("Hash", hash_return);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);

    }


    /**
     * Toasts current location when clicked
     * @param location current location to toast
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG)
                .show();
    }

    /**
     * Toasts to respond to MyLocation button being pressed
     * @return true/false to indicate choice (see below)
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT)
                .show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public int[] getScreenDimensions() {
        return new int[]{getResources().getDisplayMetrics().heightPixels, getResources().getDisplayMetrics().widthPixels};
    }
}