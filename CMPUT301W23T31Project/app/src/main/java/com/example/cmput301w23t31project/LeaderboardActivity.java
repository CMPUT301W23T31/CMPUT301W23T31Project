package com.example.cmput301w23t31project;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * A class that displays a leaderboard of all players in the playerbase
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
    TextView stat_text;
    private String username;
    private String state;
    private ArrayList<Player> dataList;
    private LeaderboardArrayAdapter leaderboardArrayAdapter;
    private ArrayList<Player> dataList2 = new ArrayList<>();

    /**
     * This method gets the search results and displays the results, if there are any
     * @param username
     *      The searched username
     */
    @Override
    public void searchUser(String username){
        int l = dataList.size();
        int c = 0;
        dataList2 = new ArrayList<>();
        for(int i=0;i<l;i++)
        {
            if (dataList.get(i).getUsername().toLowerCase().contains(username.trim().toLowerCase())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList2,username, state);
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
        setContentView(R.layout.activity_leaderboard_count);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        username = intent.getStringExtra("username");
        dataList = new ArrayList<>();
        highScoreBtn = findViewById(R.id.leaderboard_by_high_score_button);
        countBtn = findViewById(R.id.leaderboard_by_count_button);
        totalScoreBtn = findViewById(R.id.leaderboard_by_total_score_button);
        regionalBtn = findViewById(R.id.leaderboard_by_regional_button);
        LeaderboardList = findViewById(R.id.leaderboard_count_list);
        stat_text = findViewById(R.id.stat_text);
        leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList,username, state);
        LeaderboardList.setAdapter(leaderboardArrayAdapter);
        if(state.equals("COUNT")) {
            stat_text.setText(R.string.stat_count);
        } else if(state.equals("HIGHSCORE")) {
            stat_text.setText(R.string.stat_high);
        } else if(state.equals("TOTALSCORE")) {
            stat_text.setText(R.string.stat_total);
        }
        countBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
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

    public void setStats(){
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
                        if(document.getId().equals(username)){
                            total_score = document.getString("Total Score");
                            high_score = document.getString("Highest Scoring QR Code");
                            count = document.getString("Total Scans");
                            high_score_text.setText(high_score);
                            total_score_text.setText(total_score);
                            count_text.setText(count);
                        }

                    }

                }}});

    }
    /**
     from the playerinfo collection in the database access the username and fields and display the users in a listview
     then sort the list and give each player a rank
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
                        Log.i("TAG", userName);
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
                    sortList();
                    giveRank();
                }}});

    }
    public void giveRank(){
                    for(int i = 0;i < dataList.size();i++){
                        int rank;
                        rank = 1+i;
                        dataList.get(i).setRank(rank);
                        String username = dataList.get(i).getUsername();
                        String CountRank = String.valueOf(rank);
                        //PlayerScansCollection playerScansCollection = new PlayerScansCollection();
                        //playerScansCollection.addCountScoreRank(username,CountRank);
                        }
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
     * This method allows user to shift to LeaderboardHighScoreActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickHighScore(View view){
        state = "HIGHSCORE";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("state", "HIGHSCORE");
        startActivity(intent);
    }

    /**
     * This method allows user to shift to LeaderboardCountActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickCount(View view){
        state="COUNT";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("state", "COUNT");
        startActivity(intent);
    }

    /**
     * This method allows user to shift to LeaderboardTotalActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickTotalScore(View view){
        state="TOTALSCORE";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("state", "TOTALSCORE");
        startActivity(intent);
    }

    public void sortList() {
        for (int i = 0; i < dataList.size() - 1; i++)
            for (int j = 0; j < dataList.size() - i - 1; j++)
                if (state.equals("COUNT")&&dataList.get(j).getCount() < dataList.get(j + 1).getCount()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);
                } else if (state.equals("HIGHSCORE")&& dataList.get(j).getHighestScoringQR() < dataList.get(j + 1).getHighestScoringQR()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);
                } else if (state.equals("TOTALSCORE")&&dataList.get(j).getTotalScore() < dataList.get(j + 1).getTotalScore()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);
                }
        leaderboardArrayAdapter.notifyDataSetChanged();
    }
}