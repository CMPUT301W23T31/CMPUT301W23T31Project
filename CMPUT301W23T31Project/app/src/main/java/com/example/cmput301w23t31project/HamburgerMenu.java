package com.example.cmput301w23t31project;


import android.content.Intent;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;


/**
 * This class provides a method to operate a hamburger menu on many activities
 */
public class HamburgerMenu extends AppCompatActivity {
    private GpsTracker gpsTracker;
    double latitude;
    double longitude;
    /**
     * This method allows proper functionality for the Hamburger Menu
     * @param item
     *      A reference to the menu item click on the menu
     * @param username
     *      The username of the current user
     * @return
     *      Whether or not the menu worked properly
     */
    public boolean useHamburgerMenu(MenuItem item, String username) {
        switch(item.getItemId()){
            case R.id.item2: {
                if (!this.getLocalClassName().equals("MainActivity")) {
                    finish();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                return true;
            }
            case R.id.item3: {
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setPrompt("Scan a barcode");
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(CaptureActivityPortrait.class);
                integrator.initiateScan();
                return true;
            }
            case R.id.item5: {
                if (!this.getLocalClassName().equals("LeaderboardActivity")) {
                    finish();
                    Intent intent = new Intent(this, LeaderboardActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("state", "HIGHSCORE");
                    startActivity(intent);
                }
                return true;
            }
            case R.id.item4: {
                if (!this.getLocalClassName().equals("ExploreScreenActivity")) {
                    finish();
                    gpsTracker = new GpsTracker(this);
                    if(gpsTracker.canGetLocation()){
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();
                        //Toast.makeText(this, "l"+latitude+longitude, Toast.LENGTH_SHORT)
                        //.show();
                    }else{
                        gpsTracker.showSettingsAlert();
                    }
                    Intent intent = new Intent(this, ExploreScreenActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("latitude",String.valueOf(latitude));
                    intent.putExtra("longitude",String.valueOf(longitude));

                    startActivity(intent);
                }
                return true;
            }
            case R.id.item6: {
                if (!this.getLocalClassName().equals("PlayerInfoScreenActivity")) {
                    finish();
                    Intent intent = new Intent(this, PlayerInfoScreenActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                return true;
            }
            case R.id.item7: {
                if (!this.getLocalClassName().equals("MyAccountScreenActivity")) {
                    finish();
                    Intent intent = new Intent(this, MyAccountScreenActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                return true;
            }
            case R.id.item8: {
                if (!this.getLocalClassName().equals("AppInfoScreenActivity")) {
                    finish();
                    Intent intent = new Intent(this, AppInfoScreenActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
