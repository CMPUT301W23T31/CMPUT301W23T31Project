package com.example.cmput301w23t31project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


/**
 * This class creates a Dialog Fragment to show the name and score of a QR Code the user has
 * just scanned. It also processes QR Code data to be viewed in detail if the user wishes to do so
 */
public class AllowLocationFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    public AllowLocationFragment(){

    }

    /**
     * This interface has the onOkPressed() method, which checks if any clicks to jump to
     * other fragments occur in the ScanResults Fragment
     */
    public interface OnFragmentInteractionListener {
        void onOkPressed(boolean recordLocation);
    }

    /**
     * This method checks if user has implemented the required callback listener to the fragment
     * @param context
     *      Context information for an OnFragmentInteractionListener class
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context +
                    " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This method displays a pop up dialog fragment and shows the user a QR code name and score
     * @param savedInstanceState
     *      Bundle object needed to create a Dialog fragment
     * @return
     *      A Dialog fragment displaying a QR code name and score
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //creates the Dialog and handles responses to interactions between user and layout
        View view = getLayoutInflater().inflate(R.layout.fragment_allow_location, null);

        // Build dialog fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Do you want to record location of QRCode?")
                .setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Head back to main menu and close the dialog fragment
                        listener.onOkPressed(true);
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onOkPressed(false);
                        dialogInterface.cancel();
                    }
                }).create();
    }
}
