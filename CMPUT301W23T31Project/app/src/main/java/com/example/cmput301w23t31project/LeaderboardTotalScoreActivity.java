package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A class that displays a leaderboard of all players in the playerbase
 */
public class LeaderboardTotalScoreActivity extends HamburgerMenu implements SearchUserFragment.SearchUserDialogListener{
    private FirebaseFirestore db;

    Button highScoreBtn;
    Button countBtn;
    Button totalScoreBtn;
    Button regionalBtn;
    private String username;

    private ArrayList<Player> dataList = new ArrayList<>();
    private ArrayList<Player> dataList2 = new ArrayList<>();
    private LeaderboardTotalScoreArrayAdapter leaderboardTotalScoreArrayAdapter;

    public ArrayList<Player> getDataList() {
        return dataList;
    }


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
        for(int i=0;i<l;i++) {
            if (username.trim().equalsIgnoreCase(dataList.get(i).getUsername())) {
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardTotalScoreArrayAdapter = new LeaderboardTotalScoreArrayAdapter(this, dataList2,username);
                LeaderboardList.setAdapter(leaderboardTotalScoreArrayAdapter);
                c += 1;
            }
            else if(((dataList.get(i).getUsername()).toLowerCase()).startsWith(username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardTotalScoreArrayAdapter = new LeaderboardTotalScoreArrayAdapter(this, dataList2,username);
                LeaderboardList.setAdapter(leaderboardTotalScoreArrayAdapter);
                c += 1;
            }
            else if(((dataList.get(i).getUsername()).toLowerCase()).contains(username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardTotalScoreArrayAdapter = new LeaderboardTotalScoreArrayAdapter(this, dataList2,username);
                LeaderboardList.setAdapter(leaderboardTotalScoreArrayAdapter);
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
        setContentView(R.layout.activity_leaderboard_total_score);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        highScoreBtn = findViewById(R.id.leaderboard_by_high_score_button);
        countBtn = findViewById(R.id.leaderboard_by_count_button);
        totalScoreBtn = findViewById(R.id.leaderboard_by_total_score_button);
        regionalBtn = findViewById(R.id.leaderboard_by_regional_button);
        LeaderboardList = findViewById(R.id.leaderboard_list);
        leaderboardTotalScoreArrayAdapter = new LeaderboardTotalScoreArrayAdapter(this, dataList,username);
        LeaderboardList.setAdapter(leaderboardTotalScoreArrayAdapter);
        PlayerInfoCollection playerScansCollection = new PlayerInfoCollection();
        playerScansCollection.getPlayerScans();
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
     from the playerinfo collection in the database access the username and fields and
     display the users in a listview
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
                        int totalScore = Integer.parseInt(document.getString("Total Score"));
                        int totalScans = Integer.parseInt(document.getString("Total Scans"));
                        int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                        int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                        int rank = Integer.parseInt(document.getString("Rank"));
                        dataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));
                        i++;
                    }
                    leaderboardTotalScoreArrayAdapter.notifyDataSetChanged();
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
            String TotalRank = String.valueOf(rank);
            //PlayerScansCollection playerScansCollection = new PlayerScansCollection();
            //playerScansCollection.addTotalScoreRank(username,TotalRank);
        }
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
        //clickSort(name);
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
        //clickSort(name);
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
        //clickSort(name);
        Intent intent = new Intent(this, LeaderboardTotalScoreActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
    public void onClickRegional(View view){
        String name = regionalBtn.getText().toString();
        //clickSort(name);
        //Intent intent = new Intent(this, LeaderboardActivity.class);
        //intent.putExtra("username", username);
        //startActivity(intent);
    }
    public void sortList() {
        for (int i = 0; i < dataList.size() - 1; i++)
            for (int j = 0; j < dataList.size() - i - 1; j++)
                if (dataList.get(j).getTotalScore() < dataList.get(j + 1).getTotalScore()) {
                    Player temp = dataList.get(j);
                    dataList.set(j, dataList.get(j + 1));
                    dataList.set(j + 1, temp);

                }
        leaderboardTotalScoreArrayAdapter.notifyDataSetChanged();
    }
}


