package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.ArrayList;


/**
 * This class displays statistics for a particular QR code that has been scanned at least once
 */
public class QRCodeStatsActivity extends HamburgerMenu {
    private FirebaseFirestore db;
    TextView nameView;
    TextView scoreView;
    TextView coordinatesView;
    String name;
    String score;
    String lat;
    TextView date;
    TextView scanned;
    Button gotoComments;
    String hash;
    Button scanbtn;
    String coordinates;
    ArrayList<Player> playerList;
    private QRCodeStatsAdapter qrCodeStatsAdapter;
    String username;
    String CurrentUser;
    ListView datalist;
    ImageView visualRepresentation;
    Button viewSurroundings;
    double latitude = 0;
    double longitude = 0;
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState a bundle required to create the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("QR STATS");
        setContentView(R.layout.activity_qr_code_stats_screen);
        Intent intent = getIntent();
        hash = intent.getStringExtra("Hash");
        username = intent.getStringExtra("username");
        Log.d("user",username);
        CurrentUser = intent.getStringExtra("currentUser");
        boolean res = intent.getStringExtra("user")==null;
        lat = intent.getStringExtra("lat");
        name = intent.getStringExtra("name");
        score = intent.getStringExtra("score");
        // Accesses all of the text fields
        nameView = findViewById(R.id.qr_code_stats_code_name);
        scoreView = findViewById(R.id.qr_code_stats_code_score);
        coordinatesView = findViewById(R.id.qr_code_stats_code_coordinates);
        date = findViewById(R.id.qr_code_stats_code_last_scanned_date);
        scanned = findViewById(R.id.qr_code_stats_code_total_scans);
        gotoComments = findViewById(R.id.qr_code_stats_comment_list_button);
        viewSurroundings = findViewById(R.id.qr_code_stats_comments_view_surroundings);
        datalist = findViewById(R.id.qr_code_stats_scanned_by_list);
        scanbtn = findViewById(R.id.qr_code_stats_comment_like_button);
        // Setting up listview
        playerList = new ArrayList<>();
        qrCodeStatsAdapter = new QRCodeStatsAdapter(this, playerList, username);
        datalist.setAdapter(qrCodeStatsAdapter);
        QRCodesCollection qr_codes = new QRCodesCollection();
        viewSurroundings = findViewById(R.id.qr_code_stats_comments_view_surroundings);
        // generating and displaying visual representation
        ImageView visualRepresentation= findViewById(R.id.qr_code_stats_visual_representation_view);

        Glide.with(this)
                .load("https://api.dicebear.com/6.x/bottts/png?seed="+hash)
                .into(visualRepresentation);

//        SvgLoader.pluck()
//                .with(this)
//                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
//                .load("https://api.dicebear.com/6.x/pixel-art/svg", visualRepresentation);
        //"https://api.dicebear.com/6.x/pixel-art/svg"
        //visualRepresentation = new DrawRepresentation(hash, 80);

        //representationView.setForeground(visualRepresentation);

        // handles functionality for going to QR code info comment view
        gotoComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeStatsActivity.this,
                        QRCodeStatsCommentsActivity.class);
                intent.putExtra("Hash", hash);
                intent.putExtra("username", username);
                intent.putExtra("currentUser",CurrentUser);
                intent.putExtra("latitude",String.valueOf(latitude));
                intent.putExtra("longitude",String.valueOf(longitude));
                startActivity(intent);
            }
        });

        setexceptStats();
        setStats(hash);



            scanbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!coordinates.equals("No Location")) {


                        Intent intent = new Intent(QRCodeStatsActivity.this,
                                ExploreScreenActivity.class);
                        intent.putExtra("latitude", String.valueOf(latitude));
                        intent.putExtra("longitude", String.valueOf(longitude));
                        intent.putExtra("currentUser",CurrentUser);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    }else{
                        Toast.makeText(QRCodeStatsActivity.this,"Code Has No Location",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });



        db = FirebaseFirestore.getInstance();
        db.collection("PlayerScans").get()
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
                                  Log.d("TAG",  document.getData().keySet()+"   "+hash);
                                  if(document.getData().containsKey(hash)){
                                      setList(document.getId());}

                              }
                          }
                      }
                });

        viewSurroundings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QRCodeStatsActivity.this,
                        SurroundingsActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("hash", hash);
                intent.putExtra("currentUser",CurrentUser);
                startActivity(intent);
            }
        });
    }

    /**
     * Updates all text fields about all QR code stats
     * @param hash DocumentSnapshot object containing all needed information
     */
    public void setStats(String hash) {
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
                                // Adding the required statistics to the text fields
                                if (document != null) {
                                    if(document.getId().equals(hash)){
                                            nameView.setText(document.getString("Name"));
                                            scoreView.setText(document.getString("Score"));
                                        if((Double.valueOf(document.
                                                getString("Latitude"))==200)){
                                            coordinates = "No Location";
                                        }else{
                                            latitude = Double.parseDouble(
                                                    document.getString("Latitude"));
                                            longitude = Double.parseDouble(
                                                    document.getString("Longitude"));
                                            coordinates = latitude + ", " + longitude;
                                        }
                                        coordinatesView.setText(coordinates);
                                        date.setText(document.getString("LastScanned"));
                                        scanned.setText(document.getString("TimesScanned"));
                                    }
        }
    }}}});
    }

    /**
     * This method 
     */
    public void setexceptStats(){

        nameView.setText(name);
        scoreView.setText(score);
        if (lat != null){
            if((Double.valueOf(lat)==200)){
                coordinates = "No Location";
            }else{
                GpsTracker gpsTracker = new GpsTracker(QRCodeStatsActivity.this);
                if(gpsTracker.canGetLocation()){
                    latitude = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();

                }else{
                    gpsTracker.showSettingsAlert();
                }
                coordinates = latitude + ", " + longitude;
            }
        }
        coordinatesView.setText(coordinates);
        date.setText(Utilities.getCurrentDate());
        scanned.setText("1");
    }

    /**
     * sets and updates relevant listview for given QR code
     * @param username relevant QR code's identifying username
     */
    public void setList(String username){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get()
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
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if(document.getId().equals(username)){
                                    int totalScore = Integer.parseInt(document.getString("Total Score"));
                                    int totalScans = Integer.parseInt(document.getString("Total Scans"));
                                    int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                                    int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                                    int rank = Integer.parseInt(document.getString("Rank"));
                                    playerList.add(new Player(document.getId(), totalScans,
                                            totalScore, highestScoringQR, lowestScoringQR, rank));
                                }
                            }
                            qrCodeStatsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}
