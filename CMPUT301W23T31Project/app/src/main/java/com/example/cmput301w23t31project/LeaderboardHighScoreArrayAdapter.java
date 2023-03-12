package com.example.cmput301w23t31project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class LeaderboardHighScoreArrayAdapter extends ArrayAdapter<Player> {
    private Context context;
    private String Username;
    public LeaderboardHighScoreArrayAdapter (Context context, ArrayList<Player> players,String Username){
        super(context,0,players);
        this.context = context;
        this.Username = Username;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_leaderboard_high_score_list, parent, false);
        } else {
            view = convertView;
        }

        Player player = getItem(position);

        TextView playerName = view.findViewById(R.id.leaderboard_content_player_name_text);
        TextView score = view.findViewById(R.id.leaderboard_content_High_score);
        TextView usernameText = view.findViewById(R.id.leaderboard_content_user_name);
        Button profileBtn = view.findViewById(R.id.leaderboard_content_profile_button);
        TextView rank = view.findViewById(R.id.leaderboard_content_rank);
        rank.setText(String.valueOf(player.getRank()));
        playerName.setText(player.getPlayerName());
        score.setText(String.valueOf(player.getHighestScoringQR()));
        usernameText.setText(player.getUsername());
        profileBtn.setText("View Profile");

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerProfileActivity.class);
                intent.putExtra("Player_Data", player);
                intent.putExtra("currentPlayer",getUsername());
                intent.putExtra("username", Username);
                context.startActivity(intent);
            }
        });


        return view;

    }
    public String getUsername() {
        return Username;
    }
}

