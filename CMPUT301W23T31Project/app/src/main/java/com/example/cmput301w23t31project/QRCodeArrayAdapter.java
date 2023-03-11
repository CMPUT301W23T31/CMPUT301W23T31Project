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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QRCodeArrayAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> qrCodes;
    private Context context;
    private String username;

    public QRCodeArrayAdapter(Context context, ArrayList<QRCode> codes, String username){
        super(context,0, codes);
        this.qrCodes = codes;
        this.context = context;
        this.username = username;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        //return super.getView(position, convertView, parent);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_code_detail, parent, false);
        }
        else {
            view = convertView;
        }

        QRCode qrCode = getItem(position);
        TextView QRCodeName = view.findViewById(R.id.code_detail_name);
        TextView QRCodePoints = view.findViewById(R.id.code_detail_points);
        ImageView delete = view.findViewById(R.id.delete);
        PlayerScansCollection scans = new PlayerScansCollection();

        Log.d(TAG,"ADAPT:"+ qrCode.getName());
        QRCodeName.setText(qrCode.getName());
        QRCodePoints.setText("Points: "+Integer. toString(qrCode.getScore()));

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrCodes.size() > 0 && position >= 0) {
                    qrCodes.remove(position);
                    QRCodeArrayAdapter.this.notifyDataSetChanged();
                    //Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_LONG).show();
                    DocumentReference scan = scans.getReference().document(username);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(qrCode.getHash(), FieldValue.delete());
                    scan.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });

        return view;
    }
}
