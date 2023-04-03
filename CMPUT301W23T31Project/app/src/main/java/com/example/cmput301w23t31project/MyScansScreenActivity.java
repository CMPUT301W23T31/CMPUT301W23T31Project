package com.example.cmput301w23t31project;


import static android.content.ContentValues.TAG;
import androidx.appcompat.app.ActionBar;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Set;


/**
 * Activity class for "My Scans" Screen
 */
public class MyScansScreenActivity extends HamburgerMenu implements
        SearchScanFragment.OnFragmentInteractionListener {
    private FirebaseFirestore db;
    ListView qrcodeList;
    ArrayAdapter<QRCode> qrCodeAdapter;
    ArrayList<QRCode> datalist;
    private ArrayList<QRCode> datalist2 = new ArrayList<>();
    String username;
    String player;
    String currentUser;

    ListView QRCodeList;

    /**
     * On Create method
     * Defines functionality for buttons and retrieves relevant information to display
     * @param savedInstanceState previously saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("SCAN LIST");
        setContentView(R.layout.activity_my_scans_screen);

        Button searchScan;
        Intent intent = getIntent();

        username = intent.getStringExtra("Username");
        currentUser = intent.getStringExtra("currentUser");
        searchScan = findViewById(R.id.my_scans_search_scan_button);
        qrcodeList = findViewById(R.id.leaderboard_list);

        // setting up listview of scans
        datalist = new ArrayList<>();
        qrCodeAdapter = new QRCodeArrayAdapter(this, datalist, username,currentUser);
        qrcodeList.setAdapter(qrCodeAdapter);

        QRPlayerScans playerScans = new QRPlayerScans();
        QRCodesCollection QRCodes = new QRCodesCollection();
        setList(playerScans, QRCodes, username);

        // functionality for "search scans"
        searchScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchScanFragment().show(getSupportFragmentManager(), "Search Scan");
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
     * Handles functionality of when a scan is chosen
     * @param name name of chosen QR code
     */
    @Override
    public void onDisplayOkPressed(String name) {
        int c =0;
        int l = datalist.size();
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        currentUser = intent.getStringExtra("currentUser");
        datalist2 = new ArrayList<>();
        if(name.trim().isEmpty()){
            new QRCodeNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
            c+=1;
        }
        else {
            for (int i = 0; i < l; i++) {
                if ((datalist.get(i).getName().toLowerCase()).contains(name.toLowerCase().trim())) {
                    datalist2.add(datalist.get(i));
                    QRCodeList = findViewById(R.id.leaderboard_list);
                    qrCodeAdapter = new QRCodeArrayAdapter(this, datalist2,
                            username, currentUser);
                    QRCodeList.setAdapter(qrCodeAdapter);
                    c += 1;
                }
            }
        }

        if(c==0)
        {
            new QRCodeNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
        }
    }

    /**
     * Fills list of scans by given player when fist instantiated
     * @param playerScans QRPlayerScans object to populate
     * @param QRCodes possible QR codes to belong to player
     * @param username username of player whose scans are to be displayed
     */
    public void setList(QRPlayerScans playerScans, QRCodesCollection QRCodes, String username) {
        playerScans.getReference().get().addOnCompleteListener(task -> {
            Set<String> codes = null;
            for (QueryDocumentSnapshot doc : task.getResult()) {
                if (doc.getId().equals(username)) {
                    codes = doc.getData().keySet();
                }
            }
            if (codes != null) {
                Set<String> finalCodes = codes;
                QRCodes.getReference().get().addOnCompleteListener(task1 -> {
                    for (QueryDocumentSnapshot doc : task1.getResult()) {
                        if (finalCodes.contains(doc.getId())) {
                            Log.d(TAG, "VAL:"+doc.getString("Name") + "  " +
                                    doc.getString("Score"));
                            datalist.add(new QRCode(doc.getString("Name"),
                                    Integer.parseInt(doc.getString("Score")), doc.getId()));
                        }
                    }
                    qrCodeAdapter.notifyDataSetChanged();
                });
            }

        });
    }
}
