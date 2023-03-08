package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyScansScreenActivity extends AppCompatActivity implements SearchScanFragment.OnFragmentInteractionListener{
    ListView qrcodeList;
    ArrayAdapter<QRCode> qrCodeAdapter;
    ArrayList<QRCode> dataList;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_scans_screen);

        Button searchScan;
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        searchScan = findViewById(R.id.my_scans_search_scan_button);

        searchScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SearchScanFragment().show(getSupportFragmentManager(),"Search Scan");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item2: {
                finish();
                return true;
            }
            /*
            case R.id.item3: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.item5: {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            }

            */
            case R.id.item4: {
                Intent intent = new Intent(this, ExploreScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item6: {
                Intent intent = new Intent(this, PlayerInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item7: {
                Intent intent = new Intent(this, MyAccountScreenActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.item8: {
                Intent intent = new Intent(this, AppInfoScreenActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onDisplayOkPressed(String name){
        QRCodesCollection QR_codes = new QRCodesCollection();
        CollectionReference codes = QR_codes.getReference();
        codes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getString("Name").equals(name)) {
                            String hash_return =  document.getId();
                            Intent intent = new Intent(MyScansScreenActivity.this, QRCodeStatsActivity.class);
                            intent.putExtra("Hash", hash_return);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}
