package com.example.cmput301w23t31project;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SurroundingsArrayAdapter extends ArrayAdapter<String> {
    ArrayList<String> links;

    public SurroundingsArrayAdapter(@NonNull Context context, ArrayList<String> links) {
        super(context, 0);
        this.links = links;
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

        String link = getItem(position);
        ImageView image = view.findViewById(R.id.surroundings_image_view);
        TextView scanner = view.findViewById(R.id.surroundings_text_view);

        Log.d("arrayadapt", link);
        Glide.with(getContext())
                .load(link)
                .into(image);

        return view;
    }


}
