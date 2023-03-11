package com.example.cmput301w23t31project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * Used to search for player by username
 */
public class SearchUserFragment extends DialogFragment {

    private SearchUserDialogListener listener;

    /**
     * Used for searching for player by username
     */
    interface SearchUserDialogListener{
        void searchUser(String username);
    }

    /**
     * used to check if the user has implemented the required callback listener to the fragment (SearchUserDialogListener)
     * @param context relevant context
     */
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

    /**
     * Creates dialog for searching for player by username
     * @param savedInstanceState saved instance state
     * @return Dialog object to search for scan
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = getLayoutInflater().inflate(R.layout.fragment_search_user, null);
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
