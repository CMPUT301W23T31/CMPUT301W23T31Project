package com.example.cmput301w23t31project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class QRCodeArrayAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> qrCodes;
    private Context context;

    public QRCodeArrayAdapter(Context context, ArrayList<QRCode> codes){
        super(context,0, codes);
        this.qrCodes = codes;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        //return super.getView(position, convertView, parent);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content, parent, false);
        }
        else {
            view = convertView;
        }

        QRCode qrCode = getItem(position);
        TextView QRCodeName = view.findViewById(R.id.QR_code_name);
        TextView QRCodePoints = view.findViewById(R.id.QR_code_points);

        QRCodeName.setText(qrCode.getName());
        QRCodePoints.setText(qrCode.getScore());

        return view;
    }
}
