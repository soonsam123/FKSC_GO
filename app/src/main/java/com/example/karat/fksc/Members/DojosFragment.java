package com.example.karat.fksc.Members;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.RecyclerAdapterBlackBelts;
import com.example.karat.fksc.Utils.RecyclerAdapterDojos;
import com.example.karat.fksc.models.DojoInfo;
import com.example.karat.fksc.models.DojoSettings;
import com.example.karat.fksc.models.UserAndUserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karat on 17/03/2018.
 */

public class DojosFragment extends Fragment{

    private static final String TAG = "DojosFragment";

    // Layout
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    // Context
    private Context mContext = getActivity();

    // Vars
    private List<DojoInfo> all_dojos_info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting the fragment");

        View view = inflater.inflate(R.layout.fragment_dojos_members, container, false);

        setupWidgets(view);
        setupFirebaseAuth();

        return view;
    }

    /*====================================== Setups ======================================*/
    /**
     * Set up the RecyclerAdapter to show the: Profile image, Name, DojoName and BeltColor.
     * @param all_users
     */
    private void setupRecyclerAdapter(List<DojoInfo> all_dojos_info){
        Log.i(TAG, "setupRecyclerAdapter: " + all_dojos_info);

        // List only for the VERIFIED BLACK BELTS.
        List<DojoInfo> allDojosInfoVerified = new ArrayList<>();

        // 1) Loop through all dojos.
        for (DojoInfo sampleDojo: all_dojos_info){

            // 2) Get only the verified ones.
            if (sampleDojo.isVerified()){
                allDojosInfoVerified.add(sampleDojo);
            }

        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapterDojos(allDojosInfoVerified, getActivity()));

    }

    /**
     * Set up the widgets to the Layout id values.
     * @param view
     */
    private void setupWidgets(View view){

        recyclerView = view.findViewById(R.id.recyclerView_fragmentDojos);
        mProgressBar = view.findViewById(R.id.progressBar_fragmentDojos);
        all_dojos_info = new ArrayList<>();

    }
    /*====================================== END OF Setups ======================================*/


    /*====================================== Firebase ======================================*/

    /**
     * This methos set up the Firebase Authentication for users.
     */
    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up FirebaseAuth");

        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(getActivity());


    }

    /**
     * Life-cycle 2) This method is called after the onCreate.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Get database reference.
        myRef = FirebaseDatabase.getInstance().getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                all_dojos_info = firebaseMethods.getAllDojosInfo(dataSnapshot);
                mProgressBar.setVisibility(View.GONE);
                setupRecyclerAdapter(all_dojos_info);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // Attach the listener to the reference.
        myRef.addListenerForSingleValueEvent(listener);

    }

    /**
     * Life-cycle 3) This method is called when the activity becomes completed invisible.
     */
    @Override
    public void onStop() {

        if (myRef != null && listener != null){
            myRef.removeEventListener(listener);
        }
        super.onStop();
    }


    /*====================================== END OF Firebase ======================================*/
}

