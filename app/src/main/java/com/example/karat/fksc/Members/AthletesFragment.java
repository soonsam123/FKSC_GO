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

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.RecyclerAdapter;
import com.example.karat.fksc.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by karat on 06/03/2018.
 */

public class AthletesFragment extends Fragment {

    private static final String TAG = "AthletesFragment";

    // Layout
    private RecyclerView recyclerView;

    // Firebase
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    // Context
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Stating the fragment");
        
        View view = inflater.inflate(R.layout.fragment_athletes_members, container, false);

        setupWidgets(view);
        setupFirebaseAuth();

        return view;
    }


    private void setupRecyclerAdapter(List<User> all_users){

        Log.i(TAG, "setupRecyclerAdapter: " + all_users.toString());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapter(all_users, getActivity()));

    }

    /*====================================== Setups ======================================*/
    /**
     * Set up the widgets to the layout id values.
     * @param view
     */
    private void setupWidgets(View view){

        recyclerView = view.findViewById(R.id.recyclerView_athletesFragment);

        mContext = getActivity();

    }
    /*====================================== END OF Setups ======================================*/



    /*====================================== Firebase ======================================*/

    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up firebase Auth");

        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(getActivity());

        // Get a reference from the users field.
        myRef = FirebaseDatabase.getInstance().getReference().child(mContext.getString(R.string.dbname_users));
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Show all the users node. Key = userID and Value = information (belt_color, birth_date...).
                Log.i(TAG, "onDataChange: " + dataSnapshot.getValue().toString());
                setupRecyclerAdapter(firebaseMethods.getUsers(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // handle database error.
            }
        });

    }


    /*====================================== END OF Firebase ======================================*/

}
