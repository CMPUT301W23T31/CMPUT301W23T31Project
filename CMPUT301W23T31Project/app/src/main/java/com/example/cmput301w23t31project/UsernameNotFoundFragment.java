package com.example.cmput301w23t31project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class UsernameNotFoundFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_username_not_found, null);
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
