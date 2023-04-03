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
 * Used to search for scan by name
 */
public class SearchScanFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    /**
     * Implemented by MainActivity
     */
    public interface OnFragmentInteractionListener{
        void onDisplayOkPressed(String name);
    }

    /**
     * used to check if the user has implemented the required callback listener to the fragment (OnFragmentInteractionListener)
     * @param context relevant context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Creates dialog for searching for scanned QR code by name
     * @param savedInstanceState saved instance state
     * @return Dialog object to search for scan
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = getLayoutInflater().inflate(R.layout.fragment_search_scan, null);
        EditText searchScan = view.findViewById(R.id.search_scan);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
            .setView(view)
            .setTitle("Search Scan")
            .setNegativeButton("CANCEL",null)
            .setPositiveButton("SEARCH",((dialog, which) -> {
                String name = searchScan.getText().toString();
                listener.onDisplayOkPressed(name);}))
            .create();
    }
}
