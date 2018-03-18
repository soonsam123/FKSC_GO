package com.example.karat.fksc.DojoActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.BottomNavigationHelper;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.RecyclerAdapterDojos;
import com.example.karat.fksc.Utils.UniversalImageLoader;
import com.example.karat.fksc.models.DojoInfo;
import com.example.karat.fksc.models.DojoInfoAndSettings;
import com.example.karat.fksc.models.DojoSettings;
import com.example.karat.fksc.models.UserAboutMe;
import com.example.karat.fksc.models.UserAndUserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by karat on 17/03/2018.
 */

public class DojoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "DojoActivity";
    
    // Layout
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private ImageView mCoverPhoto;
    private ImageView mVerifiedAccount;
    private ImageView mBackArrow;

    private TextView mDojoName;
    private TextView mCity;
    private TextView mRegistrationNumber;
    private TextView mRankingGlobalPosition;
    private TextView mAddress;
    private TextView mTelephone;
    private TextView mDescription;

    // Context
    private Context mContext = DojoActivity.this;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dojo);

        setupFirebaseAuth();
        setupWidgets();
        setupBottomNavigationView();
        setupToolBar();
        setupClickListeners();

    }


    /*=================================== Setups ===================================*/
    /**
     * Set up the widgets with the layout id values.
     */
    private void setupWidgets(){

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = findViewById(R.id.toolBar_topBarBackArrow);

        mBackArrow = findViewById(R.id.imgView_back_topBarBackArrow);
        mCoverPhoto = findViewById(R.id.imgView_coverPhoto_headerDojoLayout);
        mVerifiedAccount = findViewById(R.id.imgView_verifiedAccount_headerDojoLayout);

        mDojoName = findViewById(R.id.textView_dojoName_headerDojoLayout);
        mCity = findViewById(R.id.textView_city_headerDojoLayout);
        mRegistrationNumber = findViewById(R.id.txtView_registrationNumber_FKSCinfoDojoLayout);
        mRankingGlobalPosition = findViewById(R.id.txtView_globalRanking_FKSCinfoDojoLayout);
        mAddress = findViewById(R.id.txtView_address_aboutDojoLayout);
        mTelephone = findViewById(R.id.txtView_telephone_aboutDojoLayout);
        mDescription = findViewById(R.id.txtView_description_aboutDojoLayout);

    }


    /**
     * Assigning the Firebase Database values to the widgets in the Profile Screen Layout.
     * @param dojoInfoAndSettings
     */
    private void setupWidgetsWithDBValues(DojoInfoAndSettings dojoInfoAndSettings){
        Log.i(TAG, "setupWidgetsWithDBValues: DojoInfo " + dojoInfoAndSettings.getDojoInfo().toString());
        Log.i(TAG, "setupWidgetsWithDBValues: DojoSettings " + dojoInfoAndSettings.getDojoSettings().toString());

        // Check if it is a verified account. If so, display the verified ImageView.
        if (dojoInfoAndSettings.getDojoInfo().isVerified()){
            mVerifiedAccount.setVisibility(View.VISIBLE);
        }

        UniversalImageLoader.setImage(dojoInfoAndSettings.getDojoInfo().getCover_img_url(), mCoverPhoto, null, "");
        mDojoName.setText(dojoInfoAndSettings.getDojoInfo().getName());
        mCity.setText(dojoInfoAndSettings.getDojoInfo().getCity());
        mRegistrationNumber.setText(dojoInfoAndSettings.getDojoInfo().getRegistration_number());

        mAddress.setText(dojoInfoAndSettings.getDojoSettings().getAddress());
        mTelephone.setText(dojoInfoAndSettings.getDojoSettings().getTelephone());
        mDescription.setText(dojoInfoAndSettings.getDojoSettings().getDescription());

    }

    /**
     * Set up the bottom navigation view to be clickable and to remove the shifting mode when
     * an item is clicked.
     */
    private void setupBottomNavigationView(){

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);

    }

    /**
     * Set up the customized toolbar to be the OFICIAL one.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }

    /**
     * Set up the CLICK Listeners.
     */
    private void setupClickListeners(){

        mBackArrow.setOnClickListener(this);

    }

    /**
     * The user clicks somewhere in the screen.
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgView_back_topBarBackArrow){
            finish();
        }
    }
    /*=================================== END OF Setups ===================================*/


    /*=================================== Firebase ===================================*/

    /**
     * Set up the Firebase Authentication.
     */
    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDataBase.getReference();

    }

    /**
     * Life-cycle 2) This method is called after onCreate.
     */
    @Override
    protected void onStart() {
        super.onStart();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 1) You click in some user in the list of all users.
                Intent intentOrigin = getIntent();

                // 2) Get the userID from the user you clicked.
                String user_id = intentOrigin.getStringExtra(mContext.getString(R.string.field_user_id));
                String dojo_number = intentOrigin.getStringExtra(mContext.getString(R.string.field_dojo_number));

                // 3) Do all the stuffs with the userID and the number of the dojo you clicked.
                setupWidgetsWithDBValues(firebaseMethods.getDojoInfoAndSettingsFromOneDojo(dataSnapshot, user_id, dojo_number));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myRef.addListenerForSingleValueEvent(listener);

    }


    @Override
    protected void onStop() {
        super.onStop();
        // Remove the ValueEventListener.

        if (listener != null && myRef != null){
            myRef.removeEventListener(listener);
        }
    }

    /*=================================== END OF Firebase ===================================*/

}
