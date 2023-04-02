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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NearbyScansArrayAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> QRCodes;
    private Context context;
    FirebaseFirestore QRdb;
    String activity;
    String username;

    /**
     * Sets up listview for use
     *
     * @param context relevant context
     * @param codes   QR codes
     */
    public NearbyScansArrayAdapter(Context context, ArrayList<QRCode> codes, String activity,String username) {
        super(context, 0, codes);
        this.QRCodes = codes;
        this.context = context;
        this.activity = activity;
        this.username= username;
    }

    /**
     * Gets view (w/ relevant/proper details) for given QR code
     *
     * @param position    position of QR code in list
     * @param convertView view to use
     * @param parent      parent to fill view with
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.code_public_content_detail, parent, false);
        } else {
            view = convertView;
        }

        QRCode QRCode = getItem(position);
        TextView QRCodeName = view.findViewById(R.id.code_detail_name);
        TextView QRCodePoints = view.findViewById(R.id.code_detail_points);
        View tier_indicator = view.findViewById(R.id.tier_indicator_marker);

        Button CodeInfo;
        CodeInfo = view.findViewById(R.id.code_info_button);

        ImageView VisualRep;
        VisualRep = view.findViewById(R.id.qr_code_visual_representation_view);

        PlayerInfoCollection scans = new PlayerInfoCollection();
        QRdb = FirebaseFirestore.getInstance();
        String hash = QRCode.getHash();

        // Filling in details
        String QRName = QRCode.getName();
        double QRDistance = QRCode.getDistance();
        Integer QRScore = QRCode.getScore();

        if(activity.equals("leaderboard")){
            QRCodePoints.setText("Points: "+QRScore);
        }else{
            if (QRDistance < 1){
                QRDistance = QRDistance * 1000;
                QRCodePoints.setText(QRScore + " pts | "+String.format("%.2f",QRDistance)+" m away");
            }else{
                QRCodePoints.setText(QRScore + " pts | "+String.format("%.2f",QRDistance)+" km away");
            }
        }

        QRCodeName.setText(QRCode.getName());

        // dynamically setting color
        if (QRScore < 20) {tier_indicator.setBackgroundResource(R.color.tier_1_teal);}
        else if (QRScore < 200) {tier_indicator.setBackgroundResource(R.color.tier_2_blue);}
        else if (QRScore < 2000) {tier_indicator.setBackgroundResource(R.color.tier_3_purple);}
        else {tier_indicator.setBackgroundResource(R.color.tier_4_pink);}

        Glide.with(context)
                .load("https://api.dicebear.com/6.x/bottts/png?seed="+hash)
                .into(VisualRep);


        // functionality for when a QR code is chosen from list
        CodeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QRCodeStatsActivity.class);
                intent.putExtra("username",username);
                intent.putExtra("Hash", hash);
                context.startActivity(intent);
            }
        });

        return view;
    }
}