package com.example.karat.fksc.OtherProfiles;

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
import com.example.karat.fksc.models.DojoInfoAndSettings;
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

public class OtherProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OtherProfileActivity";
    
    // Layout
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private ImageView mCoverPhoto;
    private ImageView mVerifiedAccount;
    private CircleImageView mProfilePhoto;
    private ImageView mBackArrow;

    private TextView mDisplayName;
    private TextView mDojoName;
    private TextView mRegistrationNumber;
    private TextView mBelt;
    private TextView mRankingGlobalPosition;
    private TextView mAboutMe;
    private TextView mCurriculum;

    private RelativeLayout mTeacherAt;

    private RecyclerView recyclerView;

    // Context
    private Context mContext = OtherProfileActivity.this;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherprofile);

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
        mCoverPhoto = findViewById(R.id.imgView_coverPhoto_headerLayout);
        mVerifiedAccount = findViewById(R.id.imgView_verifiedAccount_headerLayout);
        mProfilePhoto = findViewById(R.id.circleImgView_profilePhoto_headerLayout);

        mDisplayName = findViewById(R.id.textView_userName_headerLayout);
        mDojoName = findViewById(R.id.textView_userDojo_headerLayout);
        mRegistrationNumber = findViewById(R.id.txtView_registrationNumber_FKSCinfoLayout);
        mBelt = findViewById(R.id.txtView_belt_FKSCinfoLayout);
        mRankingGlobalPosition = findViewById(R.id.txtView_globalRanking_FKSCinfoLayout);
        mAboutMe = findViewById(R.id.txtView_aboutMe_aboutmeLayout);
        mCurriculum = findViewById(R.id.txtView_curriculum_aboutmeLayout);

        mTeacherAt = findViewById(R.id.relLayout_teacherAt_activityOtherProfile);

        recyclerView = findViewById(R.id.recyclerView_activityOtherProfile);

    }


    /**
     * Assigning the Firebase Database values to the widgets in the Profile Screen Layout.
     * @param userAndUserSettings
     */
    private void setupWidgetsWithDBValues(UserAndUserSettings userAndUserSettings, UserAboutMe userAboutMe){
        Log.i(TAG, "setupWidgetsWithDBValues: Assigning DB Values to the widgets " + userAndUserSettings.toString());

        // Check if it is a verified account. If so, display the verified ImageView.
        if (userAndUserSettings.getUser().isVerified()){
            mVerifiedAccount.setVisibility(View.VISIBLE);
        }
        UniversalImageLoader.setImage(userAndUserSettings.getUser().getProfile_img_url(), mProfilePhoto, null, "");
        mDisplayName.setText(userAndUserSettings.getUser().getFull_name());
        mDojoName.setText(userAndUserSettings.getUser().getDojo());
        mRegistrationNumber.setText(userAndUserSettings.getUser().getRegistration_number());
        mBelt.setText(userAndUserSettings.getUserSettings().getBelt_color());

        mAboutMe.setText(userAboutMe.getAbout_me());
        mCurriculum.setText(userAboutMe.getCurriculum());

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
     * Set up the RecyclerAdapter to show the: Profile image, Name, DojoName and BeltColor.
     * @param all_dojos_infoAndSettings_fromOneUser
     */
    private void setupRecyclerAdapter(List<DojoInfoAndSettings> all_dojos_infoAndSettings_fromOneUser){
        Log.i(TAG, "setupRecyclerAdapter: " + all_dojos_infoAndSettings_fromOneUser);

        // Show "Teacher At" if there is dojos to be displayed.
        if (all_dojos_infoAndSettings_fromOneUser.size() != 0){
            mTeacherAt.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapterDojos(all_dojos_infoAndSettings_fromOneUser, mContext));

    }


    private void setupClickListeners(){

        mBackArrow.setOnClickListener(this);

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

                // 3) Do all the stuffs with the userID you clicked but not with the current logged in.
                setupWidgetsWithDBValues(firebaseMethods.getUserAndUserSettings(dataSnapshot, user_id),
                        firebaseMethods.getUserAboutMe(dataSnapshot, user_id));
                setupRecyclerAdapter(firebaseMethods.getDojosInfoAndSettingsFromOneUser(dataSnapshot, user_id));
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgView_back_topBarBackArrow){
            finish();
        }
    }


    /*=================================== END OF Firebase ===================================*/

}
