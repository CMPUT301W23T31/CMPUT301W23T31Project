package com.example.cmput301w23t31project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class SearchUserFragment extends DialogFragment {

    interface SearchUserDialogListener{
        void searchUser(String username);
    }


    private SearchUserDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        if(context instanceof SearchUserDialogListener){
            listener = (SearchUserDialogListener) context; // assigning listener variable context casted to SearchUserDialogListener
        }
        else{
            throw new RuntimeException(context+"must implement SearchUserDialogListener");
        }
    }


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
                    String username = searchUsername.getText().toString();
                    listener.searchUser(username);
                }))
                .create();
    }
}
