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

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Adapter for QR code comment stats page
 */
public class QRCodeStatsCommentsAdapter  extends ArrayAdapter<Comment> {
    private Context context;
    private String username;

    /**
     * Instantiates a new adapter
     * @param context relevant context
     * @param comments comments on given QR code
     */
    public QRCodeStatsCommentsAdapter (Context context, ArrayList<Comment> comments, String username){
        super(context,0,comments);
        this.context = context;
        this.username = username;
    }


    /**
     * Creates view for Array Adapter to display comments
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
            view = LayoutInflater.from(context).inflate(R.layout.content_comment_detail, parent, false);
        } else {
            view = convertView;
        }

        // getting all attributes to update
        Comment comment= getItem(position);
        TextView comment_text = view.findViewById(R.id.comment_detail_comment);
        TextView date = view.findViewById(R.id.comment_detail_date);
        TextView username_text = view.findViewById(R.id.comment_detail_username);
        Button profileBtn = view.findViewById(R.id.comment_detail_username_button);

        // updating and filling
        comment_text.setText(comment.getComment());
        date.setText(String.valueOf(comment.getDate()));
        username_text.setText(comment.getUserName());

        // functionality for 'view player profile' button
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayerProfileActivity.class);
                intent.putExtra("username", comment.getUserName());
                intent.putExtra("crnt_username", username);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
