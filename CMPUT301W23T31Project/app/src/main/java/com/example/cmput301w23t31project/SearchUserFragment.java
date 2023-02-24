package com.example.cmput301w23t31project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SearchUserFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_user, null);
        EditText searchUsername = view.findViewById(R.id.search_user);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Search User")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Search",((dialog, which) -> {
                }))
                .create();
    }
}
