package com.example.cmput301w23t31project;

import static android.content.ContentValues.TAG;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Array adapter for QR code visualization in listviews
 */
public class QRCodeArrayAdapter extends ArrayAdapter<QRCode> {
    private ArrayList<QRCode> QRCodes;
    private Context context;
    private String username;
    FirebaseFirestore QRdb;
    CollectionReference collection;

    /**
     * Sets up listview for use
     * @param context relevant context
     * @param codes QR codes
     */
    public QRCodeArrayAdapter(Context context, ArrayList<QRCode> codes, String username){
        super(context,0, codes);
        this.QRCodes = codes;
        this.context = context;
        this.username = username;
    }

    /**
     * Gets view (w/ relevant/proper details) for given QR code
     * @param position position of QR code in list
     * @param convertView view to use
     * @param parent parent to fill view with
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_code_detail, parent, false);
        }
        else {
            view = convertView;
        }

        QRCode QRCode = getItem(position);
        TextView QRCodeName = view.findViewById(R.id.code_detail_name);
        TextView QRCodePoints = view.findViewById(R.id.code_detail_points);
        ImageView delete = view.findViewById(R.id.delete);
        PlayerScansCollection scans = new PlayerScansCollection();
        QRdb = FirebaseFirestore.getInstance();
        String hash = QRCode.getHash();

        Log.d(TAG,"ADAPT: " + QRCode.getName());
        QRCodeName.setText(QRCode.getName());
        QRCodePoints.setText("Points: " + QRCode.getScore());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QRCodes.size() > 0 && position >= 0) {
                    QRCodes.remove(position);
                    QRCodeArrayAdapter.this.notifyDataSetChanged();
                    //Toast.makeText(MainActivity.this, "deleted", Toast.LENGTH_LONG).show();
                    DocumentReference scan = QRdb.collection("PlayerScans").document(username);
                    Map<String, Object> updates = new HashMap<>();
                    updates.put(hash, FieldValue.delete());
                    scan.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i(TAG, "DELETEDDDD"+username);
                        }
                    });
                }
            }
        });

        return view;
    }
}
