package com.example.cmput301w23t31project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


/**
 * Creates fragment for usernames not found
 */
public class UsernameNotFoundFragment extends DialogFragment {

    /**
     * Creates a fragment to show that username (being searched for) was not found
     * @param savedInstanceState previously saved instance state
     * @return Dialog fragment displaying 'username not found' message
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = getLayoutInflater().inflate(R.layout.fragment_username_not_found, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
            .setView(view)
            .setTitle("Error!")
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Search Again",((dialog, which) -> {
                new SearchUserFragment().show(getFragmentManager(), "Search Again");
            }))
            .create();
    }
}
