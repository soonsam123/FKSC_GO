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
import com.example.karat.fksc.Share.ShareActivity;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.models.DojoInfo;
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

public class AddDojoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddDojoActivity";


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

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    // Context
    private Context mContext = AddDojoActivity.this;

    // Vars
    private DojoInfo dojoInfo;
    private DojoSettings dojoSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dojo);
        Log.i(TAG, "onCreate: Stating the activity");

        setupFirebaseAuth();
        setupWidgets();
        setClickListeners();
        setupToolBar();

    }

    /*======================================== Save Changes ========================================*/

    /**
     * When the user click in the check button.
     */
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

                // Add a dojo to the database for the current user logged in.
                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(mContext, R.string.we_are_adding_your_dojo, Toast.LENGTH_SHORT).show();
                        firebaseMethods.addDojo(name, city, "", "", false
                                , address, telephone, description, name, firebaseMethods.getDojoCount(dataSnapshot));

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                myRef.addListenerForSingleValueEvent(listener);

                Intent intentMembers = new Intent(mContext, MembersActivity.class);
                intentMembers.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentMembers);

            }

        }

    }
    /*======================================== END OF Save Changes ========================================*/


    /*======================================== Setups ========================================*/

    /**
     * Set up the widgets with the layout id values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        // Buttons
        mButtonCoverPhoto = findViewById(R.id.button_chooseCoverPhoto_addDojoLayout);

        // TextViews
        mTextViewChooseCoverPhoto = findViewById(R.id.txtView_chooseCoverPhoto_addDojoLayout);
        mFileNameCoverPhoto = findViewById(R.id.txtView_coverPhotoPath_addDojoLayout);

        // TextInputLayout and AppCompatEditText
        mInputLayoutDojoName = findViewById(R.id.inputLayout_dojoName_addDojoLayout);
        mEditTextDojoName = findViewById(R.id.editText_dojoName_addDojoLayout);
        mInputLayoutCity = findViewById(R.id.inputLayout_city_addDojoLayout);
        mEditTextCity = findViewById(R.id.editText_city_addDojoLayout);
        mInputLayoutAddress = findViewById(R.id.inputLayout_address_addDojoLayout);
        mEditTextAddress = findViewById(R.id.editText_address_addDojoLayout);
        mInputLayoutTelephone = findViewById(R.id.inputLayout_telephone_addDojoLayout);
        mEditTextTelephone = findViewById(R.id.editText_telephone_addDojoLayout);
        mInputLayoutDescription = findViewById(R.id.inputLayout_description_addDojoLayout);
        mEditTextDescription = findViewById(R.id.editText_description_addDojoLayout);

        // Toolbar and features
        toolbar = findViewById(R.id.toolBar_topBarAddDojoLayout);
        mBackButton = findViewById(R.id.imgView_back_topBarAddDojoLayout);
        mAddButton = findViewById(R.id.txtView_add_topBarAddDojoLayout);

        // Layouts
        mRelativeLayoutContainer = findViewById(R.id.relLayout_container_addDojoLayout);

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
            case R.id.imgView_back_topBarAddDojoLayout:
                finish();
                break;
            case R.id.txtView_add_topBarAddDojoLayout:
                saveChanges();
                break;
            case R.id.button_chooseCoverPhoto_addDojoLayout:
                Intent intentShareActivity = new Intent(mContext, ShareActivity.class);
                intentShareActivity.putExtra(getString(R.string.photo_type), getString(R.string.photo_type_cover_photo_add));
                startActivity(intentShareActivity);
                break;
            /*================== Hide the keyboard if the user click outside ==================*/
            case R.id.txtView_chooseCoverPhoto_addDojoLayout: hideKeyBoard(); break;
            case R.id.txtView_coverPhotoPath_addDojoLayout: hideKeyBoard(); break;
            case R.id.relLayout_container_addDojoLayout: hideKeyBoard(); break;
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

    }

    /**
     * Life-cycle 3) This method runs when the activity become complete invisible.
     */
    @Override
    protected void onStop() {
        super.onStop();
    }


    /*======================================== END OF Firebase ========================================*/


}


