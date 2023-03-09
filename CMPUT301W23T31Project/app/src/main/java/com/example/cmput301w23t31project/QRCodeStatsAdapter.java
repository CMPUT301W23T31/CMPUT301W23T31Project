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

public class QRCodeStatsAdapter  extends ArrayAdapter<Player> {
        private Context context;
        public QRCodeStatsAdapter (Context context, ArrayList<Player> players){
            super(context,0,players);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.content_player_detail, parent, false);
            } else {
                view = convertView;
            }

            Player player = getItem(position);
            TextView playerName = view.findViewById(R.id.player_detail_username);
            TextView date = view.findViewById(R.id.player_detail_date);
            Button profileBtn = view.findViewById(R.id.player_detail_view_profile_button);
            playerName.setText(player.getPlayerName());
            date.setText(String.valueOf(player.getTotalScore()));
            profileBtn.setText("View Profile");

            profileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayerProfileActivity.class);
                    context.startActivity(intent);
                }
            });



            return view;

        }
    }
