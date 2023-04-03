package com.example.cmput301w23t31project;


import androidx.appcompat.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;


/**
 * This class serves as the activity where users can see pictures of a QR codes' location
 */
public class SurroundingsActivity extends HamburgerMenu{
    private String username;
    private String hash;
    private String currentUser;
    ListView surroundList;
    ArrayAdapter<Image> surroundArrayAdapter;
    ArrayList<Image> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.title_bar);
        TextView title = findViewById(R.id.myTitle);
        title.setText("CODE SURROUNDINGS");
        setContentView(R.layout.activity_surroundings);
        datalist = new ArrayList<>();
        surroundList = findViewById(R.id.surround_list);
        surroundArrayAdapter = new SurroundingsArrayAdapter(SurroundingsActivity.this,
                datalist);
        surroundList.setAdapter(surroundArrayAdapter);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        hash = intent.getStringExtra("hash");
        currentUser = intent.getStringExtra("currentUser");
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
        return useHamburgerMenu(item, currentUser);

    }

    /**
     * This method sets images to elements of the array adapter to show on the screen
     */
    private void setImages() {
        QRImages images = new QRImages();

        try {
            images.getReference().get().addOnCompleteListener(task -> {
                for (QueryDocumentSnapshot doc: task.getResult()) {
                    if (/*doc.getId().equals(username) &&*/ doc.getData().containsKey(hash)) {
                        Log.d("surround hash", hash+ " "+ doc.getId());
                        String storage = doc.getString(hash);
                        Log.d("surround", storage);
                        datalist.add(new Image(storage,doc.getId()));
                    }
                }
                surroundArrayAdapter.notifyDataSetChanged();

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}