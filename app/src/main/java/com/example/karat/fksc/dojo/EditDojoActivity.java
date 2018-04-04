package com.example.karat.fksc.dojo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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

import com.example.karat.fksc.members.MembersActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.share.ShareActivity;
import com.example.karat.fksc.utils.FirebaseMethods;
import com.example.karat.fksc.models.DojoInfoAndSettings;
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

    private TextInputEditText mEditTextDojoName;
    private TextInputEditText mEditTextCity;
    private TextInputEditText mEditTextAddress;
    private TextInputEditText mEditTextTelephone;
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
    private static final int UPLOAD_COVER_PHOTO = 2;
    private String imgURL;

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

    /**
     * When the user click in the Edit button to save the information he edited.
     */
    private void saveChanges(){
        Log.i(TAG, "saveChanges: starting");


        if (mAuth.getCurrentUser() != null) {


            try {
                // Get the text in the fields to store in the database.
                final String name = mEditTextDojoName.getText().toString();
                final String city = mEditTextCity.getText().toString();
                final String address = mEditTextAddress.getText().toString();
                final String telephone = mEditTextTelephone.getText().toString();
                final String description = mEditTextDescription.getText().toString();

                // Case 1) - *********** One or more fields are empty. The user must fill in all the fields. ***********
                if (name.equals("") || city.equals("") || address.equals("")
                        || telephone.equals("") || description.equals("")) {

                    Toast.makeText(mContext, R.string.must_fillin_all_fields, Toast.LENGTH_LONG).show();

                }

                // Case 2) - *********** None of the fields were changed. ***********
                else if (name.equals(dojoInfoAndSettings.getDojoInfo().getName())
                        && city.equals(dojoInfoAndSettings.getDojoInfo().getCity())
                        && address.equals(dojoInfoAndSettings.getDojoSettings().getAddress())
                        && telephone.equals(dojoInfoAndSettings.getDojoSettings().getTelephone())
                        && description.equals(dojoInfoAndSettings.getDojoSettings().getDescription())
                        && imgURL == null){
                    Log.i(TAG, "saveChanges: Nothing changed");
                    navigateToMembersActivity();
                }

                // Case 3) - *********** Users filled in all the fields. Good to go. ***********
                else {

                    Toast.makeText(mContext, getString(R.string.we_are_editing_your_dojo)
                            , Toast.LENGTH_SHORT).show();

                    // Update the fields only if they were changed.
                    String dojoNumber = dojoInfoAndSettings.getDojoSettings().getDojo_number();

                    if (!name.equals(dojoInfoAndSettings.getDojoInfo().getName())) {
                        firebaseMethods.updateDojoName(name, dojoNumber);
                    }
                    if (!city.equals(dojoInfoAndSettings.getDojoInfo().getCity())) {
                        firebaseMethods.updateCity(city, dojoNumber);
                    }
                    if (!address.equals(dojoInfoAndSettings.getDojoSettings().getAddress())) {
                        firebaseMethods.updateAddress(address, dojoNumber);
                    }
                    if (!telephone.equals(dojoInfoAndSettings.getDojoSettings().getTelephone())) {
                        firebaseMethods.updateTelephone(telephone, dojoNumber);
                    }
                    if (!description.equals(dojoInfoAndSettings.getDojoSettings().getDescription())) {
                        firebaseMethods.updateDescription(description, dojoNumber);
                    }

                    // Upload the new picture if there is one.
                    if (imgURL != null) {
                        // Get the number of the dojo.
                        // String dojo2 --> int (2)
                        int dojoCount = Integer.parseInt(dojoNumber.substring(dojoNumber.length() - 1));

                        // Upload the picture to Firebase Storage.
                        firebaseMethods.uploadNewPhoto(mContext.getString(R.string.photo_type_cover_photo_edit)
                                , imgURL, dojoCount);
                    }

                    Toast.makeText(mContext, R.string.info_edit_success, Toast.LENGTH_SHORT).show();

                    navigateToMembersActivity();

                }
            } catch (NullPointerException e){
                Log.i(TAG, "saveChanges: " + e.getMessage());
                Toast.makeText(mContext, mContext.getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
            }

        }

    }
    /*======================================== END OF Save Changes ========================================*/

    /**
     * Navigate to members activity and clear to flags so the user can not come back here
     * by pressing the back button.
     */
    private void navigateToMembersActivity(){

        Intent intentMembers = new Intent(mContext, MembersActivity.class);
        intentMembers.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentMembers);

    }


    /*======================================== Setups ========================================*/

    /**
     * Fill in the editText fields with the firebase real time database values.
     * @param dojoInfoAndSettings the "dojos_info" and "dojos_settings" node info from a
     *                            specific dojo
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
        mEditTextDojoName = findViewById(R.id.editText_dojoName_editDojoLayout);
        mEditTextCity = findViewById(R.id.editText_city_editDojoLayout);
        mEditTextAddress = findViewById(R.id.editText_address_editDojoLayout);
        mEditTextTelephone = findViewById(R.id.editText_telephone_editDojoLayout);
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
     * @param view where the user clicked
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

                // Navigate to Share Activity, put extra to know we came from here (EditDojoActivity).
                Intent intentShareActivity = new Intent(mContext, ShareActivity.class);
                intentShareActivity.putExtra(mContext.getString(R.string.photo_type)
                        , mContext.getString(R.string.photo_type_cover_photo_edit));
                startActivityForResult(intentShareActivity, UPLOAD_COVER_PHOTO);

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


    /*======================================== OnActivity For Result ========================================*/

    /**
     * When the user come back from some activity that was started
     * with "startActivityForResult".
     * @param requestCode number to see where did I requested this method
     * @param resultCode check if it is OK or NOT
     * @param data the intent (Activity) where it came from
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == UPLOAD_COVER_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {

                imgURL = data.getStringExtra(mContext.getString(R.string.img_url));
                Log.i(TAG, "onActivityResult: " + imgURL);

                // Set the picture name to the EditDojo screen so the user can see
                // that he is uploading a picture.
                // At the side of the choose cover photo button'll be the picture name.
                // storage/0/emulated/Pictures/Screenshots/ss1.jpg --> ss1.jpg
                String imgURLLastName = imgURL.substring(imgURL.lastIndexOf("/") + 1);
                mFileNameCoverPhoto.setText(imgURLLastName);



            }

        }

    }

    /*======================================== END OF OnActivity For Result ========================================*/


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

                // 1) Get the intent from the activity that brought us here;
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


