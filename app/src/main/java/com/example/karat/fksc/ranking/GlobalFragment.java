package com.example.karat.fksc.ranking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.fksc.R;

/**
 * Created by karat on 06/03/2018.
 */

public class GlobalFragment extends Fragment{

    private static final String TAG = "GlobalFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting the fragment");

        View view = inflater.inflate(R.layout.fragment_global_ranking, container, false);

        return view;

    }
}
