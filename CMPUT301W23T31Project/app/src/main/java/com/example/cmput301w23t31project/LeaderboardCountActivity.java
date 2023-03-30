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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * A class that displays a leaderboard of all players in the playerbase
 */

public class LeaderboardCountActivity extends HamburgerMenu implements SearchUserFragment.SearchUserDialogListener{
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
    TextView StatisticText;
    private String username;
    private ArrayList<Player> dataList;
    private LeaderboardCountArrayAdapter leaderboardCountArrayAdapter;
    private LeaderboardTotalScoreArrayAdapter leaderboardTotalScoreArrayAdapter;
    private LeaderboardHighScoreArrayAdapter leaderboardHighScoreArrayAdapter;
    private ArrayList<Player> dataList2 = new ArrayList<>();
    boolean leaderboardListVisible;
    boolean leaderboardTotalScoreListVisible;
    boolean leaderboardHighScoreListVisible;
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
            if(username.trim().equalsIgnoreCase(dataList.get(i).getUsername())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardCountArrayAdapter = new LeaderboardCountArrayAdapter(this, dataList2,username);
                LeaderboardList.setAdapter(leaderboardCountArrayAdapter);
                c+=1;
            }
            else if(((dataList.get(i).getUsername()).toLowerCase()).startsWith(username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardCountArrayAdapter = new LeaderboardCountArrayAdapter(this, dataList2,username);
                LeaderboardList.setAdapter(leaderboardCountArrayAdapter);
                c += 1;
            }
            else if(((dataList.get(i).getUsername()).toLowerCase()).contains(username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardCountArrayAdapter = new LeaderboardCountArrayAdapter(this, dataList2,username);
                LeaderboardList.setAdapter(leaderboardCountArrayAdapter);
                c += 1;
            }
        }
        if(c==0)
        {
            new UsernameNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
        }
    }

    ListView LeaderboardList;
    ListView LeaderboardTotalScoreList;
    ListView LeaderboardHighScoreList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("LEADERBOARD");
        setContentView(R.layout.activity_leaderboard_count);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        dataList = new ArrayList<>();
        highScoreBtn = findViewById(R.id.leaderboard_by_high_score_button);
        countBtn = findViewById(R.id.leaderboard_by_count_button);
        totalScoreBtn = findViewById(R.id.leaderboard_by_total_score_button);
        regionalBtn = findViewById(R.id.leaderboard_by_regional_button);
        LeaderboardList = findViewById(R.id.leaderboard_count_list);
        LeaderboardHighScoreList = findViewById(R.id.leaderboard_high_score_list);
        LeaderboardTotalScoreList = findViewById(R.id.leaderboard_total_score_list);

        leaderboardTotalScoreArrayAdapter = new LeaderboardTotalScoreArrayAdapter(this, dataList,username);
        LeaderboardTotalScoreList.setAdapter(leaderboardTotalScoreArrayAdapter);

        leaderboardHighScoreArrayAdapter = new LeaderboardHighScoreArrayAdapter(this,dataList,username);
        LeaderboardHighScoreList.setAdapter(leaderboardHighScoreArrayAdapter);

        leaderboardCountArrayAdapter = new LeaderboardCountArrayAdapter(this, dataList,username);
        LeaderboardList.setAdapter(leaderboardCountArrayAdapter);

        countBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
        high_score_text = findViewById(R.id.current_high_score);
        total_score_text = findViewById(R.id.current_total_score);
        count_text = findViewById(R.id.current_count);
        StatisticText = findViewById(R.id.stat);
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
                    leaderboardCountArrayAdapter.notifyDataSetChanged();
                    sortByHighScoreList();
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
        LeaderboardList.setVisibility(View.GONE);
        LeaderboardTotalScoreList.setVisibility(View.GONE);
        LeaderboardHighScoreList.setVisibility(View.VISIBLE);
        sortByHighScoreList();
        giveRank();
        StatisticText.setText("|  High Score");
    }

    /**
     * This method allows user to shift to LeaderboardCountActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickCount(View view){
        LeaderboardList.setVisibility(View.VISIBLE);
        LeaderboardTotalScoreList.setVisibility(View.GONE);
        LeaderboardHighScoreList.setVisibility(View.GONE);
        sortList();
        giveRank();
        StatisticText.setText("|  Count");
    }

    /**
     * This method allows user to shift to LeaderboardTotalActivity
     * @param view
     *      A view needed to change intents
     */
    public void onClickTotalScore(View view){
        LeaderboardList.setVisibility(View.GONE);
        LeaderboardTotalScoreList.setVisibility(View.VISIBLE);
        LeaderboardHighScoreList.setVisibility(View.GONE);
        sortByTotalScoreList();
        giveRank();
        StatisticText.setText("|  Total Score");
    }

    public void sortList() {
        for (int i = 0; i < dataList.size() - 1; i++)
            for (int j = 0; j < dataList.size() - i - 1; j++)
                if (dataList.get(j).getCount() < dataList.get(j + 1).getCount()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);


                }
        leaderboardCountArrayAdapter.notifyDataSetChanged();
    }
    public void sortByTotalScoreList() {
        for (int i = 0; i < dataList.size() - 1; i++)
            for (int j = 0; j < dataList.size() - i - 1; j++)
                if (dataList.get(j).getTotalScore() < dataList.get(j + 1).getTotalScore()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);

                }
        leaderboardTotalScoreArrayAdapter.notifyDataSetChanged();
    }
    public void sortByHighScoreList() {
        for (int i = 0; i < dataList.size() - 1; i++)
            for (int j = 0; j < dataList.size() - i - 1; j++)
                if (dataList.get(j).getHighestScoringQR() < dataList.get(j + 1).getHighestScoringQR()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);

                }
        leaderboardHighScoreArrayAdapter.notifyDataSetChanged();
    }




}