package com.example.cmput301w23t31project;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ScanResultsFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private String result;
    private TextView resultView;

    public ScanResultsFragment(String n){
        result = n;
    }

    public interface OnFragmentInteractionListener {
        //Implemented by MainActivity, passes the new GasStationVisit object to the MainActivity to be used in ListView
        void onOkPressed();
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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        resultView = getView().findViewById(R.id.scan_results_data);
        //creates the Dialog and handles responses to interactions between user and layout
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_scan_results, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("SCAN RESULTS")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resultView.setText(result);
                        listener.onOkPressed();

                    }
                }).create();
    }
}
