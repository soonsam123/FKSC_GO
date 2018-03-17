package com.example.karat.fksc.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karat.fksc.Login.LoginActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.models.DojoInfo;
import com.example.karat.fksc.models.DojoSettings;
import com.example.karat.fksc.models.User;
import com.example.karat.fksc.models.UserAboutMe;
import com.example.karat.fksc.models.UserAndUserSettings;
import com.example.karat.fksc.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    private String userID;


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
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

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
                                                    addNewUser(fullName, "", "", "", "", "", false);

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

    private void addNewUser(String fullName, String birthDate, String beltColor, String registrationNumber, String dojo, String profileImgURL, boolean verified) {

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            String userID = currentUser.getUid();

            Log.i(TAG, "addNewUser: Adding new user database information: " + userID);


            // Creating the user model with the information provided.
            User user = new User(
                    dojo,
                    fullName,
                    profileImgURL,
                    registrationNumber,
                    verified);

            // Adding to the database;
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .setValue(user);

            // Creating the user_settings model with the information provided.
            UserSettings user_settings = new UserSettings(
                    beltColor,
                    birthDate,
                    userID);

            // Adding to the database
            myRef.child(mContext.getString(R.string.dbname_users_settings))
                    .child(userID)
                    .setValue(user_settings);


        }

    }


    /**
     * Add the node users_about_me to the user Firebase Database.
     * This node contains: cover_img_url, about_me, curriculum.
     * @param coverPhotoImgURL
     * @param aboutMe
     * @param curriculum
     */
    public void addUserAboutMe(String coverPhotoImgURL, String aboutMe, String curriculum){

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            Log.i(TAG, "addUserAboutMe: Adding node users_about_me");

            String userID = currentUser.getUid();

            // Create the model UserAboutMe.
            UserAboutMe userAboutMe = new UserAboutMe(
                    coverPhotoImgURL,
                    aboutMe,
                    curriculum);

            // Add it to: appDatabase -->
            //                      users_about_me -->
            //                                  userID -->
            //                                      cover_img_url = coverPhotoImgURL;
            //                                      about_me = aboutMe
            //                                      curriculum = curriculum
            myRef.child(mContext.getString(R.string.dbname_users_about_me))
                    .child(userID)
                    .setValue(userAboutMe);

        }

    }


    /**
     * Add the node dojos_info and dojos_settings to the user Firebase Database.
     * This node contains the attributes below.
     * @param name
     * @param city
     * @param coverImgURL
     * @param registrationNumber
     * @param verified
     * @param address
     * @param telephone
     * @param description
     * @param secretName
     */
    public void addDojo(String name, String city, String coverImgURL, String registrationNumber
            , boolean verified, String address, String telephone, String description
            , String secretName, int imageCount){


        Toast.makeText(mContext, R.string.we_are_adding_your_dojo, Toast.LENGTH_SHORT).show();

        if (mAuth.getCurrentUser() != null){

            String userID = mAuth.getCurrentUser().getUid();
            Log.i(TAG, "addDojo: Adding a Dojo");

            // Create the model with the given values.
            DojoInfo dojoInfo = new DojoInfo(
                    name,
                    city,
                    coverImgURL,
                    registrationNumber,
                    verified);

            // Create the node: fkscDataBase -->
            //                              dojos_info -->
            //                                          userID -->
            //                                              (name,city,cover_img_url,registration_number,verified)
            myRef.child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .child(mContext.getString(R.string.dbname_dojo)+ (imageCount + 1))
                    .setValue(dojoInfo);


            // Create the model with the given values.
            DojoSettings dojoSettings = new DojoSettings(
                    address,
                    telephone,
                    description,
                    secretName,
                    userID);

            // Create the node: fkscDataBase -->
            //                              dojos_settings -->
            //                                          userID -->
            //                                              (address,telephone,description,secret_name,user_id)
            myRef.child(mContext.getString(R.string.dbname_dojos_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.dbname_dojo)+ (imageCount + 1))
                    .setValue(dojoSettings);

            Toast.makeText(mContext, R.string.dojo_add_successfuly, Toast.LENGTH_SHORT).show();
        }


    }

    /*============================================ END OF Database ============================================*/


    /*========================================************ Getters and Setters ************======================================*/



    /*============================================ users node ============================================*/
    /**
     * Get the user node information from one user:
     * users --> userID --> dojo, full_name, profile_img_url, registration_number, verified.
     * @param dataSnapshot
     * @return
     */
    public User getUser(DataSnapshot dataSnapshot){

        User user = new User();

        if (mAuth.getCurrentUser() != null) {

            String userID = mAuth.getCurrentUser().getUid();

            // 1) Looping through all the user's UID in users.
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_users))
                    .getChildren()) {
                Log.d(TAG, "getUser: " + ds);

                // 2) Checking if one of the IDs is the current user's one.
                if (ds.getKey().equals(userID)) {
                    user = ds.getValue(User.class);
                }

            }
        }

        return user;

    }


    /**
     * Get the users information: dojo, full_name, profile_img_url, registration_number, verified.
     * @param dataSnapshot
     * @return
     */
    public List<User> getUsers(DataSnapshot dataSnapshot){
        List<User> all_users = new ArrayList<>();

        // From all the users ID, it gets one by one.
        for (DataSnapshot ds: dataSnapshot.getChildren()){
            Log.i(TAG, "getUsers: " + ds);

            // Transform the value of the specific key to a User object and get its full name, birth_date...
            try {

                User user = new User();
                user = ds.getValue(User.class);

                Log.i(TAG, "getUsers: " + user.toString());
                all_users.add(user);
                Log.i(TAG, "getUsers: " + all_users.toString());


            }catch (NullPointerException e){e.printStackTrace();}

        }


        Log.i(TAG, "getUsers: " + all_users.toString());
        return all_users;

    }


    /*============================================ users node ============================================*/


    /*============================================ users_settings node ============================================*/

    /**
     * Get the user_settings information: belt_color, birth_date, user_id.
     * @param dataSnapshot
     * @return
     */
    public List<UserSettings> getUsersSettings(DataSnapshot dataSnapshot){

        List<UserSettings> all_users_settings = new ArrayList<>();

        for (DataSnapshot ds: dataSnapshot.getChildren()){
            Log.i(TAG, "getUsersSettings: " + ds);

            try {

                UserSettings userSettings = new UserSettings();
                userSettings = ds.getValue(UserSettings.class);

                Log.i(TAG, "getUsersSettings: Single element being added" + userSettings.toString());
                all_users_settings.add(userSettings);
                Log.i(TAG, "getUsersSettings: Building the List" + all_users_settings.toString());

            } catch (NullPointerException e){e.printStackTrace();}

        }

        Log.i(TAG, "getUsersSettings: Complete List" + all_users_settings.toString());

        return all_users_settings;

    }


    /**
     * Get the users and users_settings information from the current user.
     * @param dataSnapshot
     * @return
     */
    public UserAndUserSettings getUserAndUserSettings(DataSnapshot dataSnapshot){

        if (mAuth.getCurrentUser() != null) {

            String userID = mAuth.getCurrentUser().getUid();

            User user = new User();
            UserSettings userSettings = new UserSettings();

            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Log.i(TAG, "getUserAndUserSettings: Looping through ds: " + ds);

                if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                    Log.i(TAG, "getUserAndUserSettings: User " + ds.child(userID).getValue(User.class));

                    user = ds.child(userID).getValue(User.class);
                } else if (ds.getKey().equals(mContext.getString(R.string.dbname_users_settings))){
                    Log.i(TAG, "getUserAndUserSettings: UserSettings " + ds.child(userID).getValue(UserSettings.class));

                    userSettings = ds.child(userID).getValue(UserSettings.class);
                }

            }

            return new UserAndUserSettings(user, userSettings);

        } else {
            return null;
        }
    }

    /**
     * This method retrieves a List with all users and users_settings. As UserAndUserSettings(User, UserSettings).
     * @param dataSnapshot
     * @return
     */
    public List<UserAndUserSettings> getAllUserAndUserSettings(DataSnapshot dataSnapshot){

        List<UserAndUserSettings> all_usersAndUserSettings = new ArrayList<>();
        List<User> all_users = new ArrayList<>();
        List<UserSettings> all_users_settings = new ArrayList<>();

        // users node.
        for (DataSnapshot ds: dataSnapshot.child(mContext.getString(R.string.dbname_users))
                .getChildren()){
            Log.i(TAG, "getAllUserAndUserSettings: Looping through users " + ds);

            try {

                User user = new User();

                Log.i(TAG, "getAllUserAndUserSettings: User one" + ds.getValue(User.class));
                user = ds.getValue(User.class);

                all_users.add(user);

            } catch (NullPointerException e){e.printStackTrace();}

        }

        // users_settings node.
        for (DataSnapshot ds: dataSnapshot.child(mContext.getString(R.string.dbname_users_settings))
                .getChildren()){
            Log.i(TAG, "getAllUserAndUserSettings: Looping through users_settings " + ds);

            try {

                UserSettings userSettings = new UserSettings();

                Log.i(TAG, "getAllUserAndUserSettings: UserSettings one" + ds.getValue(UserSettings.class));
                userSettings = ds.getValue(UserSettings.class);

                all_users_settings.add(userSettings);

            } catch (NullPointerException e){e.printStackTrace();}

        }

        for (int i = 0; i < all_users.size(); i++){

            all_usersAndUserSettings.add(new UserAndUserSettings(all_users.get(i), all_users_settings.get(i)));

        }

        return all_usersAndUserSettings;

    }


    /*============================================ users_settings node ============================================*/


    /*============================================ users_about_me node ============================================*/

    /**
     * Retrieve the users_about_me node information from the current user.
     * users_about_me --> userID --> cover_img_url, about_me, curriculum.
     * @param dataSnapshot
     * @return
     */
    public UserAboutMe getUserAboutMe(DataSnapshot dataSnapshot){

        UserAboutMe userAboutMe = new UserAboutMe();

        if (mAuth.getCurrentUser() != null){

            String userID = mAuth.getCurrentUser().getUid();

            // 1) Looping through all the user's UID in users_about_me node.
            for (DataSnapshot ds: dataSnapshot
                    .child(mContext.getString(R.string.dbname_users_about_me))
                    .getChildren()){

                // 2) Checking if one of the IDs is the current user's one.
                if (ds.getKey().equals(userID)) {
                    userAboutMe = ds.getValue(UserAboutMe.class);
                }

            }

        }

        return userAboutMe;

    }

    /*============================================ END OF users_about_me node ============================================*/


    /*============================================ dojos_info node ============================================*/

    /**
     * Get the dojo_info node information from one user:
     * dojos_info --> userID --> name, city, cover_img_url, registration_number, verified.
     * @param dataSnapshot
     * @return
     */
    public DojoInfo getDojoInfo(DataSnapshot dataSnapshot){

        DojoInfo dojoInfo = new DojoInfo();

        if (mAuth.getCurrentUser() != null) {

            String userID = mAuth.getCurrentUser().getUid();

            // 1) Looping through all the user's UID in dojos_info node.
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_info))
                    .getChildren()) {
                Log.d(TAG, "getDojoInfo: " + ds);

                // 2) Checking if one of the IDs is the current user's one.
                if (ds.getKey().equals(userID)) {
                    dojoInfo = ds.getValue(DojoInfo.class);
                }

            }
        }

        return dojoInfo;

    }


    /**
     * Retrieve a list of all the registered DojoInfo.
     * @param dataSnapshot
     * @return
     */
    public List<DojoInfo> getAllDojosInfo(DataSnapshot dataSnapshot){

        List<DojoInfo> all_dojos_info = new ArrayList<>();

        // fkscDatabase --> dojos_info --> Loop through the (user's ID)
        for (DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_dojos_info))
                .getChildren()){

            // Loop through the children in userID, which are all the dojos of each user.
            // userID_1 --> (dojo1, dojo2, dojo3) and userID_2 --> (dojo1, dojo2)...
            for (DataSnapshot ds2: ds.getChildren()){

                DojoInfo sampleDojo = new DojoInfo();
                sampleDojo = ds2.getValue(DojoInfo.class);
                all_dojos_info.add(sampleDojo);

            }

        }

        return all_dojos_info;

    }

    /**
     * Get the number of dojos the user has.
     * @param dataSnapshot
     * @return
     */
    public int getDojoCount(DataSnapshot dataSnapshot){

        int count = 0;

        if (mAuth.getCurrentUser() != null){

            String userID = mAuth.getCurrentUser().getUid();

            // path: fkscDatabase --> dojos_info --> userID --> (dojo1, dojo2, dojo3)
            for (DataSnapshot ds: dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .getChildren()){
                count++;
            }

        }

        return count;

    }

    /*============================================ END OF dojos_info node ============================================*/


    /*============================================ dojos_settings node ============================================*/

    /**
     * Get the dojo_settings node information from one user:
     * dojos_settings --> userID --> address, telephone, description, secret_name, user_id.
     * @param dataSnapshot
     * @return
     */
    public DojoSettings getDojoSettings(DataSnapshot dataSnapshot){

        DojoSettings dojoSettings = new DojoSettings();

        if (mAuth.getCurrentUser() != null){

            String userID = mAuth.getCurrentUser().getUid();

            // 1) Looping through all the user's UID in dojos_settings node.
            for (DataSnapshot ds: dataSnapshot
                    .child(mContext.getString(R.string.dbname_users_settings))
                    .getChildren()){
                Log.d(TAG, "getDojoSettings: " + ds);

                // 2) Checking if one of the IDs is the current user's one.
                if (ds.getKey().equals(userID)){
                    dojoSettings = ds.getValue(DojoSettings.class);
                }

            }

        }


        return dojoSettings;

    }

    /*============================================ END OF dojos_settings node ============================================*/


    /*=================================************ END OF Getters and Setters ************===============================*/



    /*=================================================== UPDATES ===================================================*/

    /*======================================= Update users =======================================*/
    /**
     * Updates the dojo Database value to
     * @param dojo
     */
    public void updateDojo(String dojo){
        Log.d(TAG, "updateDojo: Updating dojo to: " + dojo);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_dojo))
                .setValue(dojo);

    }


    /**
     * Updates the Full Name Database value to
     * @param fullName
     */
    public void updateFullName(String fullName){
        Log.d(TAG, "updateFullName: Updating full name to: " + fullName);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_full_name))
                .setValue(fullName);

    }


    /**
     * Updates the Profile Image Database value to
     * @param profileImgURL
     */
    public void updateProfileImgURL(String profileImgURL){
        Log.d(TAG, "updateProfileImgURL: Updating Profile Image to: " + profileImgURL);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_profile_img_url))
                .setValue(profileImgURL);

    }


    /**
     * Updates the Registration Number Database value to
     * @param registrationNumber
     */
    public void updateRegistrationNumber(String registrationNumber){
        Log.d(TAG, "updateRegistrationNumber: Updating Registration Number to: " + registrationNumber);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_registration_number))
                .setValue(registrationNumber);

    }


    /**
     * Updates the Verified Database value to
     * @param verified
     */
    public void updateVerified(boolean verified){
        Log.d(TAG, "updateVerified: Updating Verified to: " + verified);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_verified))
                .setValue(verified);

    }

    /*======================================= END OF Update users =======================================*/

    /*======================================= Update users_about_me =======================================*/
    /**
     * Updates the AboutMe Database value to
     * @param aboutMe
     */
    public void updateAboutMe(String aboutMe){
        Log.d(TAG, "updateAboutMe: Updating Verified to: " + aboutMe);

        myRef.child(mContext.getString(R.string.dbname_users_about_me))
                .child(userID)
                .child(mContext.getString(R.string.field_about_me))
                .setValue(aboutMe);

    }


    /**
     * Updates the Cover Image Database value to
     * @param coverImgURL
     */
    public void updateCoverImgURL(String coverImgURL){
        Log.d(TAG, "updateCoverImgURL: Updating Cover Image to: " + coverImgURL);

        myRef.child(mContext.getString(R.string.dbname_users_about_me))
                .child(userID)
                .child(mContext.getString(R.string.field_cover_img_url))
                .setValue(coverImgURL);

    }



    /**
     * Updates the Curriculum Database value to
     * @param curriculum
     */
    public void updateCurriculum(String curriculum){
        Log.d(TAG, "updateCurriculum: Updating Curriculum to: " + curriculum);

        myRef.child(mContext.getString(R.string.dbname_users_about_me))
                .child(userID)
                .child(mContext.getString(R.string.field_curriculum))
                .setValue(curriculum);

    }
    /*======================================= END OF Update users_about_me =======================================*/

    /*======================================= Update users_settings =======================================*/

    /**
     * Updates the Belt Color Database value to
     * @param beltColor
     */
    public void updateBeltColor(String beltColor){
        Log.d(TAG, "updateBeltColor: Updating Belt Color to: " + beltColor);

        myRef.child(mContext.getString(R.string.dbname_users_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_belt_color))
                .setValue(beltColor);

    }


    /**
     * Updates the Birth Date Database value to
     * @param birthDate
     */
    public void updateBirthDate(String birthDate){
        Log.d(TAG, "updateBirthDate: Updating Birth Date to: " + birthDate);

        myRef.child(mContext.getString(R.string.dbname_users_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_birth_date))
                .setValue(birthDate);

    }
    /*======================================= END OF Update users_settings =======================================*/


    /*=================================================== END OF UPDATES ===================================================*/

}
