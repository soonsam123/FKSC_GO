package com.example.karat.fksc.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karat.fksc.Login.LoginActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karat on 07/03/2018.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;


    /**
     * Receive the context from the activity we are working.
     * Creates the FirebaseAuth.
     * @param context
     */
    public FirebaseMethods(Context context) {

        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

    }


    /*============================================ Sign in and Sign up ============================================*/

    /**
     * Register a new user to the Firebase Authentication with his email and password.
     * @param email
     * @param password
     */
    public void registerNewEmail(String email, String password, final String fullName, final ProgressBar progressBar, final Intent intent){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "createUserWithEmail:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null){

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.i(TAG, "onComplete: Successful send verification email");
                                                    Toast.makeText(mContext, mContext.getString(R.string.confirm_email_verification)
                                                            + user.getEmail(), Toast.LENGTH_SHORT).show();

                                                    // 1) Add user's info to database;
                                                    // 2) Sign out to wait for the email verification;
                                                    // 3) Go to login activity.
                                                    addNewUser(fullName, "", "", "", "", false);

                                                    mAuth.signOut();
                                                    mContext.startActivity(intent);

                                                } else {
                                                    Log.i(TAG, "onComplete: Failed to send verification email");
                                                    Toast.makeText(mContext, mContext.getString(R.string.failed_send_verification_email),
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }

                            /*updateUI(user);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            /*updateUI(null);*/
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * Sign in a user that is already registered with his email and password.
     * @param email
     * @param password
     */
    public void signInWithEmail(String email, String password, final ProgressBar progressBar, final Intent intent){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {

                                try {
                                    if (user.isEmailVerified()) {
                                        Log.i(TAG, "onComplete: User logged in: " + user.getUid());

                                        mContext.startActivity(intent);

                                    } else {

                                        mAuth.signOut();

                                        Toast.makeText(mContext, R.string.confirm_email_inbox
                                                , Toast.LENGTH_LONG).show();

                                    }

                                } catch (Exception e){e.printStackTrace();}
                            }

                            /*updateUI(user);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                            /*updateUI(null);*/
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
    /*============================================ END OF Sign in and Sign up ============================================*/



    /*============================================ Database ============================================*/

    private void addNewUser(String fullName, String birthDate, String beltColor, String registrationNumber, String dojo, boolean verified) {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            String userID = currentUser.getUid();

            Log.i(TAG, "addNewUser: Adding new user database information: " + userID);

            // Creating the user model with the information provided.
            User user = new User(
                    fullName,
                    birthDate,
                    beltColor,
                    registrationNumber,
                    dojo,
                    userID,
                    verified);

            // Adding to the database
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .setValue(user);


        }

    }

    /*============================================ END OF Database ============================================*/


    /*============================================ Getters and Setters ============================================*/

    public User getUser(DataSnapshot dataSnapshot){

        User user = new User();

        for (DataSnapshot ds: dataSnapshot.getChildren()){

            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))){
                Log.i(TAG, "getUser: Getting user information: " + ds);

                try {

                    String userID = mAuth.getCurrentUser().getUid();

                    user.setFull_name(ds.child(userID)
                    .getValue(User.class)
                    .getFull_name());

                    user.setBirth_date(ds.child(userID)
                    .getValue(User.class)
                    .getBirth_date());

                    user.setBelt_color(ds.child(userID)
                    .getValue(User.class)
                    .getBelt_color());

                    user.setRegistration_number(ds.child(userID)
                    .getValue(User.class)
                    .getRegistration_number());

                    user.setDojo(ds.child(userID)
                    .getValue(User.class)
                    .getDojo());

                    user.setUser_id(ds.child(userID)
                    .getValue(User.class)
                    .getUser_id());

                    user.setVerified(ds.child(userID)
                    .getValue(User.class)
                    .isVerified());


                } catch (NullPointerException e){e.printStackTrace();}


            }

        }

        return user;

    }


    public List<User> getUsers(DataSnapshot dataSnapshot){

        List<User> all_users = new ArrayList<>();

        // From all the users ID, it gets one by one.
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            Log.i(TAG, "getUsers: " + ds);

            // Transform the value of the specific key to a User object and get its full name, birth_date...
            try {
                User user = new User();

                user.setFull_name(ds.getValue(User.class)
                        .getFull_name());
                user.setBirth_date(ds.getValue(User.class)
                        .getBirth_date());
                user.setBelt_color(ds.getValue(User.class)
                        .getBelt_color());
                user.setDojo(ds.getValue(User.class)
                        .getDojo());
                user.setUser_id(ds.getValue(User.class)
                        .getUser_id());
                user.setRegistration_number(ds.getValue(User.class)
                        .getRegistration_number());
                user.setVerified(ds.getValue(User.class)
                        .isVerified());

                Log.i(TAG, "getUsers: " + user.toString());
                all_users.add(user);
                Log.i(TAG, "getUsers: " + all_users.toString());

            }catch (NullPointerException e){e.printStackTrace();}

        }


        Log.i(TAG, "getUsers: " + all_users.toString());
        return all_users;

    }

    /*============================================ END OF Getters and Setters ============================================*/

}
