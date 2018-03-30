package com.example.karat.fksc.Members;

import android.content.Context;
import android.icu.lang.UScript;
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
import android.widget.RelativeLayout;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.RecyclerAdapter;
import com.example.karat.fksc.models.User;
import com.example.karat.fksc.models.UserAndUserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by karat on 06/03/2018.
 */

public class AthletesFragment extends Fragment {

    private static final String TAG = "AthletesFragment";

    // Layout
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout_PleaseWait;

    // Firebase
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private ValueEventListener listener;

    // Context
    private Context mContext;

    // Vars
    private List<UserAndUserSettings> all_users;


    /**
     * Life-cyle 1) This method is called when the app is launching.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Stating the fragment");
        
        View view = inflater.inflate(R.layout.fragment_athletes_members, container, false);

        setupWidgets(view);
        setupFirebaseAuth();

        return view;

    }

    /*====================================== Setups ======================================*/

    /**
     * Set up the recycler adapter with the user that are verified.
     * @param all_users
     */
    private void setupRecyclerAdapter(List<UserAndUserSettings> all_users){


        Log.i(TAG, "setupRecyclerAdapter: " + all_users.toString());

        List<UserAndUserSettings> users_verified = new ArrayList<>();

        for (UserAndUserSettings singleUser: all_users){
            if (singleUser.getUser().isVerified()){
                users_verified.add(singleUser);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapter(users_verified, getActivity()));

    }

    /**
     * Set up the widgets to the layout id values.
     * @param view
     */
    private void setupWidgets(View view){

        recyclerView = view.findViewById(R.id.recyclerView_athletesFragment);
        relativeLayout_PleaseWait = view.findViewById(R.id.relLayout_progressBar_snippetPleaseWait);
        all_users = new ArrayList<>();

        mContext = getActivity();

    }
    /*====================================== END OF Setups ======================================*/



    /*====================================== Firebase ======================================*/

    /**
     * This method set up the Firebase Authentication for users.
     */
    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up firebase Auth");

        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(getActivity());

    }


    /**
     * Life-cyle 2) This is called after onCreate().
     */
    @Override
    public void onStart() {
        super.onStart();

        // Get a reference from the users field.
        myRef = FirebaseDatabase.getInstance().getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 1) Get a list with all the users;
                all_users = firebaseMethods.getAllUserAndUserSettings(dataSnapshot);

                // 2) Dismiss the progressBar;
                relativeLayout_PleaseWait.setVisibility(View.GONE);

                // 3) Set up the recycler adapter with the users' list.
                setupRecyclerAdapter(all_users);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addListenerForSingleValueEvent(listener);

    }

    /**
     * Life-cyle 3) This method is called when the activity is 100% not visible anymore.
     */
    @Override
    public void onStop() {

        if (listener != null && myRef != null) {
            myRef.removeEventListener(listener);
        }

        super.onStop();
    }

    /*====================================== END OF Firebase ======================================*/

}
