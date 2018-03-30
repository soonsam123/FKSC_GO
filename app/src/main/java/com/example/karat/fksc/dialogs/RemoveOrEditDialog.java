package com.example.karat.fksc.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.fksc.R;

/**
 * Created by karat on 19/03/2018.
 */

public class RemoveOrEditDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "RemoveOrEditDialog";

    private AppCompatButton mRemove;
    private AppCompatButton mEdit;


    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_remove_or_edit, container, false);

        setupWidgets(view);
        setupClickListeners();

        return view;
    }


    /**
     * Set up the widgets with the layout id values.
     * @param view
     */
    private void setupWidgets(View view){

        mRemove = view.findViewById(R.id.button_remove_DialogRemoveOrEdit);
        mEdit = view.findViewById(R.id.button_edit_DialogRemoveOrEdit);

        mContext = getActivity();

    }

    /**
     * Set up the CLICK Listeners
     */
    private void setupClickListeners(){

        mRemove.setOnClickListener(this);
        mEdit.setOnClickListener(this);

    }

    /**
     * The user clicks somewhere in the screen.
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_remove_DialogRemoveOrEdit){
            Toast.makeText(mContext, "Remove", Toast.LENGTH_SHORT).show();

        } else if (view.getId() == R.id.button_edit_DialogRemoveOrEdit){
            Toast.makeText(mContext, "Edit", Toast.LENGTH_SHORT).show();
        }
    }
}
