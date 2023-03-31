package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Locale;


/**
 * This class displays statistics for a particular QR code that has been scanned at least once
 */
public class QRCodeStatsCommentsActivity extends AppCompatActivity implements AddCommentFragment.OnFragmentInteractionListener{
    private FirebaseFirestore db;
    TextView nameView;
    TextView scoreView;
    TextView coordinatesView;
    TextView likesView;
    TextView date;
    TextView scanned;
    String hash;
    Button add_comment;
    Button scanbtn;
    Button scanned_by;
    String coordinates;

    private ArrayList<Comment> commentList;
    private QRCodeStatsCommentsAdapter qrCodeStatsCommentsAdapter;
    ListView datalist;
    String username;
    String CurrentUser;
    String date_text;
    String latitude;
    String longitude;
    boolean hasQR;
    DrawRepresentation visualRepresentation;

    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState a bundle required to create the activity
     */
    protected void onCreate(Bundle savedInstanceState) {
        hasQR = false;
        // Get access to the database
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("QR STATS");
        setContentView(R.layout.activity_qr_code_stats_comments);
        Intent intent = getIntent();
        hash = intent.getStringExtra("Hash");
        username = intent.getStringExtra("username");
        CurrentUser = intent.getStringExtra("currentUser");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");


        // Accesses all of the text fields
        nameView = findViewById(R.id.qr_code_stats_comment_code_name);
        scoreView = findViewById(R.id.qr_code_stats_comment_code_score);
        coordinatesView = findViewById(R.id.qr_code_stats_comment_code_coordinates);
        date = findViewById(R.id.qr_code_stats_comment_code_last_scanned_date);
        scanned = findViewById(R.id.qr_code_stats_comment_code_total_scans);
        add_comment = findViewById(R.id.comments_add_button);
        scanned_by = findViewById(R.id.qr_code_stats_scanned_by_button);
        datalist = findViewById(R.id.qr_code_stats_comments_by_list);
        scanbtn = findViewById(R.id.qr_code_stats_comment_like_button);

        // Setting up Listview
        commentList = new ArrayList<>();
        qrCodeStatsCommentsAdapter = new QRCodeStatsCommentsAdapter(this, commentList);
        datalist.setAdapter(qrCodeStatsCommentsAdapter);
        QRCodesCollection qr_codes = new QRCodesCollection();

        // generating and displaying visual representation
        View representationView = findViewById(R.id.qr_code_stats_visual_representation_view);
        visualRepresentation = new DrawRepresentation(hash, 80);
        representationView.setForeground(visualRepresentation);


        //set date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date_text = df.format(c);

        setButtonVisibility();


        // handles 'add comment' button
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCommentFragment(hash).show(getSupportFragmentManager(),"Add Comment");
            }
        });

        // handles functionality for going back to stat view
        scanned_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } //TODO: WILL THIS ALWAYS GO BACK TO THE CORRECT SPOT?
        });

        ///
        //Toast.makeText(getApplicationContext(),"hashstats: "+hash,Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();
        CommentsCollection collectionReferenceAccount = new CommentsCollection();
        CommentsCollection comments = new CommentsCollection();
        QRPlayerScans playerScans = new QRPlayerScans();
        //Toast.makeText(getApplicationContext(),"hashstats: "+hash,Toast.LENGTH_SHORT).show();
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
                                if(document.getId().equals(hash)){
                                    setStats(document);}
                            }
                        }
                    }
                });
        setList(hash);

        scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!coordinates.equals("No Location")) {


                    Intent intent = new Intent(QRCodeStatsCommentsActivity.this,
                            ExploreScreenActivity.class);
                    intent.putExtra("latitude", String.valueOf(latitude));
                    intent.putExtra("longitude", String.valueOf(longitude));
                    intent.putExtra("username", username);
                    startActivity(intent);
                }else{
                    Toast.makeText(QRCodeStatsCommentsActivity.this,"Code Has No Location",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Updates all text fields about all QR code stats
     * @param document DocumentSnapshot object containing all needed information
     */
    public void setStats(DocumentSnapshot document) {

        // Add the required statistics to the text fields

        if (document != null) {
            //Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_SHORT).show();
            nameView.setText(document.getString("Name"));
            scoreView.setText(document.getString("Score"));
            if(document.getString("Latitude").equals("200")){
                coordinates = "No Location";
            }else{
                coordinates = document.getString("Latitude") + ", " +
                        document.getString("Longitude");
            }

            String likes = document.getString("Likes") + " / " +
                    document.getString("Dislikes");
            coordinatesView.setText(coordinates);
            likesView.setText(likes);
            date.setText(document.getString("LastScanned"));
            scanned.setText(document.getString("TimesScanned"));
        }
    }
    public void setButtonVisibility(){
        add_comment.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerScans").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot document : list) {
                                Log.i("user", username);
                                //Log.i("user", CurrentUser);
                                Log.i("Testing QR comment", document.getId());
                                if(document.getId().equals(CurrentUser)){
                                    if(document.getData().containsKey(hash)){
                                        hasQR = true;
                                        add_comment.setVisibility(View.VISIBLE);
                                        Log.i("Testing QR comment", document.getId());
                                        Log.i("user", CurrentUser);
                            }}}


                        }}});

    }

    /**
     * sets and updates relevant listview for given QR code
     * @param hash relevant QR code's identifying hash
     */
    public void setList(String hash){
        db = FirebaseFirestore.getInstance();
        db.collection("Comments").get()
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
                            //List<DocumentSnapshot> list = ;
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                if(document.getString("QRhash").equals(hash)) {
                                    commentList.add(new Comment(document.getString("user"), document.getString("comment"), document.getString("date"), document.getString("QRhash")));
                                }
                            }
                            qrCodeStatsCommentsAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    // Adds new comment to database w/ relevant details
    @Override
    public void onDisplayOkPressed(String comment, String hash) {
        commentList.add(new Comment(username,comment,date_text,hash));
        qrCodeStatsCommentsAdapter.notifyDataSetChanged();
        CommentsCollection collectionReferenceAccount = new CommentsCollection();
        collectionReferenceAccount.addCommentToCollection(username, comment, date_text, hash);
    }

}

