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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;


/**
 * A class that displays a leaderboard of all players in the playerbase
 */

public class LeaderboardActivity extends HamburgerMenu implements
        SearchUserFragment.SearchUserDialogListener{
    private FirebaseFirestore db;

    Button highScoreBtn;
    Button countBtn;
    Button totalScoreBtn;
    Button TopCodesBtn;
    String total_score;
    String high_score;
    String count;
    Button searchUser;
    TextView high_score_text;
    TextView total_score_text;
    TextView count_text;
    TextView stat_text;
    private String username;
    private String currentUser;
    private String state;
    private ArrayList<Player> dataList;
    private ArrayList<QRCode> codeList;
    private LeaderboardArrayAdapter leaderboardArrayAdapter;
    private NearbyScansArrayAdapter qrCodeArrayAdapter;
    LeaderBoardFunctions func = new LeaderBoardFunctions();
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
        if(username.trim().isEmpty()){
            new UsernameNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
            c+=1;
        }
        else {
            for (int i = 0; i < l; i++) {
                if (dataList.get(i).getUsername().toLowerCase().contains(
                        username.trim().toLowerCase())) {
                    Log.d("The username output:  ", username);
                    dataList2.add(dataList.get(i));
                    LeaderboardList = findViewById(R.id.leaderboard_count_list);
                    leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList2,
                            username, state);
                    LeaderboardList.setAdapter(leaderboardArrayAdapter);
                    c += 1;
                }

            }
        }
        if(c==0)
        {
            new UsernameNotFoundFragment().show(getSupportFragmentManager(), "Error Message");
        }
    }

    ListView LeaderboardList;
    ListView CodesList;
    LinearLayout stats_layout;
    ConstraintLayout rank_description;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);

        TextView title = findViewById(R.id.myTitle);
        title.setText("LEADERBOARD");

        setContentView(R.layout.activity_leaderboard);
        Intent intent = getIntent();

        state = intent.getStringExtra("state");
        username = intent.getStringExtra("Username");
        currentUser = intent.getStringExtra("currentUser");
        dataList = new ArrayList<>();
        codeList = new ArrayList<>();

        highScoreBtn = findViewById(R.id.leaderboard_by_high_score_button);
        countBtn = findViewById(R.id.leaderboard_by_count_button);
        totalScoreBtn = findViewById(R.id.leaderboard_by_total_score_button);
        TopCodesBtn = findViewById(R.id.leaderboard_by_top_codes_button);
        LeaderboardList = findViewById(R.id.leaderboard_count_list);
        CodesList = findViewById(R.id.leaderboard_code_list);
        stats_layout = findViewById(R.id.your_stats);
        rank_description = findViewById(R.id.rank_description);
        searchUser = findViewById(R.id.leaderboard_search_user_button);
        stat_text = findViewById(R.id.stat_text);

        if(state.equals("TOPCODES")){
            searchUser.setVisibility(View.GONE);
            LeaderboardList.setVisibility(View.GONE);
            CodesList.setVisibility(View.VISIBLE);
            stats_layout.setVisibility(View.GONE);
            rank_description.setVisibility(View.GONE);
            qrCodeArrayAdapter = new NearbyScansArrayAdapter(this, codeList,
                    "leaderboard", username);
            CreateHighScores();
            func.sortCodeList(codeList);
            qrCodeArrayAdapter.notifyDataSetChanged();

        }else{
            LeaderboardList.setVisibility(View.VISIBLE);
            CodesList.setVisibility(View.GONE);
            stats_layout.setVisibility(View.VISIBLE);
            rank_description.setVisibility(View.VISIBLE);
            leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList,
                    currentUser, state);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CreateLeaderBoard();
                }
            },250);
        }
        CodesList.setAdapter(qrCodeArrayAdapter);
        LeaderboardList.setAdapter(leaderboardArrayAdapter);
        setBtnColor();

        high_score_text = findViewById(R.id.current_high_score);
        total_score_text = findViewById(R.id.current_total_score);
        count_text = findViewById(R.id.current_count);
        setStats();

        searchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchUserFragment().show(getSupportFragmentManager(),"Search Username");
            }
        });

    }
    /**
     from the QRCodes collection in the database sort the fields by score and display the codes in a listview
     */

    public void CreateHighScores(){
        db = FirebaseFirestore.getInstance();
        Query query= db.collection("QRCodes").orderBy("Score");
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    // if the snapshot is not empty we are
                    // hiding our progress bar and adding
                    // our data in a list.
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    int i = 0;
                    for (DocumentSnapshot doc : list) {
                        if(i<101) {
                            codeList.add(new QRCode(doc.getString("Name"),
                                    Integer.parseInt(doc.getString("Score")), doc.getId()));
                        }
                        i++;

                    }
                    qrCodeArrayAdapter.notifyDataSetChanged();
                    func.sortCodeList(codeList);
                    qrCodeArrayAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    /**
     from the playerinfo collection in the database access the username and fields and display the users in a listview
     then sort the list and give each player a rank
     */
    public void CreateLeaderBoard(){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get() .addOnSuccessListener(
                queryDocumentSnapshots -> {
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
                    int highestScoringQR = Integer.parseInt(
                            document.getString("Highest Scoring QR Code"));
                    int lowestScoringQR = Integer.parseInt(
                            document.getString("Lowest Scoring QR Code"));
                    int rank = Integer.parseInt(
                            document.getString("Rank"));
                    dataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,
                            lowestScoringQR,rank));
                    i++;
                }
                leaderboardArrayAdapter.notifyDataSetChanged();
                func.sortList(state, dataList);
                leaderboardArrayAdapter.notifyDataSetChanged();
                func.giveRank(dataList);
            }
        });

    }
    /**
     * This method uses the current state to determine which button to highlight/change colour
     */
    public void setBtnColor(){
        switch (state) {
            case "COUNT":
                stat_text.setText(R.string.stat_count);
                countBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
                break;
            case "HIGHSCORE":
                stat_text.setText(R.string.stat_high);
                highScoreBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
                break;
            case "TOTALSCORE":
                stat_text.setText(R.string.stat_total);
                totalScoreBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
                break;
            case "TOPCODES":
                TopCodesBtn.setBackgroundColor(getColor(R.color.activity_selected_button_color));
                break;}
    }

    /**
     * This method goes into the database and gets certain values from the user and displays them in the leaderboards
     */
    public void setStats(){
        db = FirebaseFirestore.getInstance();
        db.collection("PlayerInfo").get() .addOnSuccessListener(
                queryDocumentSnapshots -> {
            // after getting the data we are calling on success method
            // and inside this method we are checking if the received
            // query snapshot is empty or not.
            if (!queryDocumentSnapshots.isEmpty()) {
                // if the snapshot is not empty we are
                // hiding our progress bar and adding
                // our data in a list.
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot document : list) {
                    if (document.getId().equals(username)) {
                        total_score = document.getString("Total Score");
                        high_score = document.getString("Highest Scoring QR Code");
                        count = document.getString("Total Scans");
                        high_score_text.setText(high_score);
                        total_score_text.setText(total_score);
                        count_text.setText(count);

                    }

                }
            }});}


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
     * This method changes the state to high score
     * @param view
     *      A view needed to change intents
     */
    public void onClickHighScore(View view){
        state = "HIGHSCORE";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("state", "HIGHSCORE");
        startActivity(intent);
    }

    /**
     * This method changes the state to count
     * @param view
     *      A view needed to change intents
     */
    public void onClickCount(View view){
        state="COUNT";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("state", "COUNT");
        startActivity(intent);
    }

    /**
     * This method changes the state to total score
     * @param view
     *      A view needed to change intents
     */
    public void onClickTotalScore(View view){
        state="TOTALSCORE";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("state", "TOTALSCORE");
        startActivity(intent);
    }

    /**
     * This method changes the state to Top codes
     * @param view
     *      A view needed to change intents
     */
    public void onClickTopCodes(View view){
        state="TOPCODES";
        finish();
        Intent intent = new Intent(this, LeaderboardActivity.class);
        intent.putExtra("Username", username);
        intent.putExtra("currentUser", currentUser);
        intent.putExtra("state", "TOPCODES");
        startActivity(intent);
    }

}