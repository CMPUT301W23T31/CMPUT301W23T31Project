package com.example.cmput301w23t31project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


/**
 * Creates fragment for QRCode not found
 */
public class QRCodeNotFoundFragment extends DialogFragment {

    /**
     * Creates a fragment to show that QRCode (being searched for) was not found
     * @param savedInstanceState previously saved instance state
     * @return Dialog fragment displaying 'QRCode not found' message
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = getLayoutInflater().inflate(R.layout.fragment_q_r_code_not_found, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Error!")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("SEARCH AGAIN",((dialog, which) -> {
                    new SearchScanFragment().show(getFragmentManager(), "Search Again");
                }))
                .create();
    }
}
