package com.example.cmput301w23t31project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardHighScoreActivity extends AppCompatActivity  implements SearchUserFragment.SearchUserDialogListener{
    private FirebaseFirestore db;

    Button highScoreBtn;
    Button countBtn;
    Button totalScoreBtn;
    Button regionalBtn;

    private ArrayList<Player> dataList;
    private LeaderboardHighScoreArrayAdapter leaderboardHighScoreArrayAdapter;
    private ArrayList<Player> dataList2 = new ArrayList<>();

    @Override
    public void searchUser(String username){
        int l = dataList.size();
        int c = 0;
        dataList2 = new ArrayList<>();
        for(int i=0;i<l;i++)
        {
            if(username.trim().equalsIgnoreCase(dataList.get(i).getUserName())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardHighScoreArrayAdapter = new LeaderboardHighScoreArrayAdapter(this, dataList2);
                LeaderboardList.setAdapter(leaderboardHighScoreArrayAdapter);
                c+=1;
            }
            else if(((dataList.get(i).getUserName()).toLowerCase()).startsWith(username.toLowerCase().trim())){
                dataList2.add(dataList.get(i));
                LeaderboardList = findViewById(R.id.leaderboard_list);
                leaderboardHighScoreArrayAdapter = new LeaderboardHighScoreArrayAdapter(this, dataList2);
                LeaderboardList.setAdapter(leaderboardHighScoreArrayAdapter);
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
        setContentView(R.layout.activity_leaderboard_screen);
        Intent intent = getIntent();
        dataList = new ArrayList<>();
        highScoreBtn = findViewById(R.id.leaderboard_by_high_score_button);
        countBtn = findViewById(R.id.leaderboard_by_count_button);
        totalScoreBtn = findViewById(R.id.leaderboard_by_total_score_button);
        regionalBtn = findViewById(R.id.leaderboard_by_regional_button);
        LeaderboardList = findViewById(R.id.leaderboard_list);
        leaderboardHighScoreArrayAdapter = new LeaderboardHighScoreArrayAdapter(this, dataList);
        LeaderboardList.setAdapter(leaderboardHighScoreArrayAdapter);
        CreateLeaderBoard();


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
                        //Log.i("TAG", document.getId());
                        int totalScore = Integer.parseInt(document.getString("Total Score"));
                        int totalScans = Integer.parseInt(document.getString("Total Scans"));
                        int highestScoringQR = Integer.parseInt(document.getString("Highest Scoring QR Code"));
                        int lowestScoringQR = Integer.parseInt(document.getString("Lowest Scoring QR Code"));
                        int rank = Integer.parseInt(document.getString("Rank"));
                        dataList.add(i,new Player(userName,totalScans,totalScore,highestScoringQR,lowestScoringQR,rank));
                        Log.i("Size", Integer.toString(dataList.size()));

                        //Log.i("Size", Integer.toString(dataList.get(0).getTotalScore()));
                        i++;
                    }
                    leaderboardHighScoreArrayAdapter.notifyDataSetChanged();
                    sortList();
                    giveRank();
                }}});

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item2: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            /*
            case R.id.item3: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item4: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item5: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            */
            case R.id.item5: {
                Intent intent = new Intent(this, LeaderboardActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.item6: {
                Intent intent = new Intent(this, PlayerInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item8: {
                Intent intent = new Intent(this, AppInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void giveRank(){
        for(int i = 0;i < dataList.size();i++){
            int rank;
            rank = 1+i;
            dataList.get(i).setRank(rank);
        }
    }
    public void onClickHighScore(View view){
        String name = highScoreBtn.getText().toString();
        //clickSort(name);
        Intent intent = new Intent(this, LeaderboardHighScoreActivity.class);
        startActivity(intent);
    }
    public void onClickCount(View view){
        String name = countBtn.getText().toString();
        //clickSort(name);
        Intent intent = new Intent(this, LeaderboardCountActivity.class);
        startActivity(intent);
    }
    public void onClickTotalScore(View view){
        String name = totalScoreBtn.getText().toString();
        //clickSort(name);
        Intent intent = new Intent(this, LeaderboardTotalScoreActivity.class);
        startActivity(intent);
    }

    public void sortList() {
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

