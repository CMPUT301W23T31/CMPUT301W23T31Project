package com.example.cmput301w23t31project;


//Reference: https://developers.google.com/maps/documentation/android-sdk/map#view_the_code
//https://www.geeksforgeeks.org/how-to-add-dynamic-markers-in-google-maps-with-firebase-firstore/?ref=lbp


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import java.util.Map;
import java.util.Objects;


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
    double latitude;
    double longitude;
    Button nearbyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("Explore");
        setContentView(R.layout.activity_explore_screen);

        nearbyBtn = findViewById(R.id.nearby_button);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        nearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreScreenActivity.this,
                        NearByCodesActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("crnt_username", username);
                startActivity(intent);
            }
        });
    }

    /**
     * Gets a handle to the GoogleMap object and display marker.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.
                ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        googleMap.setOnMyLocationButtonClickListener(this);
        googleMap.setOnMyLocationClickListener(this);

        QRCodesCollection qrCodes = new QRCodesCollection();
        qrCodes.getReference().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // after getting the data we are calling on success method
                        // and inside this method we are checking if the received
                        // query snapshot is empty or not.
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                                Log.d("TAG",(document.getString("Latitude")).
                                        getClass().toString());
                                if((Double.parseDouble(Objects.requireNonNull(document.
                                        getString("Latitude")))==200)){
                                    String coordinates = "No Location";
                                }else{
                                    int QRScore = Integer.parseInt(document.getString("Score"));
                                    float markerColor = getMarkerColor(QRScore);
                                    LatLng location = new LatLng(Double.parseDouble(document.
                                            getString("Latitude")), Double.parseDouble(
                                                    document.getString("Longitude")));
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(document.getString("Name"))
                                            .alpha(0.7f)  // transparency (for layering)
                                            .icon(BitmapDescriptorFactory.defaultMarker(markerColor)));  // color in hsv (h only)
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
                                    Intent intent = new Intent(ExploreScreenActivity.
                                            this, QRCodeStatsActivity.class);
                                    Log.i("TAG", hash_return+"  "+username);
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

        //getting current location
        gpsTracker = new GpsTracker(ExploreScreenActivity.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Toast.makeText(this, "l"+latitude+longitude, Toast.LENGTH_SHORT)
                    .show();
        }else{
            gpsTracker.showSettingsAlert();
        }
        if (!username.equals("NewTestName")) {
            handleNewLocation(latitude, longitude, googleMap);
        }


    }

    private void handleNewLocation(Double currentLatitude, Double currentLongitude, GoogleMap googleMap) {


        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        googleMap.addMarker(options);
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 12.0f));
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

    /**
     * This method obtains the dimensions of the user's phone screen
     * @return
     *      The dimensions of the screen of the user's phone
     */
    public int[] getScreenDimensions() {
        return new int[]{getResources().getDisplayMetrics().heightPixels,
                getResources().getDisplayMetrics().widthPixels};
    }

    public float getMarkerColor(int QRScore) {
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