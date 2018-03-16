package com.example.karat.fksc.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.fksc.AddDojo.AddDojoActivity;
import com.example.karat.fksc.EditProfile.EditProfileActivity;
import com.example.karat.fksc.Login.LoginActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.BottomNavigationHelper;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.UniversalImageLoader;
import com.example.karat.fksc.models.User;
import com.example.karat.fksc.models.UserAboutMe;
import com.example.karat.fksc.models.UserAndUserSettings;
import com.example.karat.fksc.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by karat on 13/03/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    
    // Layout
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private ImageView mCoverPhoto;
    private ImageView mVerifiedAccount;
    private CircleImageView mProfilePhoto;

    private TextView mDisplayName;
    private TextView mDojoName;
    private TextView mRegistrationNumber;
    private TextView mBelt;
    private TextView mRankingGlobalPosition;
    private TextView mAboutMe;
    private TextView mCurriculum;

    // Context
    private Context mContext = ProfileActivity.this;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setupFirebaseAuth();
        setupWidgets();
        setupBottomNavigationView();
        setupToolBar();

    }


    /*=================================== Setups ===================================*/
    /**
     * Set up the widgets with the layout id values.
     */
    private void setupWidgets(){

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = findViewById(R.id.toolBar_layout_top_bar);
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
    /*=================================== END OF Setups ===================================*/



    /*=================================== Menu ===================================*/

    /**
     * Inflate the menu to this Action Bar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionbar, menu);

        return true;
    }

    /**
     * Perform an action when user click in one of the menu items.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addDojo_menu_actionbar:
                Intent intentAddDojo = new Intent(mContext, AddDojoActivity.class);
                startActivity(intentAddDojo);
                break;

            case R.id.editProfile_menu_actionbar:
                Intent intentEditProfile = new Intent(mContext, EditProfileActivity.class);
                startActivity(intentEditProfile);
                break;

            case R.id.logout_menu_actionbar:
                // 1) Sign out;
                mAuth.signOut();

                // 2) Navigate to Login Screen.
                Intent intentLogin = new Intent(mContext, LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                break;

            default:
                return super.onOptionsItemSelected(item);

        }

        return true;
    }

    /*=================================== END OF Menu ===================================*/


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
                setupWidgetsWithDBValues(firebaseMethods.getUserAndUserSettings(dataSnapshot),
                        firebaseMethods.getUserAboutMe(dataSnapshot));
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
