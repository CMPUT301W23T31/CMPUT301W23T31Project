package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerInfoScreenActivity extends AppCompatActivity {

    Button viewScanBtn;
    ImageButton myAccountBtn;
    TextView player_info_username;
    TextView player_scans_textview;
    TextView score;
    String username;
    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_info_screen);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        password = intent.getStringExtra("password");

        viewScanBtn = findViewById(R.id.player_info_see_scans_button);
        myAccountBtn = findViewById(R.id.player_info_my_account_button);
        player_info_username = findViewById(R.id.player_info_username);
        player_scans_textview = findViewById(R.id.player_info_total_scans);
        score = findViewById(R.id.player_info_total_score);

        //set total scans
        PlayerScansCollection scans = new PlayerScansCollection();
        setTotalScans(scans, player_scans_textview, username);

        //set total score
        QRPlayerScans playerScans = new QRPlayerScans();
        QRCodesCollection QRcodes = new QRCodesCollection();
        setHomeScore(playerScans, score, QRcodes, username);


        player_info_username.setText(username);
        viewScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerScansCollection scans = new PlayerScansCollection();
                scans.processPlayerScansInDatabase(username);
                Intent intent = new Intent(PlayerInfoScreenActivity.this, MyScansScreenActivity.class);
                startActivity(intent);
            }
        });

        myAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerInfoScreenActivity.this, MyAccountScreenActivity.class);
                startActivity(intent);
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

    public static void setHomeScore(QRPlayerScans playerScans, TextView score,
                                    QRCodesCollection QRcodes, String username) {
        playerScans.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Set<String> codes = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc.getId().equals(username)) {
                        codes = doc.getData().keySet();
                    }
                }
                if (codes != null) {
                    Set<String> finalCodes = codes;
                    QRcodes.getReference().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            int total_score = 0;
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if (finalCodes.contains(doc.getId())) {
                                    total_score += Integer.parseInt(doc.getString("Score"));
                                }
                            }
                            score.setText(String.valueOf(total_score));
                        }
                    });
                }
            }
        });
    }


    public static void setTotalScans(PlayerScansCollection scans, TextView player_scans_textview,  String username) {
        AtomicInteger total_scans = new AtomicInteger();
        CollectionReference player_scans = scans.getReference();
        player_scans.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        if (document.getId().equals(username)) {
                            if (document != null) {
                                Map data = document.getData();
                                data.entrySet()
                                        .forEach((entry) ->
                                                total_scans.addAndGet(Integer.valueOf(entry.toString().split("=")[1])));
                                player_scans_textview.setText(Integer.toString(total_scans.get()));
                            }
                        }
                    }

                }
            }
        });
    }
}