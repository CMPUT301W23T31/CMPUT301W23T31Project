package com.example.cmput301w23t31project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class QRCodeStatsCommentsAdapter  extends ArrayAdapter<Comment> {
    private Context context;
    public QRCodeStatsCommentsAdapter (Context context, ArrayList<Comment> comments){
        super(context,0,comments);
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_comment_detail, parent, false);
        } else {
            view = convertView;
        }

        Comment comment= getItem(position);
        TextView comment_text = view.findViewById(R.id.comment_detail_comment);
        TextView date = view.findViewById(R.id.comment_detail_date);
        Button username = view.findViewById(R.id.comment_detail_username_button);
        comment_text.setText(comment.getComment());
        date.setText(String.valueOf(comment.getDate()));
        username.setText(comment.getUserName());
        return view;

    }
}
