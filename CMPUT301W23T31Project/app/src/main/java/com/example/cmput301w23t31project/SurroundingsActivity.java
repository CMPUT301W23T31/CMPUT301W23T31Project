package com.example.cmput301w23t31project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Table;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class SurroundingsActivity extends  HamburgerMenu{

    private String username;
    private String hash;
    private ArrayList<Bitmap> dataList = new ArrayList<>();
    private LeaderboardCountArrayAdapter surroundingsAdapter;
    private TableLayout table;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("CODE SURROUNDINGS");
        setContentView(R.layout.activity_surroundings);
        table = findViewById(R.id.surroundings_image_table);
        image = findViewById(R.id.surroundings_image_view);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        hash = intent.getStringExtra("hash");
        setImages();
    }

    /**
     * For creating the options menu
     * @param menu menu to create
     * @return boolean of whether to display or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.hamburger_menu,menu);
        return true;
    }

    /**
     * Delegates functionality when item is chosen from menu
     * @param item item chosen from menu
     * @return boolean on whether to proceed or not
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return useHamburgerMenu(item, username);

    }

    private void setImages() {
        QRImages images = new QRImages();
        CollectionReference collection = images.getReference();
        collection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                TableRow row = (TableRow) SurroundingsActivity.this.getLayoutInflater().inflate(R.layout.table_row, null);
                TableRow.LayoutParams l = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setLayoutParams(l);
                int i = 0;
                for (QueryDocumentSnapshot doc: task.getResult()) {
                    if (doc.getData().containsKey(hash)) {
                        ImageView temp = row.findViewById(R.id.table_image);
                        String storage_location = doc.getString(hash);
                        Glide.with(SurroundingsActivity.this)
                                .load(storage_location).into(temp);
                        if (i < 3) {
                            row.addView(temp);
                            i++;
                        }
                    }
                }
                table.addView(row, 0);
            }
        });
    }
}