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

public class SearchScanFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    public interface OnFragmentInteractionListener{
        //Implemented by MainActivity, passes the new GasStationVisit object to the MainActivity to be used in ListView
        void onDisplayOkPressed(String name);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        //used to check if the user has implemented the required callback listener to the fragment (OnFragmentInteractionListener)
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_scan, null);
        EditText searchScan = view.findViewById(R.id.search_scan);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Search Scan")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Search",((dialog, which) -> {
                    String name = searchScan.getText().toString();
                    listener.onDisplayOkPressed(name);}))
                .create();
    }
}
