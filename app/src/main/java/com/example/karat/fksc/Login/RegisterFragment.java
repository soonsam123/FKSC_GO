package com.example.karat.fksc.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karat.fksc.Members.MembersActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**x
 * Created by karat on 07/03/2018.
 */

public class RegisterFragment extends Fragment implements View.OnKeyListener {

    // TAG
    private static final String TAG = "RegisterFragment";

    // Layout
    private AppCompatEditText mEmail;
    private AppCompatEditText mFullName;
    private AppCompatEditText mPassword;
    private AppCompatButton mRegisterButton;
    private ProgressBar mProgressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;

    // Variables
    private String fullName;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting the fragment");
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        setupFirebaseAuth();
        setupWidgets(view);
        setupClickListeners();
        init();

        return view;
    }


    /*============================================ Init ============================================*/
    /**
     * When the user press the REGISTER BUTTON.
     */
    private void init(){
        Log.i(TAG, "init: Initializing the button click");

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString();
                fullName = mFullName.getText().toString();
                String password = mPassword.getText().toString();

                if (stringIsNull(email) || stringIsNull(fullName) || stringIsNull(password)){
                    Toast.makeText(getActivity(), R.string.fillin_all_fields, Toast.LENGTH_SHORT).show();
                } else {

                    firebaseMethods = new FirebaseMethods(getActivity());

                    /*
                     * 1) Create an intent to the same Login Screen;
                     * 2) Register email (Send verification email, if OK start Login Screen again and send message to verify the email).
                     */
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);

                    firebaseMethods.registerNewEmail(email, password, fullName, mProgressBar, intentLogin);

                }

            }
        });

    }
    /*============================================ END OF Init ============================================*/




    /*==================================== Setups ====================================*/
    /**
     * Set up the widgets to the layout values.
     */
    private void setupWidgets(View view){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        mEmail = view.findViewById(R.id.appCompatEditText_email_fragmentRegister);
        mFullName = view.findViewById(R.id.appCompatEditText_fullname_fragmentRegister);
        mPassword = view.findViewById(R.id.appCompatEditText_password_fragmentRegister);
        mRegisterButton = view.findViewById(R.id.appCompatButton_register_fragmentRegister);

        mProgressBar = view.findViewById(R.id.progressBar_fragmentRegister);
        mProgressBar.setVisibility(View.GONE);

    }

    /**
     * Set up all the CLICK and KEY listeners.
     */
    private void setupClickListeners(){

        mPassword.setOnKeyListener(this);

    }

    /*==================================== END OF Setups ====================================*/



    /*==================================== Keys and Clicks ====================================*/
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (view.getId() == R.id.appCompatEditText_password_fragmentRegister){
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                mRegisterButton.performClick();
            }
        }

        return false;
    }
    /*==================================== END OF Keys and Clicks ====================================*/


    /*================================= Helpers =================================*/

    /**
     * Returns true if the string is null, otherwise returns false.
     * @param string
     * @return
     */
    public boolean stringIsNull(String string){
        if (string.equals("")){
            return true;
        } else {
            return false;
        }
    }
    /*================================= END OF Helpers =================================*/



    /*==================================== Firebase ====================================*/

    /**
     * Set up the Firebase Authentication (Login and Register)
     */
    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up the Firebase Authentication");

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called with the initial value and again
                // whenever any values is changed in this fragment.


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // This methos are necessary in order to Firebase to work.
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*==================================== END OF Firebase ====================================*/






}
