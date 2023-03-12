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

import java.io.Serializable;
import java.util.ArrayList;
// https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
/**
 * Adapter for QR code stats page
 */
public class QRCodeStatsAdapter extends ArrayAdapter<Player> implements Serializable {
    private Context context;

    /**
     * Instantiates a new adapter
     * @param context relevant context
     * @param players player profiles who have scanned QR code
     */
    public QRCodeStatsAdapter (Context context, ArrayList<Player> players){
        super(context,0,players);
        this.context = context;
    }

    /**
     * Creates view for Array Adapter to display player scans
     * @param position position in list to create view for
     * @param convertView view to use
     * @param parent parent ViewGroup to work within
     * @return view for given scan (in given position)
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_player_detail, parent, false);
        } else {
            view = convertView;
        }

        // getting all attributes to update
        Player player = getItem(position);
        TextView playerName = view.findViewById(R.id.player_detail_username);
        TextView date = view.findViewById(R.id.player_detail_date);
        Button profileBtn = view.findViewById(R.id.player_detail_view_profile_button);

        playerName.setText(player.getUsername());
        date.setText(String.valueOf(player.getTotalScore()));
        profileBtn.setText("View Profile");

        // functionality for 'view player profile' button
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerProfileActivity.class);
                intent.putExtra("Player_Data", player);
                context.startActivity(intent);
            }
        });

        return view;

    }
}
