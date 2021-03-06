package soon.semicontato.karat.fksc.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import soon.semicontato.karat.fksc.members.MembersActivity;
import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.FirebaseMethods;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by karat on 07/03/2018.
 */

public class LoginFragment extends Fragment implements View.OnKeyListener {

    // TAG
    private static final String TAG = "LoginFragment";

    // Layout
    private AppCompatEditText mEmail;
    private AppCompatEditText mPassword;
    private AppCompatButton mLoginButton;
    private ProgressBar mProgressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting the fragment");
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        setupFirebaseAuth();
        setupWidgets(view);
        setupClickListeners();
        init();

        mAuth.signOut();

        return view;
    }


    /*============================================ Init ============================================*/

    /**
     * When the user PRESS the log in button.
     */
    private void init(){
        Log.i(TAG, "init: Initializing the button");

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if ( stringIsNull(email)|| stringIsNull(password)){
                    Toast.makeText(getActivity(), R.string.fillin_all_fields, Toast.LENGTH_SHORT).show();
                } else {

                    firebaseMethods = new FirebaseMethods(getActivity());

                    Intent intentMembers = new Intent(getActivity(), MembersActivity.class);
                    intentMembers.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    firebaseMethods.signInWithEmail(email, password, mProgressBar, intentMembers);

                    // If user is really logged in. Finish the login activity so he can not come back
                    // by pressing the back button.
                    if (mAuth.getCurrentUser() != null){
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                    }
                }
            }
        });

    }
    /*============================================ END OF Init ============================================*/



    /*================================= Setups =================================*/
    /**
     * Set up the widgets to the layout values.
     * @param view get the activity view to be able to access the activity's widgets
     *             from inside the fragment
     */
    private void setupWidgets(View view){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        mEmail = view.findViewById(R.id.appCompatEditText_email_fragmentLogin);
        mPassword = view.findViewById(R.id.appCompatEditText_password_fragmentLogin);
        mLoginButton = view.findViewById(R.id.appCompatButton_login_fragmentLogin);

        mProgressBar = view.findViewById(R.id.progressBar_fragmentLogin);
        mProgressBar.setVisibility(View.GONE);

    }


    /**
     * Set up all the CLICK and KEY listeners.
     */
    private void setupClickListeners(){

        mPassword.setOnKeyListener(this);

    }
    /*================================= END OF Setups =================================*/


    /*================================= Keys and Clicks =================================*/
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (view.getId() == R.id.appCompatEditText_password_fragmentLogin){
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                mLoginButton.performClick();
            }
        }

        return false;
    }
    /*================================= END OF Keys and Clicks =================================*/



    /*================================= Helpers =================================*/

    /**
     * Returns true if the string is null, otherwise returns false.
     * @param string any string (block of letters)
     * @return "true" for null string or "false" for not null string
     */
    public boolean stringIsNull(String string){
        return string.equals("");
    }

    /*================================= END OF Helpers =================================*/


    /*============================= Firebase =============================*/

    /**
     * Set up the authentications to FirebaseAuth.
     */
    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up Firebase Auth");

        mAuth = FirebaseAuth.getInstance();
        
        if (mAuth.getCurrentUser() != null){
            Log.i(TAG, "setupFirebaseAuth: User is loggend in: " + mAuth.getCurrentUser().getUid());
        } else {
            Log.i(TAG, "setupFirebaseAuth: User is signed out");
        }

    }


    /**
     * Two methods bellow necessary to Firebase work.
     */
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*============================= END OF Firebase =============================*/

}
