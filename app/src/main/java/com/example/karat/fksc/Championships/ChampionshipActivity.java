package com.example.karat.fksc.Championships;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.BottomNavigationHelper;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.RecyclerAdapterChampionships;
import com.example.karat.fksc.Utils.SectionsPageAdapter;
import com.example.karat.fksc.models.ChampionshipInfo;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karat on 06/03/2018.
 */

public class ChampionshipActivity extends AppCompatActivity {

    private static final String TAG = "ChampionshipActivity";

    // Layout
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout_PleaseWait;

    // Context
    private Context mContext = ChampionshipActivity.this;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;


    /**
     * Life-cycle 1) First method to run.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship);
        Log.i(TAG, "onCreate: Starting activity");

        setupFirebaseAuth();
        setupWidgets();
        setupBottomNavigationView();
        setupToolBar();

    }


    /**
     * Set up my customized toolbar to be the Support Action Bar for the Championship Activity.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }

    /**
     * Setting up the widgets to the layout values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        toolbar = findViewById(R.id.toolBar_layout_top_bar);

        recyclerView = findViewById(R.id.recyclerView_activityChampionship);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        relativeLayout_PleaseWait = findViewById(R.id.relLayout_progressBar_snippetPleaseWait);

    }


    /**
     * Enable the user to change from one activity to another when clicking in the Bottom Navigation View items.
     */
    private void setupBottomNavigationView(){
        Log.i(TAG, "setupBottomNavigationView: Setting up the Bottom Navigation View");

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);


    }

    /**
     * Set up the recycler adapter with the information from the championships.
     * @param championshipInfos
     */
    private void setupRecyclerAdapter(List<ChampionshipInfo> championshipInfos){
        Log.i(TAG, "setupRecyclerAdapter: Setting up the recycler adapter");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapterChampionships(championshipInfos, mContext));
    }


    /*=================================================== Firebase ===================================================*/

    /**
     * Set up the Firebase User Authentication and the Database Reference.
     */
    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        myRef = FirebaseDatabase.getInstance().getReference();

    }

    /**
     * Life-cycle 2) Runs right after the onCreate method.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Starting");
        
        /*==== Code to make the selected item use a different color ====*/
        // Members = 0 / Championship = 1 / Ranking = 2 / Profile = 3
        int ACTIVITY_NUM = 1;
        // 1) Get the bottomNavigationView menu;
        Menu menu = bottomNavigationView.getMenu();
        // 2) Get the current item;
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        // 3) Select it, so it'll display a different color for this icon.
        menuItem.setChecked(true);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 1) Get a list with all the Championships;
                List<ChampionshipInfo> allChampionshipsInfo = firebaseMethods.getAllChampionshipsInfo(dataSnapshot);

                // 2) Dismiss the progressBar;
                relativeLayout_PleaseWait.setVisibility(View.GONE);

                // 3) Set up the Recycler Adapter with the championships' list.
                setupRecyclerAdapter(allChampionshipsInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(listener);
    }

    /**
     * Runs when the activity is 100% invisible.
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (listener != null && myRef != null){
            myRef.removeEventListener(listener);
        }

    }


    /*=================================================== END OF Firebase ===================================================*/

}
