package com.example.karat.fksc.members;

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
import android.widget.RelativeLayout;

import com.example.karat.fksc.R;
import com.example.karat.fksc.utils.FirebaseMethods;
import com.example.karat.fksc.utils.RecyclerAdapterDojos;
import com.example.karat.fksc.models.DojoInfoAndSettings;
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
    private RelativeLayout relativeLayout_PleaseWait;

    // Firebase
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    // Vars
    private List<DojoInfoAndSettings> all_dojos_infoAndSettings;

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
     * @param all_dojos_infoAndSettings a list with all the "dojos_info" and "dojos_settings"
     *                                  node information of all the dojos in the app.
     */
    private void setupRecyclerAdapter(List<DojoInfoAndSettings> all_dojos_infoAndSettings){
        Log.i(TAG, "setupRecyclerAdapter: " + all_dojos_infoAndSettings);

        // List only for the VERIFIED BLACK BELTS.
        List<DojoInfoAndSettings> allDojosVerified = new ArrayList<>();

        // 1) Loop through all dojos.
        for (DojoInfoAndSettings sampleDojo: all_dojos_infoAndSettings){

            // 2) Get only the verified ones.
            if (sampleDojo.getDojoInfo().isVerified()){
                allDojosVerified.add(sampleDojo);
            }

        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapterDojos(allDojosVerified, getActivity()));

    }

    /**
     * Set up the widgets to the Layout id values.
     * @param view get the activity view to be able to access the activity's widgets
     *             from inside the fragment
     */
    private void setupWidgets(View view){

        recyclerView = view.findViewById(R.id.recyclerView_fragmentDojos);
        relativeLayout_PleaseWait = view.findViewById(R.id.relLayout_progressBar_snippetPleaseWait);
        all_dojos_infoAndSettings = new ArrayList<>();

    }
    /*====================================== END OF Setups ======================================*/


    /*====================================== Firebase ======================================*/

    /**
     * This methos set up the Firebase Authentication for users.
     */
    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up FirebaseAuth");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
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

                // 1) Get a list with all the dojos;
                all_dojos_infoAndSettings = firebaseMethods.getAllDojosInfoAndDojosSettings(dataSnapshot);

                // 2) Dismiss the progressBar;
                relativeLayout_PleaseWait.setVisibility(View.GONE);

                // 3) Set up the recycler adapter with the dojos' list.
                setupRecyclerAdapter(all_dojos_infoAndSettings);

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

