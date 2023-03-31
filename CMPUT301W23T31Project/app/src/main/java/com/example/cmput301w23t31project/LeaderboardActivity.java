package com.example.cmput301w23t31project;
// NOTICE:
// For the LeaderBoard...Activity classes that inherit this,
// the method functionality is very similar


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.List;


/**
 * A class that displays a leaderboard of all players in the player database
 */
public class LeaderboardActivity extends HamburgerMenu implements SearchUserFragment.SearchUserDialogListener{
    private FirebaseFirestore db;

    Button highScoreBtn;
    Button countBtn;
    Button totalScoreBtn;
    Button regionalBtn;
    String total_score;
    String high_score;
    String count;
    TextView high_score_text;
    TextView total_score_text;
    TextView count_text;
    private String username;
    private ArrayList<Player> dataList = new ArrayList<>();
    private ArrayList<Player> dataList2 = new ArrayList<>();
    private LeaderboardArrayAdapter leaderboardArrayAdapter;


    public ArrayList<Player> getDataList() {
        return dataList;
    }


    /**
     * This method gets the search results and displays the results, if there are any
     * @param search_username
     *      The searched username
     */
    @Override
    public void searchUser(String search_username){
        int l = dataList.size();
        int c = 0;
        dataList2 = new ArrayList<>();
        for(int i=0;i<l;i++) {
            if (search_username.trim().equalsIgnoreCase(dataList.get(i).getUsername())) {
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList2,search_username);
                LeaderboardList.setAdapter(leaderboardArrayAdapter);
                c += 1;
            }
            else if(((dataList.get(i).getUsername()).toLowerCase()).startsWith(search_username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList2,search_username);
                LeaderboardList.setAdapter(leaderboardArrayAdapter);
                c += 1;
            }
            else if(((dataList.get(i).getUsername()).toLowerCase()).contains(search_username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList2,search_username);
                LeaderboardList.setAdapter(leaderboardArrayAdapter);
                c += 1;
            }
        }
        if(c==0)
        {
            new UsernameNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
        }
    }

    ListView LeaderboardList;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("LEADERBOARD");
        setContentView(R.layout.activity_leaderboard_screen);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        highScoreBtn = findViewById(R.id.leaderboard_by_high_score_button);
        countBtn = findViewById(R.id.leaderboard_by_count_button);
        totalScoreBtn = findViewById(R.id.leaderboard_by_total_score_button);
        regionalBtn = findViewById(R.id.leaderboard_by_regional_button);
        LeaderboardList = findViewById(R.id.leaderboard_list);
        leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList,username);
        LeaderboardList.setAdapter(leaderboardArrayAdapter);
<<<<<<< Updated upstream
=======
        if(state.equals("COUNT")) {
            stat_text.setText(R.string.stat_count);
            countBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
        } else if(state.equals("HIGHSCORE")) {
            stat_text.setText(R.string.stat_high);
            highScoreBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
        } else if(state.equals("TOTALSCORE")) {
            totalScoreBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
            stat_text.setText(R.string.stat_total);
        }
>>>>>>> Stashed changes
        high_score_text = findViewById(R.id.current_high_score);
        total_score_text = findViewById(R.id.current_total_score);
        count_text = findViewById(R.id.current_count);
        setStats();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    CreateLeaderBoard();
                                }
                            },250);


        Button searchUser;
        searchUser = findViewById(R.id.leaderboard_search_user_button);
        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchUserFragment().show(getSupportFragmentManager(),"Search Username");
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    /**
     * from the playerinfo collection in the database access the username and
     * fields and display the users in a listview
     */
    public void CreateLeaderBoard(){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get() .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        int i = 0;
                        String userName = document.getId();
                        int totalScore = Integer.parseInt(document.getString("Total Score"));
                        int totalScans = Integer.parseInt(document.getString("Total Scans"));
                        int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                        int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                        int rank = Integer.parseInt(document.getString("Rank"));
                        dataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));
                        Log.i("Size", Integer.toString(dataList.size()));
                        i++;
                    }
                    leaderboardArrayAdapter.notifyDataSetChanged();

                }}});

    }
<<<<<<< Updated upstream
=======
    public void giveRank(){
                    for(int i = 0;i < dataList.size();i++){
                        int rank;
                        rank = 1+i;
                        dataList.get(i).setRank(rank);
                        }
    }
>>>>>>> Stashed changes

    public void setStats(){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get() .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d("Stats",username);
                // after getting the data we are calling on success method
                // and inside this method we are checking if the received
                // query snapshot is empty or not.
                if (!queryDocumentSnapshots.isEmpty()) {
                    // if the snapshot is not empty we are
                    // hiding our progress bar and adding
                    // our data in a list.
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot document : list) {
                        if(document.getId().equals(username)){
                            total_score = document.getString("Total Score");
                            high_score = document.getString("Highest Scoring QR Code");
                            count = document.getString("Total Scans");
                            high_score_text.setText(high_score);
                            total_score_text.setText(total_score);
                            count_text.setText(count);
                            Log.d("Stats",username+" "+total_score+" "+high_score+" "+count);
                        }

                    }

                }}});

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);

    }

    /**
     * This method allows user to shift to LeaderboardHighScoreActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickHighScore(View view){
        String name = highScoreBtn.getText().toString();
        Intent intent = new Intent(this, LeaderboardHighScoreActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * This method allows user to shift to LeaderboardCountActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickCount(View view){
        String name = countBtn.getText().toString();
        Intent intent = new Intent(this, LeaderboardCountActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    /**
     * This method allows user to shift to LeaderboardTotalScoreActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickTotalScore(View view){
        String name = totalScoreBtn.getText().toString();
        Intent intent = new Intent(this, LeaderboardTotalScoreActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    public void onClickRegional(View view){
        String name = regionalBtn.getText().toString();
        //intent.putExtra("username", username);

        //Intent intent = new Intent(this, LeaderboardActivity.class);
        //startActivity(intent);
    }

}

