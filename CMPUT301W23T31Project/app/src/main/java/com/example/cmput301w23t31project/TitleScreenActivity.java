package com.example.cmput301w23t31project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class TitleScreenActivity extends AppCompatActivity {
    FirebaseFirestore QRdb = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_title_screen);
    }

    public void onTap(View view){
        QRdb.collection("Accounts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getString("DeviceID").equals(Utilities.
                                        getDeviceId(TitleScreenActivity.this))) {
                                    Intent intent = new Intent(TitleScreenActivity.
                                            this, MainActivity.class);
                                    intent.putExtra("username", "");
                                    intent.putExtra("username_present",
                                            document.getId());
                                    startActivity(intent);
                                    return;
                                }
                            }
                            Intent intent = new Intent(
                                    TitleScreenActivity.this, LoginActivity.class);
                            intent.putExtra("username", "");
                            startActivity(intent);
                        }
                    }
                });
    }
}
