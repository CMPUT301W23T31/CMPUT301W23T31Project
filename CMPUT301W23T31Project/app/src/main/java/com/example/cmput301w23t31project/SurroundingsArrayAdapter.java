package com.example.cmput301w23t31project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SurroundingsArrayAdapter extends ArrayAdapter<Image> {
    ArrayList<Image> links;
    private Context context;

    public SurroundingsArrayAdapter(@NonNull Context context, ArrayList<Image> links) {
        super(context,0,links);
        this.context = context;
        //String temp = links.get(0).getLink();
        Log.d("arrayadapt", "enters"+links.get(0).getLink());
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content_surroundings, parent, false);
        }
        else {
            view = convertView;
        }

        Image link = getItem(position);
        String storage = link.getLink();
        ImageView image = view.findViewById(R.id.surroundings_image_view);
        TextView username = view.findViewById(R.id.surroundings_username_text);
        TextView date = view.findViewById(R.id.surroundings_date_text);

        Log.d("arrayadapt", storage);
        Glide.with(getContext())
                .load(storage)
                .into(image);
        username.setText(link.getUser());
        date.setText("2023-03-30");  // TODO: ACTUALLY MAKE THIS FUNCTIONAL

        return view;
    }


}
