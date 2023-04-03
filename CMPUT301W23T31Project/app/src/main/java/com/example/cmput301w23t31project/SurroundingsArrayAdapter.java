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


/**
 * This class serves as an adapter to help display images of QR code surroundings
 */
public class SurroundingsArrayAdapter extends ArrayAdapter<Image> {
    private Context context;

    public SurroundingsArrayAdapter(@NonNull Context context, ArrayList<Image> links) {
        super(context,0,links);
        this.context = context;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.content_surroundings,
                    parent, false);
        }
        else {
            view = convertView;
        }

        Image link = getItem(position);
        String storage = link.getLink();
        ImageView image = view.findViewById(R.id.surroundings_image_view);
        TextView scanner = view.findViewById(R.id.surroundings_text_view);

        Log.d("arrayadapt", storage);
        Glide.with(getContext())
                .load(storage)
                .into(image);
        scanner.setText(" "+link.getUser());

        return view;
    }


}
