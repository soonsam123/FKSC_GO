package soon.semicontato.karat.fksc.dojo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.BottomNavigationHelper;
import soon.semicontato.karat.fksc.utils.FirebaseMethods;
import soon.semicontato.karat.fksc.utils.UniversalImageLoader;
import soon.semicontato.karat.fksc.models.DojoInfoAndSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

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

    private RelativeLayout relativeLayout_PleaseWait;

    private LinearLayout linearLayoutContainer;

    // Context
    private Context mContext = DojoActivity.this;

    // Firebase
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dojo);

        setupFirebaseAuth();
        initImageLoader();
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

        linearLayoutContainer = findViewById(R.id.linLayout_container_activityDojo);
        linearLayoutContainer.setVisibility(View.GONE);

        relativeLayout_PleaseWait = findViewById(R.id.relLayout_progressBar_snippetPleaseWait);

    }


    /**
     * Assigning the Firebase Database values to the widgets in the Profile Screen Layout.
     * @param dojoInfoAndSettings the "dojos_info" and "dojos_settings" node info from a
     *                            specific dojo
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
     * @param view where the user clicked
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgView_back_topBarBackArrow){
            finish();
        }
    }
    /*=================================== END OF Setups ===================================*/


    /*==================================== Initialize ====================================*/

    public void initImageLoader(){
        Log.i(TAG, "initImageLoader: Initializing the imageLoader");

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    /*==================================== END OF Initialize ====================================*/


    /*=================================== Firebase ===================================*/

    /**
     * Set up the Firebase Authentication.
     */
    private void setupFirebaseAuth(){

        firebaseMethods = new FirebaseMethods(mContext);
        FirebaseDatabase mFirebaseDataBase = FirebaseDatabase.getInstance();
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

                // 1) You click in some user in the list of all users;
                Intent intentOrigin = getIntent();

                // 2) Get the userID from the dojo you clicked (owner of the dojo) and the number of the dojo;
                String user_id = intentOrigin.getStringExtra(mContext.getString(R.string.field_user_id));
                String dojo_number = intentOrigin.getStringExtra(mContext.getString(R.string.field_dojo_number));

                // 3) Display all information of the chosen dojo (clicked dojo).

                // 3.1) Get all information from this specific dojo;
                DojoInfoAndSettings chosenDojo_InfoAndSettings = firebaseMethods
                        .getDojoInfoAndSettingsFromOneDojo(dataSnapshot, user_id, dojo_number);

                // 3.2) Dismiss the progressBar and enable all the widgets.
                relativeLayout_PleaseWait.setVisibility(View.GONE);
                linearLayoutContainer.setVisibility(View.VISIBLE);

                // 3.3) Set up the widgets with the values from this dojo.
                setupWidgetsWithDBValues(chosenDojo_InfoAndSettings);
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
