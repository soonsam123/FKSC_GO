package com.example.karat.fksc.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.karat.fksc.R;
import com.example.karat.fksc.utils.BottomNavigationHelper;
import com.example.karat.fksc.utils.RecyclerAdapterSearchUsers;
import com.example.karat.fksc.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SearchActivity";
    
    // Layout
    private Toolbar mToolBar;
    private RecyclerView mRecyclerView;
    private BottomNavigationView mBottomNavigationView;
    private EditText mEditText;
    private ImageView mBackButton;

    private ProgressBar mProgressBar;
    private TextView mTxtView_1;
    private TextView mTxtView_2;
    private TextView mTxtView_3;
    
    // Vars
    private List<User> mSelectedUsers;
    private List<String> mSelectedUserIDs;
    
    // Context
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupWidgets();
        setupBottomNavView();
        setupToolBar();
        setupClickListeners();
        initTextListener();

    }


    /*=============================== Init ===============================*/

    /**
     * Listen when the user is typing something in the EditText.
     */
    private void initTextListener(){

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String fullName = mEditText.getText().toString();
                searchForUser(fullName);

            }
        });

    }

    /*=============================== END OF Init ===============================*/


    /*=============================== Search for a specific User ===============================*/

    private void searchForUser(String keyword){
        Log.i(TAG, "searchForUser: Searching a user");

        displayMessage();

        mSelectedUsers.clear();

        if (keyword.length() == 0){
            Log.d(TAG, "searchForUser: EditText is empty");
        } else {

            /*==================== Get the "users" node information ====================*/
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child(getString(R.string.dbname_users))
                    .orderByChild(getString(R.string.field_full_name)).equalTo(keyword);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        Log.i(TAG, "onDataChange: Found a match userID: "
                                + singleSnapshot.getKey());

                        // 1) Get the userID that is the match.
                        mSelectedUserIDs.add(singleSnapshot.getKey());

                        // 2) Get the "users" node information from this selected user.
                        mSelectedUsers.add(singleSnapshot.getValue(User.class));

                        // 4) Dismiss the progressBar and the Message.
                        dismissMessage();

                        // 3) Update the users Recycler View.
                        setupRecyclerAdapter(mSelectedUsers, mSelectedUserIDs);

                        Log.i(TAG, "onDataChange: " + mSelectedUserIDs.toString());
                        }
                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    /*=============================== END OF Search for a specific User ===============================*/



    /*=============================== Setups ===============================*/

    /**
     * Set up the recycler Adapter with the users we found from the database
     * that has the name the user typed in the Edit Text when looking for a user.
     * @param selectedUsers a list with the "users" node information from the user we found in the database
     * @param selectedUserIDs a list with the userID's of the users we found in the database
     */
    private void setupRecyclerAdapter(List<User> selectedUsers, List<String> selectedUserIDs){
        Log.i(TAG, "setupRecyclerAdapter: Setting up the recycler Adapter with this users: " + selectedUsers.toString());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new RecyclerAdapterSearchUsers(selectedUsers, selectedUserIDs, mContext));

    }

    /**
     * Set up the widgets to the id layout values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        // Simple Layout
        mToolBar = findViewById(R.id.toolBar_topBarSearch);
        mRecyclerView = findViewById(R.id.recyclerView_searchUser_activitySearch);
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mEditText = findViewById(R.id.editText_searchUser_topBarSearch);
        mBackButton = findViewById(R.id.imgView_back_topBarSearch);

        // Context
        mContext = SearchActivity.this;

        // Lists
        mSelectedUsers = new ArrayList<>();
        mSelectedUserIDs = new ArrayList<>();

        // Progress Bar and Info Message.
        mProgressBar = findViewById(R.id.progressBar_snippetPleaseWaitSearch);
        mTxtView_1 = findViewById(R.id.txtView_1_snippetPleaseWaitSearch);
        mTxtView_2 = findViewById(R.id.txtView_2_snippetPleaseWaitSearch);
        mTxtView_3 = findViewById(R.id.txtView_3_snippetPleaseWaitSearch);

        mProgressBar.setVisibility(View.GONE);

    }

    /**
     * Set up the bottom navigation view to enable to change between activities
     * and to remove the animation features we don't want to.
     */
    private void setupBottomNavView(){
        Log.i(TAG, "setupBottomNavView: Setting up the Bottom Navigation View");
        
        BottomNavigationHelper.enablePagination(mContext, mBottomNavigationView);
        BottomNavigationHelper.removeShiftMode(mBottomNavigationView);
    }

    /**
     * Set up the layout Tool Bar to be the Default Support Action One.
     */
    private void setupToolBar(){
        Log.i(TAG, "setupToolBar: Setting up the Tool Bar");

        setSupportActionBar(mToolBar);
        
    }

    /**
     * Set up the OnClickListeners
     */
    private void setupClickListeners(){
        Log.i(TAG, "setupClickListeners: Setting up the click listeners");

        mBackButton.setOnClickListener(this);
        
    }


    /*=============================== END OF Setups ===============================*/



    /*=============================== On Click ===============================*/
    /**
     * The user clicked somewhere in the screen.
     * @param view where the user clicked
     */
    @Override
    public void onClick(View view) {

        // Case1) The user clicked in the back button so "Leave the Activity".
        if (view.getId() == R.id.imgView_back_topBarSearch) {
            finish();
        }

    }
    /*=============================== END OF On Click ===============================*/


    /*=============================== Helpers ===============================*/
    /**
     * Dismiss the Progress Bar with the message about what the user should type
     * to look for a user.
     */
    private void dismissMessage(){
        mProgressBar.setVisibility(View.GONE);
        mTxtView_1.setVisibility(View.GONE);
        mTxtView_2.setVisibility(View.GONE);
        mTxtView_3.setVisibility(View.GONE);
    }


    /**
     * Display the Progress Bar with the message about what the user should type
     * to look for a user.
     */
    private void displayMessage(){
        mProgressBar.setVisibility(View.VISIBLE);
        mTxtView_1.setVisibility(View.VISIBLE);
        mTxtView_2.setVisibility(View.VISIBLE);
        mTxtView_3.setVisibility(View.VISIBLE);
    }
    /*=============================== END OF Helpers ===============================*/
    
}


