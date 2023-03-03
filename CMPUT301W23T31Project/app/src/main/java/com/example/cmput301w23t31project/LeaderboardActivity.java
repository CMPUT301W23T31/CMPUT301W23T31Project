package com.example.cmput301w23t31project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity implements SearchUserFragment.SearchUserDialogListener{

    Button highScoreBtn;
    Button countBtn;
    Button totalScoreBtn;
    Button regionalBtn;

    private ArrayList<Player> dataList;
    private LeaderboardArrayAdapter leaderboardArrayAdapter;

    @Override
    public void searchUser(String username){
        leaderboardArrayAdapter.search(username);
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
        leaderboardArrayAdapter = new LeaderboardArrayAdapter(this, dataList);
        LeaderboardList.setAdapter(leaderboardArrayAdapter);
        dataList.add(0, new Player("DonKrieg", "Joshu", 20, 2500));
        dataList.add(1, new Player("Average", "Saumya", 25, 2000));
        dataList.add(2, new Player("LongDongLyndom", "Lyndon", 40, 3500));
        dataList.add(3, new Player("LongTanHandsome", "Carson", 15, 1500));
        dataList.add(4, new Player("BigPapi", "RJ", 30, 3000));
        dataList.add(5, new Player("Rus", "Rus", 35, 1200));

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item2: {
                finish();
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
            case R.id.item7: {
                Intent intent = new Intent(this, MyAccountScreenActivity.class);
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
        //Intent intent = new Intent(this, LeaderboardActivity.class);
        //startActivity(intent);
    }
    public void onClickRegional(View view){
        String name = regionalBtn.getText().toString();
        //clickSort(name);
        //Intent intent = new Intent(this, LeaderboardActivity.class);
        //startActivity(intent);
    }
    /*
    public void clickSort(String name){
        switch (name){
            case "High Score": {
                sortList("High Score");
                break;
            }
            case "Count":{
                sortList("Count");
                break;
            }
            case "Total Score":{
                sortList("Total Score");
                break;
            }
            case "Regional":{
                sortList("Regional");
                break;
            }
        }
    }

    public void sortList(String name) {
        switch (name) {
            case "High Score": {
                for (int i = 0; i < dataList.size() - 1; i++)
                    for (int j = 0; j < dataList.size() - i - 1; j++)
                        if (dataList.get(j).getScore() < dataList.get(j + 1).getScore()) {
                            Player temp = dataList.get(j);
                            dataList.set(j, dataList.get(j + 1));
                            dataList.set(j + 1, temp);

                        }
                break;
            }
            case "Count": {
                for (int i = 0; i < dataList.size() - 1; i++)
                    for (int j = 0; j < dataList.size() - i - 1; j++)
                        if (dataList.get(j).getCount() < dataList.get(j + 1).getCount()) {
                            Player temp = dataList.get(j);
                            dataList.set(j, dataList.get(j + 1));
                            dataList.set(j + 1, temp);
                        }
                break;
            }


        }
        leaderboardArrayAdapter.notifyDataSetChanged();
    }

     */
}

