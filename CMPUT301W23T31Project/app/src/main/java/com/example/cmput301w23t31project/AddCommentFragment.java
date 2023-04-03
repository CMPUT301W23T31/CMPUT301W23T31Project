package com.example.cmput301w23t31project;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


/**
 * This class opens up a fragment that allows for the user to create a comment for a QR code
 */
public class AddCommentFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    public String hash;
    Button add;
    Button cancel;
    public interface OnFragmentInteractionListener{
        //Implemented by MainActivity, passes the new GasStationVisit object to the
        // MainActivity to be used in ListView
        void onDisplayOkPressed(String commentText, String hash);
    }

    /**
     * Constructor for AddCommentFragment
     * @param hash
     *      The hash of the QR code the user is commenting on
     */
    AddCommentFragment(String hash){
        this.hash = hash;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        //used to check if the user has implemented the required callback
        // listener to the fragment (OnFragmentInteractionListener)
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        View view = getLayoutInflater().inflate(R.layout.fragment_add_comment, null);
        EditText comment = view.findViewById(R.id.add_comment);
        add = view.findViewById(R.id._add_button);
        cancel = view.findViewById(R.id.comments_cancel_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Dialog built = builder
                .setView(view)
                .setTitle("Add Comment")
                .create();
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                built.dismiss();
            }

    });

        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String commentText = comment.getText().toString();
                listener.onDisplayOkPressed(commentText, hash);
                built.dismiss();
            }

        });
        return built;
    }
}
