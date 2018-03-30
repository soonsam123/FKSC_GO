package com.example.karat.fksc.DojoActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.fksc.Members.MembersActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.models.DojoInfo;
import com.example.karat.fksc.models.DojoInfoAndSettings;
import com.example.karat.fksc.models.DojoSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by karat on 14/03/2018.
 */

public class EditDojoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EditDojoActivity";


    // Layout Widgets
    private AppCompatButton mButtonCoverPhoto;

    private TextView mFileNameCoverPhoto;
    private TextView mTextViewChooseCoverPhoto;

    private TextInputLayout mInputLayoutDojoName;
    private TextInputEditText mEditTextDojoName;
    private TextInputLayout mInputLayoutCity;
    private TextInputEditText mEditTextCity;
    private TextInputLayout mInputLayoutAddress;
    private TextInputEditText mEditTextAddress;
    private TextInputLayout mInputLayoutTelephone;
    private TextInputEditText mEditTextTelephone;
    private TextInputLayout mInputLayoutDescription;
    private TextInputEditText mEditTextDescription;

    private Toolbar toolbar;
    private ImageView mBackButton;
    private TextView mAddButton;

    private RelativeLayout mRelativeLayoutContainer;
    private RelativeLayout mRelativeLayout_PleaseWait;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    // Context
    private Context mContext = EditDojoActivity.this;

    // Vars
    private DojoInfoAndSettings dojoInfoAndSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_dojo);
        Log.i(TAG, "onCreate: Stating the activity");

        setupFirebaseAuth();
        setupWidgets();
        setClickListeners();
        setupToolBar();

    }

    /*======================================== Save Changes ========================================*/

    private void saveChanges(){
        Log.i(TAG, "saveChanges: starting");


        if (mAuth.getCurrentUser() != null) {

            // Get the text in the fields to store in the database.
            final String name = mEditTextDojoName.getText().toString();
            final String city = mEditTextCity.getText().toString();
            final String address = mEditTextAddress.getText().toString();
            final String telephone = mEditTextTelephone.getText().toString();
            final String description = mEditTextDescription.getText().toString();

            // Case 1 - One or more fields are empty. The user must fill in all the fields.
            if (name.equals("") || city.equals("") || address.equals("")
                    || telephone.equals("") || description.equals("")){

                Toast.makeText(mContext, R.string.must_fillin_all_fields, Toast.LENGTH_LONG).show();

            }

            // Case 2 - Users filled in all the fields. Good to go.
            else {

                // Update the fields only if they were changed.
                String dojoNumber = dojoInfoAndSettings.getDojoSettings().getDojo_number();

                if (!name.equals(dojoInfoAndSettings.getDojoInfo().getName())){
                    firebaseMethods.updateDojoName(name, dojoNumber);
                }
                if (!city.equals(dojoInfoAndSettings.getDojoInfo().getCity())){
                    firebaseMethods.updateCity(city, dojoNumber);
                }
                if (!address.equals(dojoInfoAndSettings.getDojoSettings().getAddress())){
                    firebaseMethods.updateAddress(address, dojoNumber);
                }
                if (!telephone.equals(dojoInfoAndSettings.getDojoSettings().getTelephone())){
                    firebaseMethods.updateTelephone(telephone, dojoNumber);
                }
                if (!description.equals(dojoInfoAndSettings.getDojoSettings().getDescription())){
                    firebaseMethods.updateDescription(description, dojoNumber);
                }
                Toast.makeText(mContext, R.string.info_edit_success, Toast.LENGTH_SHORT).show();

                // Navigate to members activity and clear to flags so the user can not come back here
                // by pressing the back button.
                Intent intentMembers = new Intent(mContext, MembersActivity.class);
                intentMembers.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMembers);

            }

        }

    }
    /*======================================== END OF Save Changes ========================================*/


    /*======================================== Setups ========================================*/

    /**
     * Fill in the editText fields with the firebase realtime database values.
     * @param dojoInfoAndSettings
     */
    private void setupWidgetsWitDBValues(DojoInfoAndSettings dojoInfoAndSettings){
        Log.i(TAG, "setupWidgetsWitDBValues: starting");
        
        // Fill in the fields with the values from the database.
        mEditTextDojoName.setText(dojoInfoAndSettings.getDojoInfo().getName());
        mEditTextCity.setText(dojoInfoAndSettings.getDojoInfo().getCity());
        mEditTextAddress.setText(dojoInfoAndSettings.getDojoSettings().getAddress());
        mEditTextTelephone.setText(dojoInfoAndSettings.getDojoSettings().getTelephone());
        mEditTextDescription.setText(dojoInfoAndSettings.getDojoSettings().getDescription());

    }

    /**
     * Set up the widgets with the layout id values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        // Buttons
        mButtonCoverPhoto = findViewById(R.id.button_chooseCoverPhoto_editDojoLayout);

        // TextViews
        mTextViewChooseCoverPhoto = findViewById(R.id.txtView_chooseCoverPhoto_editDojoLayout);
        mFileNameCoverPhoto = findViewById(R.id.txtView_coverPhotoPath_editDojoLayout);

        // TextInputLayout and AppCompatEditText
        mInputLayoutDojoName = findViewById(R.id.inputLayout_dojoName_editDojoLayout);
        mEditTextDojoName = findViewById(R.id.editText_dojoName_editDojoLayout);
        mInputLayoutCity = findViewById(R.id.inputLayout_city_editDojoLayout);
        mEditTextCity = findViewById(R.id.editText_city_editDojoLayout);
        mInputLayoutAddress = findViewById(R.id.inputLayout_address_editDojoLayout);
        mEditTextAddress = findViewById(R.id.editText_address_editDojoLayout);
        mInputLayoutTelephone = findViewById(R.id.inputLayout_telephone_editDojoLayout);
        mEditTextTelephone = findViewById(R.id.editText_telephone_editDojoLayout);
        mInputLayoutDescription = findViewById(R.id.inputLayout_description_editDojoLayout);
        mEditTextDescription = findViewById(R.id.editText_description_editDojoLayout);

        // Toolbar and features
        toolbar = findViewById(R.id.toolBar_topBarEditDojoLayout);
        mBackButton = findViewById(R.id.imgView_back_topBarEditDojoLayout);
        mAddButton = findViewById(R.id.txtView_edit_topBarEditDojoLayout);

        // Layouts
        mRelativeLayoutContainer = findViewById(R.id.relLayout_container_editDojoLayout);
        mRelativeLayoutContainer.setVisibility(View.GONE);

        mRelativeLayout_PleaseWait = findViewById(R.id.relLayout_progressBar_snippetPleaseWait);

    }



    /**
     * Set up the customized toolbar to be the one for this activity.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }

    /*======================================== END OF Setups ========================================*/


    /*======================================== Click and Key Listeners ========================================*/

    /**
     * Set up the OnClick Listeners.
     */
    private void setClickListeners(){

        mBackButton.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mButtonCoverPhoto.setOnClickListener(this);

        /* Hide the key board when click in some of these ones */
        mFileNameCoverPhoto.setOnClickListener(this);
        mTextViewChooseCoverPhoto.setOnClickListener(this);
        mRelativeLayoutContainer.setOnClickListener(this);

    }

    /**
     * The user clicks somewhere in the screen.
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgView_back_topBarEditDojoLayout:
                finish();
                break;
            case R.id.txtView_edit_topBarEditDojoLayout:
                saveChanges();
                break;
            case R.id.button_chooseCoverPhoto_editDojoLayout:
                Toast.makeText(this, "Cover Photo", Toast.LENGTH_SHORT).show();
                break;
            /*================== Hide the keyboard if the user click outside ==================*/
            case R.id.txtView_chooseCoverPhoto_editDojoLayout: hideKeyBoard(); break;
            case R.id.txtView_coverPhotoPath_editDojoLayout: hideKeyBoard(); break;
            case R.id.relLayout_container_editDojoLayout: hideKeyBoard(); break;
            /*================== END OF Hide the keyboard if the user click outside ==================*/
        }
    }


    /*======================================== END OF Click and Key Listeners ========================================*/


    /*======================================== Hide ========================================*/

    /**
     * This method is called to hide the keyboard.
     */
    private void hideKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }

    }

    /*======================================== END OF Hide ========================================*/


    /*======================================== Firebase ========================================*/

    /**
     * Set up the Firebase Authentication.
     */
    private void setupFirebaseAuth(){

        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(mContext);
        myRef = FirebaseDatabase.getInstance().getReference();

    }


    /**
     * Life-cycle 2) This method runs after the onCreate.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Starting");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 1) Get the intent from the activty that brought us here;
                Intent intentOrigin = getIntent();

                String userID = intentOrigin.getStringExtra(getString(R.string.field_user_id));
                String dojoNumber = intentOrigin.getStringExtra(getString(R.string.field_dojo_number));

                // 2) Get the info and settings about the dojo so we can use here in this activity to display and update;
                dojoInfoAndSettings = firebaseMethods.getDojoInfoAndSettingsFromOneDojo(dataSnapshot, userID, dojoNumber);

                // 3) Dismiss the progressBar and enable the widgets;
                mRelativeLayout_PleaseWait.setVisibility(View.GONE);
                mRelativeLayoutContainer.setVisibility(View.VISIBLE);

                // 4) Fill in the fields with the database values.
                setupWidgetsWitDBValues(dojoInfoAndSettings);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(listener);

    }

    /**
     * Life-cycle 3) This method runs when the activity become complete invisible.
     */
    @Override
    protected void onStop() {
        super.onStop();

        // Remove listener
        if (listener != null && myRef != null) {
            myRef.removeEventListener(listener);
        }
    }


    /*======================================== END OF Firebase ========================================*/


}


