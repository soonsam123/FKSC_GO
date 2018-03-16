package com.example.karat.fksc.AddDojo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.models.User;
import com.example.karat.fksc.models.UserAboutMe;
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

    // Context
    private Context mContext = AddDojoActivity.this;

    // Vars

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dojo);
        Log.i(TAG, "onCreate: Stating the activity");

        setupFirebaseAuth();
        setupWidgets();
        setClickAndKeyListeners();
        setupToolBar();

    }

    /*======================================== Save Changes ========================================*/

    /*private void saveChanges(){
        Log.i(TAG, "saveChanges: starting");


        if (mAuth.getCurrentUser() != null) {

            // Get the text in the fields to compare with the ones in the database.
            final String name = mEditTextFullName.getText().toString();
            final String aboutMe = mEditTextAboutme.getText().toString();
            final String curriculum = mEditTextCurriculum.getText().toString();

            final String userID = mAuth.getCurrentUser().getUid();

            // Case 1 - None of the fields were changed. Just leave the activity.
            if (name.equals(user.getFull_name()) && aboutMe.equals(userAboutMe.getAbout_me())
                    && curriculum.equals(userAboutMe.getCurriculum())){
                finish();
            }
            // Case 2 - Fields were changed.
            else {

                myRef = FirebaseDatabase.getInstance().getReference();
                ValueEventListener listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Case 2.1 - Current User is already registered in users_about_me node.
                        if (dataSnapshot.child(mContext.getString(R.string.dbname_users_about_me))
                                .hasChild(userID)) {
                            Log.i(TAG, "onDataChange: USER REGISTERED in users_about_me node");

                            if (!name.equals(user.getFull_name())){
                                firebaseMethods.updateFullName(name);
                            }
                            if (!aboutMe.equals(userAboutMe.getAbout_me())){
                                firebaseMethods.updateAboutMe(aboutMe);
                            }
                            if (!curriculum.equals(userAboutMe.getCurriculum())){
                                firebaseMethods.updateCurriculum(curriculum);
                            }

                            finish();

                        }
                        // Case 2.2 - Current User is NOT registered in users_about_me node yet.
                        else {
                            Log.i(TAG, "onDataChange: Registering user to users_about_me node");

                            // Check if he changed the name and update if so.
                            if (!name.equals(user.getFull_name())){
                                firebaseMethods.updateFullName(name);
                            }

                            // Register this user to the users_about_me node.
                            firebaseMethods.addUserAboutMe("", aboutMe, curriculum);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };

                myRef.addListenerForSingleValueEvent(listener);
            }
        }

    }
*/
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
     * Set up the widgets with the Firebase Database values.
     */
    /*private void setupWidgetsWithDBValues(User user, UserAboutMe userAboutMe){

        // Display the Firebase database information to the EditText as Texts.
        // User --> fullName / UserAboutMe --> aboutMe, curriculum
        mEditTextFullName.setText(user.getFull_name());
        mEditTextAboutme.setText(userAboutMe.getAbout_me());
        mEditTextCurriculum.setText(userAboutMe.getCurriculum());



    }
*/
    /**
     * Set up the customized toolbar to be the one for this activity.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }

    /*======================================== END OF Setups ========================================*/


    /*======================================== Click and Key Listeners ========================================*/

    /**
     * Set up the OnClick and OnKey Listeners.
     */
    private void setClickAndKeyListeners(){

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
                /*saveChanges();*/
                Toast.makeText(mContext, R.string.dojo_add_successfuly, Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_chooseCoverPhoto_addDojoLayout:
                Toast.makeText(this, "Cover Photo", Toast.LENGTH_SHORT).show();
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
        firebaseMethods = new FirebaseMethods(getApplicationContext());

    }


    /**
     * Life-cycle 2) This method runs after the onCreate.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Starting");
        
        myRef = FirebaseDatabase.getInstance().getReference();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /*user = firebaseMethods.getUser(dataSnapshot);
                userAboutMe = firebaseMethods.getUserAboutMe(dataSnapshot);
                setupWidgetsWithDBValues(user, userAboutMe);*/

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
    }


    /*======================================== END OF Firebase ========================================*/


}


