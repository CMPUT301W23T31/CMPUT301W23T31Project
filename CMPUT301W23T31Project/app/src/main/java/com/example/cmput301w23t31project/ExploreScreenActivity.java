package com.example.cmput301w23t31project;


//Reference: https://developers.google.com/maps/documentation/android-sdk/map#view_the_code
//https://www.geeksforgeeks.org/how-to-add-dynamic-markers-in-google-maps-with-firebase-firstore/?ref=lbp


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private String currentUser;
    double latitude;
    double longitude;
    Button nearbyBtn;
    EditText mSearchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        //ImageView search = findViewById(R.id.ic_magnify);
        title.setText("Explore");
        setContentView(R.layout.activity_explore_screen);

        nearbyBtn = findViewById(R.id.nearby_button);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        latitude = Double.parseDouble(intent.getStringExtra("latitude"));
        longitude = Double.parseDouble(intent.getStringExtra("longitude"));
        currentUser = intent.getStringExtra("currentUser");
        mSearchText = findViewById(R.id.input_search);


        nearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExploreScreenActivity.this,
                        NearByCodesActivity.class);
                intent.putExtra("Username", username);
                intent.putExtra("currentUser", currentUser);
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
                                    int QRScore = Integer.parseInt(
                                            document.getString("Score"));
                                    float markerColor = Utilities.getMarkerColor(QRScore);
                                    LatLng location = new LatLng(Double.parseDouble(
                                            document.getString("Latitude")),
                                            Double.parseDouble(
                                                    document.getString("Longitude")));
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(location)
                                            .title(document.getString("Name"))
                                            .alpha(0.7f)  // transparency (for layering)
                                            .icon(BitmapDescriptorFactory.
                                                    defaultMarker(markerColor)));  // color in hsv
                                }
                            }
                        }
                });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
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
                                    intent.putExtra("Hash", hash_return);
                                    intent.putExtra("Username", username);
                                    intent.putExtra("currentUser", currentUser);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
                return false;
            }
        });
        if (!username.equals("NewTestName")) {
            handleNewLocation(latitude, longitude, googleMap);
        }
        init(googleMap);
    }

    /**
     * initializes the map to be displayed
     * @param g- googleMap that is initialized
     */
    private void init(GoogleMap g){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate(g);
                }
                return false;
            }
        });
    }

    /**
     * zooms the map into the current location upon being called
     * @param g- googleMap that is initialized
     */
    private void geoLocate(GoogleMap g){
        String searchString = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(ExploreScreenActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }
        if(list.size() > 0){
            Address address = list.get(0);
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            moveCamera(latlng, g);
        }
    }

    /**
     * zooms the map into the current location upon being called
     * @param latLng- latitute and longitude position to be zoomed into
     * @param googleMap- googleMap that is initialized
     */
    private void moveCamera(LatLng latLng, GoogleMap googleMap){
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, (float) 12.0));
    }


    /**
     * zooms the map into the current location upon being called
     * @param currentLatitude- latituteposition to be zoomed into
     * @param currentLongitude- longitude position to be zoomed into
     * @param googleMap- googleMap that is initialized
     */
    private void handleNewLocation(Double currentLatitude, Double currentLongitude,
                                   GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        googleMap.addMarker(options);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude,
                currentLongitude), 12.0f));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, currentUser);

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

}