package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    Button scanned_by;
    /**
     * This method creates the activity to display QR code stats
     * @param savedInstanceState
     *      A bundle required to create the activity
     */
    private ArrayList<Comment> commentList;
    private QRCodeStatsCommentsAdapter qrCodeStatsCommentsAdapter;
    ListView datalist;
    String username;
    String date_text;
    protected void onCreate(Bundle savedInstanceState) {
        // Get access to the database
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_stats_comments);
        Intent intent = getIntent();
        hash = intent.getStringExtra("Hash");
        username = intent.getStringExtra("username");
        // Accesses all of the text fields

        nameView = findViewById(R.id.qr_code_stats_comment_code_name);
        scoreView = findViewById(R.id.qr_code_stats_comment_code_score);
        coordinatesView = findViewById(R.id.qr_code_stats_comment_code_coordinates);
        likesView = findViewById(R.id.qr_code_stats_comment_code_likes_dislikes);
        date = findViewById(R.id.qr_code_stats_comment_code_last_scanned_date);
        scanned = findViewById(R.id.qr_code_stats_comment_code_total_scans);
        add_comment = findViewById(R.id.comments_add_button);
        scanned_by = findViewById(R.id.qr_code_stats_scanned_by_button);
        commentList = new ArrayList<>();
        datalist = findViewById(R.id.qr_code_stats_comments_by_list);
        qrCodeStatsCommentsAdapter = new QRCodeStatsCommentsAdapter(this, commentList);
        datalist.setAdapter(qrCodeStatsCommentsAdapter);
        QRCodesCollection qr_codes = new QRCodesCollection();

        //set date
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        date_text = df.format(c);

        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCommentFragment(hash).show(getSupportFragmentManager(),"Add Comment");
            }
        });

        scanned_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
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
    }
    //QueryDocumentSnapshot document = qr_codes.getDocument(hash);

    public void setStats(DocumentSnapshot document) {

        // Add the required statistics to the text fields

        if (document != null) {
            //Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_SHORT).show();
            nameView.setText(document.getString("Name"));
            scoreView.setText(document.getString("Score"));
            String coordinates = document.getString("Latitude") + ", " +
                    document.getString("Longitude");
            String likes = document.getString("Likes") + " / " +
                    document.getString("Dislikes");
            coordinatesView.setText(coordinates);
            likesView.setText(likes);
            date.setText(document.getString("LastScanned"));
            scanned.setText(document.getString("TimesScanned"));
        }
    }

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
                    }});}

    @Override
    public void onDisplayOkPressed(String comment, String hash) {
        commentList.add(new Comment(username,comment,date_text,hash));
        qrCodeStatsCommentsAdapter.notifyDataSetChanged();
        CommentsCollection collectionReferenceAccount = new CommentsCollection();
        collectionReferenceAccount.addCommentToCollection(username, comment, date_text, hash);
    }

}
