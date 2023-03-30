package com.example.cmput301w23t31project;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    private String currentUser;
    FirebaseFirestore QRdb;
    CollectionReference collection;

    /**
     * Sets up listview for use
     * @param context relevant context
     * @param codes QR codes
     */
    public QRCodeArrayAdapter(Context context, ArrayList<QRCode> codes, String username,String currentUser){
        super(context,0, codes);
        this.QRCodes = codes;
        this.context = context;
        this.username = username;
        this.currentUser = currentUser;
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
        ImageView delete = view.findViewById(R.id.delete_button);
        View tier_indicator = view.findViewById(R.id.tier_indicator_marker);

        Button CodeInfo;
        CodeInfo = view.findViewById(R.id.code_info_button);

        PlayerInfoCollection scans = new PlayerInfoCollection();
        QRdb = FirebaseFirestore.getInstance();
        String hash = QRCode.getHash();

        // filling in details
        String QRName = QRCode.getName();
        Integer QRScore = QRCode.getScore();
        Log.d(TAG,"ADAPT: " + QRName);
        QRCodeName.setText(QRName);
        QRCodePoints.setText("Points: " + QRScore);

        // dynamically setting color
        if (QRScore < 20) {tier_indicator.setBackgroundResource(R.color.tier_1_teal);}
        else if (QRScore < 200) {tier_indicator.setBackgroundResource(R.color.tier_2_blue);}
        else if (QRScore < 2000) {tier_indicator.setBackgroundResource(R.color.tier_3_purple);}
        else {tier_indicator.setBackgroundResource(R.color.tier_4_pink);}

        // delete button stuff
        if (!currentUser.equals(username)){
            delete.setVisibility(View.GONE);
            Log.i("TAG", currentUser+":crnt   display:"+username);
        }else {
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
                                Log.i(TAG, "DELETEDDDD" + username);
                            }
                        });
                    }
                }
            });
        }

        // functionality for when a QR code is chosen from list
        CodeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "REACHED HERE!!");
                Intent intent = new Intent(context, QRCodeStatsActivity.class);
                intent.putExtra("Hash", hash);
                intent.putExtra("username", username);
                context.startActivity(intent);
            }
        });




        return view;
    }
//    public boolean isVisibility(){
//        Utilities utilities = new Utilities();
//
//        QRdb = FirebaseFirestore.getInstance();
//        QRdb.collection("Accounts").get() .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                // after getting the data we are calling on success method
//                // and inside this method we are checking if the received
//                // query snapshot is empty or not.
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    // if the snapshot is not empty we are
//                    // hiding our progress bar and adding
//                    // our data in a list.
//                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//                    for (DocumentSnapshot document : list) {
//                        if(document.getId().equals(username));
//                    }
//
//        }
//    }});
//
//    }
}
