package soon.semicontato.karat.fksc.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import soon.semicontato.karat.fksc.login.LoginActivity;
import soon.semicontato.karat.fksc.profile.ProfileActivity;
import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.models.ChampionshipInfo;
import soon.semicontato.karat.fksc.models.CoverPhoto;
import soon.semicontato.karat.fksc.models.DojoInfo;
import soon.semicontato.karat.fksc.models.DojoInfoAndSettings;
import soon.semicontato.karat.fksc.models.DojoSettings;
import soon.semicontato.karat.fksc.models.ProfilePhoto;
import soon.semicontato.karat.fksc.models.User;
import soon.semicontato.karat.fksc.models.UserAboutMe;
import soon.semicontato.karat.fksc.models.UserAndUserSettings;
import soon.semicontato.karat.fksc.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import soon.semicontato.karat.fksc.login.LoginActivity;
import soon.semicontato.karat.fksc.models.ChampionshipInfo;
import soon.semicontato.karat.fksc.models.CoverPhoto;
import soon.semicontato.karat.fksc.models.DojoInfo;
import soon.semicontato.karat.fksc.models.DojoInfoAndSettings;
import soon.semicontato.karat.fksc.models.DojoSettings;
import soon.semicontato.karat.fksc.models.ProfilePhoto;
import soon.semicontato.karat.fksc.models.User;
import soon.semicontato.karat.fksc.models.UserAboutMe;
import soon.semicontato.karat.fksc.models.UserAndUserSettings;
import soon.semicontato.karat.fksc.models.UserSettings;

/**
 * Created by karat on 07/03/2018.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";

    private Context mContext;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private StorageReference mStorageReference;


    private String userID;

    private double mPhotoProgress;


    /**
     * Receive the context from the activity we are working.
     * Creates the FirebaseAuth.
     * @param context the activity's context
     */
    public FirebaseMethods(Context context) {

        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }

    }


    /*============================================ Sign in and Sign up ============================================*/

    /**
     * Register a new user to the Firebase Authentication with his email and password.
     * @param email the user's email for registering
     * @param password the user's password for registering
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
                            if (task.getException() != null) {
                                Toast.makeText(mContext, "Authentication failed." + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            /*updateUI(null);*/
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * Sign in a user that is already registered with his email and password.
     * @param email the user's email
     * @param password the user's password
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

                            if (task.getException() != null) {
                                Toast.makeText(mContext, "Authentication failed." + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                            /*updateUI(null);*/
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });

    }
    /*============================================ END OF Sign in and Sign up ============================================*/



    /*============================================ Database ============================================*/

    /*============================================ Add ============================================*/

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

            // 1) Added the information for the user that just registered;
            // 2) Sign out to wait for email verification.
            mAuth.signOut();

        }

    }


    /**
     * Add the node users_about_me to the user Firebase Database.
     * This node contains: cover_img_url, about_me, curriculum.
     * @param coverPhotoImgURL The link of the user's Cover Photo, it'll be null for now
     *                         and all the users will have a single image which is in the android:src=""
     *                         of the Cover ImageView in XML Layout.
     * @param aboutMe a string where the user will write about him
     * @param curriculum a string where the user will write about what he already won in the Championships
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
     * @param name name of the dojo (gym / place to train karate)
     * @param city the city of the dojo
     * @param coverImgURL the Cover Image of the dojo
     * @param registrationNumber the Registration Number of the dojo, it'll be null when creating the dojo
     *                           and then I'll add it in the database only for the users who paied the service
     *                           for our Office.
     * @param verified boolen that says if the user is verified or not, by default, all the users are set to false
     *                then the user must pay a fee for our Office and we will change it to true in the Firebase database.
     *
     * @param address the address of the dojo
     * @param telephone the telephone of the dojo
     * @param description the description of the dojo
     * @param secretName the Secret Name of the dojo, this is a name that only we have the access, so we can refer to the
     *                   dojos by using this name, because the other one can be changed when we don't know this happened.
     */
    public void addDojo(String name, String city, String coverImgURL, String registrationNumber
            , boolean verified, String address, String telephone, String description
            , String secretName, final int nextDojoNumber){

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
                    .child(mContext.getString(R.string.dbname_dojo)+ nextDojoNumber)
                    .setValue(dojoInfo);


            // Create the model with the given values.
            DojoSettings dojoSettings = new DojoSettings(
                    address,
                    telephone,
                    description,
                    secretName,
                    userID,
                    mContext.getString(R.string.dbname_dojo)+ nextDojoNumber);

            // Create the node: fkscDataBase -->
            //                              dojos_settings -->
            //                                          userID -->
            //                                              (address,telephone,description,secret_name,user_id)
            myRef.child(mContext.getString(R.string.dbname_dojos_settings))
                    .child(userID)
                    .child(mContext.getString(R.string.dbname_dojo)+ nextDojoNumber)
                    .setValue(dojoSettings);

            Toast.makeText(mContext, R.string.dojo_add_successfuly, Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * Add the profile photo to "photos --> user_id --> profile_photo --> (profile_img_url = "firebase/...")
     * @param profile_img_url the Profile Photo URL
     */
    private void addProfilePhoto(String profile_img_url){
        Log.i(TAG, "addProfilePhoto: Adding profile photo to profile_photos node");

        ProfilePhoto profilePhoto = new ProfilePhoto(profile_img_url);

        if (mAuth.getCurrentUser() != null) {

            String user_id = mAuth.getCurrentUser().getUid();

            myRef.child(mContext.getString(R.string.dbname_photos))
                    .child(user_id)
                    .child(mContext.getString(R.string.photo_type_profile))
                    .setValue(profile_img_url);

        }

    }


    /**
     * Add a Dojo's COVER PHOTO into the "photos" node.
     * @param cover_img_url the Cover Image URL for the dojos
     * @param imageCount the number that the cover_photo will assign, for example "cover_photo8"
     *                   or "cover_photo10" and so on
     */
    private void addCoverPhoto(String cover_img_url, int imageCount){

        CoverPhoto coverPhoto = new CoverPhoto(cover_img_url);

        if (mAuth.getCurrentUser() != null){

            String user_id = mAuth.getCurrentUser().getUid();

            myRef.child(mContext.getString(R.string.dbname_photos))
                    .child(user_id)
                    .child(mContext.getString(R.string.photo_type_cover) + imageCount)
                    .setValue(coverPhoto);

        }

    }



    /*============================================ END OF Add ============================================*/


    /*============================================ Remove ============================================*/

    /**
     * Remove a dojo from the current user.
     * Receive the dojo_number to know what dojo of the user will be deleted.
     * @param dojo_number the dojo and the number of the dojo, for example, "dojo1" or "dojo2" and so on
     */
    public void removeDojo(String dojo_number){
        // dojo_number = dojo1 || dojo2 || dojo3 || dojo4

        if (mAuth.getCurrentUser() != null){

            String userID = mAuth.getCurrentUser().getUid();

            // 1) **************** Remove "dojo_info" node. ****************
            myRef.child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .child(dojo_number)
                    .removeValue();

            // 2) **************** Remove "dojo_settings" node. ****************
            myRef.child(mContext.getString(R.string.dbname_dojos_settings))
                    .child(userID)
                    .child(dojo_number)
                    .removeValue();

            // 3) **************** Remove "photos" node cover photo. ****************
            // Number --> The number of this dojo
            // E.G = dojo3 --> "3"
            int index = dojo_number.lastIndexOf("o");
            String Number = dojo_number.substring(index + 1);

            // photos --> user_id --> cover_photo3 --> Remove Value.
            myRef.child(mContext.getString(R.string.dbname_photos))
                    .child(userID)
                    .child(mContext.getString(R.string.photo_type_cover) + Number)
                    .removeValue();

            // 4) **************** Remove "cover_photo" from Firebase Storage. ****************
            FilePaths filePaths = new FilePaths();
            mStorageReference.child(filePaths.FIREBASE_STORAGE_PATH + "/"
                    + userID + "/" + mContext.getString(R.string.photo_type_cover) + Number).delete();

        }

        Toast.makeText(mContext, R.string.dojo_deleted, Toast.LENGTH_SHORT).show();

    }

    /*============================================ END OF Remove ============================================*/

    /*============================================ END OF Database ============================================*/


    /*========================================************ Getters and Setters ************======================================*/



    /*============================================ users node ============================================*/
    /**
     * Get the user node information from one user:
     * users --> userID --> dojo, full_name, profile_img_url, registration_number, verified.
     * @param dataSnapshot a snapshot of the database
     * @return a User object from the current logged in user
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
     * @param dataSnapshot a snapshot of the database
     * @return a list with all Users object
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
     * @param dataSnapshot  a snapshot of the database
     * @return a list with all the "user_settings" node information from all users registered in the app
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
     * @param dataSnapshot a snapshot of the database
     * @return the "users" and "users_settings" node information from the current logged in user
     */
    public UserAndUserSettings getUserAndUserSettings(DataSnapshot dataSnapshot){

        if (mAuth.getCurrentUser() != null) {

            String userID = mAuth.getCurrentUser().getUid();

            User user = new User();
            UserSettings userSettings = new UserSettings();

            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Log.i(TAG, "getUserAndUserSettings: Looping through ds: " + ds);

                if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                    Log.i(TAG, "getUserAndUserSettings: 1) userID: " + userID);
                    Log.i(TAG, "getUserAndUserSettings: 2) Child of users" + ds.child(userID).toString());
                    Log.i(TAG, "getUserAndUserSettings: 3) User " + ds.child(userID).getValue(User.class));

                    user = ds.child(userID).getValue(User.class);
                } else if (ds.getKey().equals(mContext.getString(R.string.dbname_users_settings))){
                    Log.i(TAG, "getUserAndUserSettings: userID: " + userID);
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
     * * Get the users and users_settings information from a specific userID given as a parameter.
     * @param dataSnapshot a snapshot of the database
     * @param userID the ID of the user we want to get this information
     * @return the "users" and "users_settings" node information from a specific user by passing the userID.
     */
    public UserAndUserSettings getUserAndUserSettings(DataSnapshot dataSnapshot, String userID){

        if (mAuth.getCurrentUser() != null) {

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
     * @param dataSnapshot a snapshot of the database
     * @return a list with all the "users" and "users_settings" node information from all the user's registered in the app
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

                // When I register a user it adds the "users" node with 5 information
                // (dojo, full_name, profile_img_url, registration_number, verified)
                // and the "users_settings" node with 3 information
                // (birth_date, belt_color, user_id).
                // There were some users that when the account was being created it was adding only
                // the "users" node with one information (full_name) and it was not adding the "users_settings" node information.
                // I could not figure it out why this was happening because it was happening just a one time each 30 or 40
                // and it was never with me.
                // So I check the "users" node and I see if there are all the fields (dojo, full_name...)
                // If there is it means the data was added correctly, if there is not it means it happened this mysterious case
                // and so I'll not even consider this user. Then when the user report me I fix his problem and when this bug
                // happen with me I'll be able to fix it.
                if (ds.hasChild(mContext.getString(R.string.field_dojo)) &&
                        ds.hasChild(mContext.getString(R.string.field_profile_img_url)) &&
                        ds.hasChild(mContext.getString(R.string.field_registration_number)) &&
                        ds.hasChild(mContext.getString(R.string.field_full_name))){

                    Log.i(TAG, "getAllUserAndUserSettings: User one" + ds.getValue(User.class));
                    user = ds.getValue(User.class);

                    all_users.add(user);
                }

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
     * @param dataSnapshot a snapshot of the database
     * @return the "users_about_me" node information from the current logged in user
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


    /**
     * Retrieve the users_about_me node information from the user I passed the userID as a parameter.
     * users_about_me --> userID --> cover_img_url, about_me, curriculum.
     * @param dataSnapshot a snapshot of the database
     * @param userID the ID of the user we want to get this information
     * @return the "users_about_me" node information from a specific user by passing its ID
     */
    public UserAboutMe getUserAboutMe(DataSnapshot dataSnapshot, String userID){

        UserAboutMe userAboutMe = new UserAboutMe();

        if (mAuth.getCurrentUser() != null){

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
     * * Get all the dojos from the current user:
     * dojos_info --> userID --> dojo1 (info), dojo2 (info)...
     * @param dataSnapshot a snapshot of the database
     * @return a list with all the "dojos_info" node information from the current logged in user
     */
    public List<DojoInfo> getDojosInfoFromOneUser(DataSnapshot dataSnapshot){

        List<DojoInfo> all_dojos_info_fromOneUser = new ArrayList<>();

        DojoInfo sampleDojo = new DojoInfo();

        if (mAuth.getCurrentUser() != null) {

            String userID = mAuth.getCurrentUser().getUid();

            // 1) Looping through all the dojos from the current user.
            // fkscDatabase --> dojos_info --> userID --> dojo1, dojo2, dojo3...
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .getChildren()) {
                Log.d(TAG, "getDojosInfoFromOneUser: " + ds);

                sampleDojo = ds.getValue(DojoInfo.class);
                all_dojos_info_fromOneUser.add(sampleDojo);

            }
        }

        return all_dojos_info_fromOneUser;

    }


    /**
     * Get all the dojos from the user we pass the userID as a parameter:
     * dojos_info --> userID --> dojo1 (info), dojo2 (info)...
     * @param dataSnapshot a snapshot of the database
     * @param userID the ID of the user you want to get the information
     * @return a list with all the "dojos_info" node information from the specific user by passing the user ID
     */
    public List<DojoInfo> getDojosInfoFromOneUser(DataSnapshot dataSnapshot, String userID){

        List<DojoInfo> all_dojos_info_fromOneUser = new ArrayList<>();

        DojoInfo sampleDojo = new DojoInfo();

        if (mAuth.getCurrentUser() != null) {

            // 1) Looping through all the dojos from the userID that was passed as a parameter.
            // fkscDatabase --> dojos_info --> userID --> dojo1, dojo2, dojo3...
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .getChildren()) {
                Log.d(TAG, "getDojosInfoFromOneUser: " + ds);

                sampleDojo = ds.getValue(DojoInfo.class);
                all_dojos_info_fromOneUser.add(sampleDojo);

            }
        }

        return all_dojos_info_fromOneUser;

    }


    /**
     * Get all the dojos from the user we pass the userID as a parameter:
     * dojos_info --> userID --> dojo1 (info), dojo2 (info)...
     * @param dataSnapshot a snapshot of the database
     * @param userID the ID of the user you want to get the information
     * @return a list with all the "dojos_info" and "dojos_settings" node information of the specific
     * user by passing its ID
     */
    public List<DojoInfoAndSettings> getDojosInfoAndSettingsFromOneUser(DataSnapshot dataSnapshot, String userID){

        List<DojoInfo> all_dojos_info_fromOneUser = new ArrayList<>();
        List<DojoSettings> all_dojos_settings_fromOneUser = new ArrayList<>();
        List<DojoInfoAndSettings> all_dojos_infoAndSettings_fromOneUser = new ArrayList<>();

        DojoInfo sampleDojoInfo = new DojoInfo();
        DojoSettings sampleDojoSettings = new DojoSettings();

        if (mAuth.getCurrentUser() != null) {

            // 1) Looping through all the dojos info from the userID that was passed as a parameter.
            // fkscDatabase --> dojos_info --> userID --> dojo1, dojo2, dojo3...
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .getChildren()) {
                Log.d(TAG, "getDojosInfoAndSettingsFromOneUser: " + ds);

                sampleDojoInfo = ds.getValue(DojoInfo.class);
                all_dojos_info_fromOneUser.add(sampleDojoInfo);

            }


            // 2) Looping through all the dojos settings from the userID that was passed as a parameter.
            // fkscDatabase --> dojos_settings --> userID --> dojo1, dojo2, dojo3...
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_settings))
                    .child(userID)
                    .getChildren()) {
                Log.d(TAG, "getDojosInfoAndSettingsFromOneUser: " + ds);

                sampleDojoSettings = ds.getValue(DojoSettings.class);
                all_dojos_settings_fromOneUser.add(sampleDojoSettings);

            }
        }


        for (int i = 0; i < all_dojos_info_fromOneUser.size(); i++){

            all_dojos_infoAndSettings_fromOneUser.add(
                    new DojoInfoAndSettings(all_dojos_info_fromOneUser.get(i)
                            , all_dojos_settings_fromOneUser.get(i)));

        }


        return all_dojos_infoAndSettings_fromOneUser;

    }


    /**
     * Get all the dojos from the current user.
     * dojos_info --> userID --> dojo1 (info), dojo2 (info)...
     * dojos_settings --> userID --> dojo1 (info), dojo2 (info)...
     * @param dataSnapshot a snapshot of the database
     * @return a list with all the "dojos_info" and "dojos_settings" node information from the current
     * logged in user
     */
    public List<DojoInfoAndSettings> getDojosInfoAndSettingsFromOneUser(DataSnapshot dataSnapshot){

        List<DojoInfo> all_dojos_info_fromOneUser = new ArrayList<>();
        List<DojoSettings> all_dojos_settings_fromOneUser = new ArrayList<>();
        List<DojoInfoAndSettings> all_dojos_infoAndSettings_fromOneUser = new ArrayList<>();

        DojoInfo sampleDojoInfo = new DojoInfo();
        DojoSettings sampleDojoSettings = new DojoSettings();

        if (mAuth.getCurrentUser() != null) {

            String userID = mAuth.getCurrentUser().getUid();

            // 1) Looping through all the dojos info from the current user.
            // fkscDatabase --> dojos_info --> userID --> dojo1, dojo2, dojo3...
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_info))
                    .child(userID)
                    .getChildren()) {
                Log.d(TAG, "getDojosInfoAndSettingsFromOneUser: " + ds);

                sampleDojoInfo = ds.getValue(DojoInfo.class);
                all_dojos_info_fromOneUser.add(sampleDojoInfo);

            }


            // 2) Looping through all the dojos settings from current user.
            // fkscDatabase --> dojos_settings --> userID --> dojo1, dojo2, dojo3...
            for (DataSnapshot ds : dataSnapshot
                    .child(mContext.getString(R.string.dbname_dojos_settings))
                    .child(userID)
                    .getChildren()) {
                Log.d(TAG, "getDojosInfoAndSettingsFromOneUser: " + ds);

                sampleDojoSettings = ds.getValue(DojoSettings.class);
                all_dojos_settings_fromOneUser.add(sampleDojoSettings);

            }
        }


        for (int i = 0; i < all_dojos_info_fromOneUser.size(); i++){

            all_dojos_infoAndSettings_fromOneUser.add(
                    new DojoInfoAndSettings(all_dojos_info_fromOneUser.get(i)
                            , all_dojos_settings_fromOneUser.get(i)));

        }


        return all_dojos_infoAndSettings_fromOneUser;

    }





    /**
     * Retrieve a list of all the registered DojoInfo.
     * @param dataSnapshot a snapshot of the database
     * @return a list with all the "dojos_info" node information from all dojos that are registered in the app
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
     * Retrieve a list of all the registered DojoInfo and DojoSettings.
     * @param dataSnapshot a snapshot of the database
     * @return a list with all "dojos_info" and "dojos_settings" node information from all the users that are
     * registered in the app
     */
    public List<DojoInfoAndSettings> getAllDojosInfoAndDojosSettings(DataSnapshot dataSnapshot){

        List<DojoInfo> all_dojos_info = new ArrayList<>();
        List<DojoSettings> all_dojos_setttings = new ArrayList<>();
        List<DojoInfoAndSettings> all_dojos_infoAndSettings = new ArrayList<>();

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

        // fkscDatabase --> dojos_settings --> Loop through the (user's ID)
        for (DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_dojos_settings))
                .getChildren()){

            // Loop through the children in userID, which are all the dojos from this user.
            // userID_1 --> (dojo1, dojo2, dojo3) and userID_2 --> (dojo1, dojo2)...
            for (DataSnapshot ds2: ds.getChildren()){

                DojoSettings dojoSettings = new DojoSettings();
                dojoSettings = ds2.getValue(DojoSettings.class);
                all_dojos_setttings.add(dojoSettings);

            }

        }

        // Create the DojoInfoAndSettings list with the values from the list of DojoInfo and the list of DojoSettings.
        for (int i =0; i < all_dojos_info.size(); i++){

            all_dojos_infoAndSettings.add(new DojoInfoAndSettings(all_dojos_info.get(i), all_dojos_setttings.get(i)));

        }


        return all_dojos_infoAndSettings;

    }


    /**
     * Get all dojos_info and dojos_settings information only from ONE SINGLE DOJO.
     * @param dataSnapshot a snapshot of the database
     * @param userID the ID of the user you want to get the information
     * @param dojo_number the number of the dojo you want to get the information, for example, "dojo3" or "dojo4"
     *                    and so on
     * @return the "dojos_info" and "dojos_settings" node information from a single dojo
     */
    public DojoInfoAndSettings getDojoInfoAndSettingsFromOneDojo(DataSnapshot dataSnapshot
            , String userID, String dojo_number){

        DojoInfo dojoInfo = new DojoInfo();
        DojoSettings dojoSettings = new DojoSettings();

        // fkscDatabase --> dojos_info --> userID --> Loop through the dojos from this user. (dojo1, dojo2, dojo3)
        for (DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_dojos_info))
                .child(userID)
                .getChildren()){

            // If the dojo is the dojo_number I passed as a variable.
            // For example dojo1 == dojo1. I get the value from this Dojo.
            if (ds.getKey().equals(dojo_number)){
                dojoInfo = ds.getValue(DojoInfo.class);
            }

        }

        // fkscDatabase --> dojos_settings --> userID --> Loop through all the dojos from this user. (dojo1, dojo2, dojo3)
        for (DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_dojos_settings))
                .child(userID)
                .getChildren()){

            // If the dojo is the dojo_number I passed as a variable.
            // For example dojo1 == dojo1. I get the value from this Dojo.
            if (ds.getKey().equals(dojo_number)){
                dojoSettings = ds.getValue(DojoSettings.class);
            }


        }


        return  new DojoInfoAndSettings(dojoInfo, dojoSettings);

    }

    /**
     * Get the number of dojos the user has.
     * @param dataSnapshot a snapshot of the database
     * @return the number of dojos the current user has
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
     * @param dataSnapshot a snapshot of the database
     * @return the "dojos_settings" node information from the current logged in user
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



    /*============================================ championships_info node ============================================*/

    /**
     * Retrieve a list with all the CHAMPIONSHIPS INFO.
     * @param dataSnapshot a snapshot of the database
     * @return a list with all the "championships" node information, this is a list with
     * all the championships that are registered in the app: "championship1" or "championship2"
     * or "championship3" and so on
     */
    public List<ChampionshipInfo> getAllChampionshipsInfo(DataSnapshot dataSnapshot){

        ChampionshipInfo sampleChampionshipInfo = new ChampionshipInfo();
        List<ChampionshipInfo> championshipInfos = new ArrayList<>();

        // fkscDatabase --> championships_info --> Loop through all the championships inside here
        // (championship1, championship2, championship3)
        for (DataSnapshot ds: dataSnapshot
                .child(mContext.getString(R.string.dbname_championships_info))
                .getChildren()){

            // Debugging
            int index = ds.getKey().lastIndexOf("p") + 1;
            Log.i(TAG, "getAllChampionshipsInfo: ADDING a Championship " + ds.getKey().substring(index));

            sampleChampionshipInfo = ds.getValue(ChampionshipInfo.class);
            championshipInfos.add(sampleChampionshipInfo);

        }

        return  championshipInfos;

    }

    /*============================================ END OF championships_info node ============================================*/


    /*=================================************ END OF Getters and Setters ************===============================*/



    /*=================================================== UPDATES ===================================================*/

    /*======================================= Update users =======================================*/
    /**
     * Updates the dojo Database value to
     * @param dojo the dojo of the user
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
     * @param fullName the full name of the user
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
     * @param profileImgURL the URL of the Profile Photo
     */
    private void updateProfileImgURL(String profileImgURL){
        Log.d(TAG, "updateProfileImgURL: Updating Profile Image to: " + profileImgURL);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_profile_img_url))
                .setValue(profileImgURL);

    }


    /**
     * Updates the Registration Number Database value to
     * @param registrationNumber the Registration Number of the user
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
     * @param verified a boolean that means the state of the user's account
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
     * @param aboutMe a description of the user
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
     * @param coverImgURL the URL of the Cover Photo
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
     * @param curriculum a description of the curriculum of the user, the championships he already won
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
     * @param beltColor the user's color belt
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
     * @param birthDate the birth date of the user
     */
    public void updateBirthDate(String birthDate){
        Log.d(TAG, "updateBirthDate: Updating Birth Date to: " + birthDate);

        myRef.child(mContext.getString(R.string.dbname_users_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_birth_date))
                .setValue(birthDate);

    }
    /*======================================= END OF Update users_settings =======================================*/


    /*======================================= Update dojo_info =======================================*/

    /**
     * Updates the name of the dojo the one given in the parameter.
     * @param name the name of the dojo
     * @param dojoNumber the dojo Number to see which dojo we are trying to change. For example,
     *                   dojo1 or dojo2 and so on...
     */
    public void updateDojoName(String name, String dojoNumber){
        Log.d(TAG, "updateDojoName: updating dojo name to " + name);

        myRef.child(mContext.getString(R.string.dbname_dojos_info))
                .child(userID)
                .child(dojoNumber)
                .child(mContext.getString(R.string.field_dojo_name))
                .setValue(name);
    }


    /**
     * Updates the city of the dojo to the one given in the parameter.
     * @param city the city of the dojo
     * @param dojoNumber the dojo Number to see which dojo we are trying to change. For example,
     *                   dojo1 or dojo2 and so on...
     */
    public void updateCity(String city, String dojoNumber){
        Log.d(TAG, "updateCity: updating city to " + city);

        myRef.child(mContext.getString(R.string.dbname_dojos_info))
                .child(userID)
                .child(dojoNumber)
                .child(mContext.getString(R.string.field_city))
                .setValue(city);
    }


    /**
     * Updates the Cover Image Url of the dojo the one given in the parameter.
     * @param coverImgURL the URL of the Cover Photo
     * @param dojoNumber the dojo Number to see which dojo we are trying to change. For example,
     *                   dojo1 or dojo2 and so on...
     */
    private void updateCoverImgURL(String coverImgURL, String dojoNumber){
        Log.d(TAG, "updateCoverImgURL: updating cover image to " + coverImgURL);

        myRef.child(mContext.getString(R.string.dbname_dojos_info))
                .child(userID)
                .child(dojoNumber)
                .child(mContext.getString(R.string.field_cover_img_url))
                .setValue(coverImgURL);
    }


    /*======================================= END OF Update dojo_info =======================================*/




    /*======================================= Update dojo_settings =======================================*/


    /**
     * Updates the address of the dojo the one given in the parameter.
     * @param address the address of the dojo
     * @param dojoNumber the dojo Number to see which dojo we are trying to change. For example,
     *                   dojo1 or dojo2 and so on...
     */
    public void updateAddress(String address, String dojoNumber){
        Log.d(TAG, "updateAddress: updating address to " + address);

        myRef.child(mContext.getString(R.string.dbname_dojos_settings))
                .child(userID)
                .child(dojoNumber)
                .child(mContext.getString(R.string.field_address))
                .setValue(address);
    }


    /**
     * Updates the telephone of the dojo the one given in the parameter.
     * @param telephone the telephone of the dojo
     * @param dojoNumber the dojo Number to see which dojo we are trying to change. For example,
     *                   dojo1 or dojo2 and so on...
     */
    public void updateTelephone(String telephone, String dojoNumber){
        Log.d(TAG, "updateTelephone: updating telephone to " + telephone);

        myRef.child(mContext.getString(R.string.dbname_dojos_settings))
                .child(userID)
                .child(dojoNumber)
                .child(mContext.getString(R.string.field_telephone))
                .setValue(telephone);
    }



    /**
     * Updates the description of the dojo the one given in the parameter.
     * @param description the description of the dojo
     * @param dojoNumber the dojo Number to see which dojo we are trying to change. For example,
     *                   dojo1 or dojo2 and so on...
     */
    public void updateDescription(String description, String dojoNumber){
        Log.d(TAG, "updateDescription: updating description to " + description);

        myRef.child(mContext.getString(R.string.dbname_dojos_settings))
                .child(userID)
                .child(dojoNumber)
                .child(mContext.getString(R.string.field_description))
                .setValue(description);
    }



    /*======================================= END OF Update dojo_settings =======================================*/


    /*=================================================== END OF UPDATES ===================================================*/


    /*=================================================== Upload ===================================================*/

    /**
     * Upload a new profile photo or cover photo to Firebase Storage.
     * @param photoType the type of the photo we are trying to change, for example,
     *                  Profile Photo, Cover Photo (when adding dojo) or Cover Photo (when editing dojo)
     * @param imgURL the URL of the image
     * @param dojoCount the number of the dojo that will be changed
     */
    public void uploadNewPhoto(String photoType, String imgURL, final int dojoCount){

        // Case 1) ********************** Editing Profile - Adding the User's PROFILE Photo. **********************
        if (photoType.equals(mContext.getString(R.string.photo_type_profile))){
            Log.i(TAG, "uploadNewPhoto: Uploading a new profile photo " + imgURL);

            Toast.makeText(mContext, R.string.preparing_your_picture, Toast.LENGTH_LONG).show();

            FilePaths filePaths = new FilePaths();

            if (mAuth.getCurrentUser() != null) {

                String user_id = mAuth.getCurrentUser().getUid();


                // 1) Creates the Storage Reference --> "photos/users/user_id/profile_photo"
                StorageReference storageReference = mStorageReference
                        .child(filePaths.FIREBASE_STORAGE_PATH + "/" + user_id + "/" + photoType);


                // 2) Convert image url to bitmap and then to bytes.
                Bitmap bitmap = ImageManager.getBitMapFromImgURL(imgURL);
                byte[] bytes = ImageManager.getBytesFromBitmap(bitmap, 50);



                // 3) Upload the photo to storage.
                UploadTask uploadTask = null;
                uploadTask = storageReference.putBytes(bytes);


                // 4) Navigate to Profile Activity.
                Intent intentProfileActivity = new Intent(mContext, ProfileActivity.class);
                intentProfileActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intentProfileActivity);


                // 5) Handle the loading.
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "onSuccess: Uploaded a photo");
                        Uri firebaseURL = taskSnapshot.getDownloadUrl();
                        Toast.makeText(mContext, R.string.photo_added_successfully, Toast.LENGTH_SHORT).show();

                        if (firebaseURL != null) {

                            // Add profile photo to "photos" node;
                            addProfilePhoto(firebaseURL.toString());

                            // Add link to the "users" NODE at the FIELD "profile_img_url".
                            updateProfileImgURL(firebaseURL.toString());

                        }



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: Failed to upload the photo");
                        Toast.makeText(mContext, "Failed to upload your photo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        // Show the percentage of the uploading picture.
                        double progress = 100*(taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        Log.i(TAG, "onProgress: " + progress);


                        if (progress - 15 > mPhotoProgress) {
                            Toast.makeText(mContext, "Uploading photo: "
                                    + new DecimalFormat("##.#").format(progress) + "%", Toast.LENGTH_SHORT).show();
                            mPhotoProgress = progress;
                        }

                    }
                });

            }

        }

        // Case 2) ********************** Adding a dojo - Adding the Dojo's COVER Photo. **********************
        else if (photoType.equals(mContext.getString(R.string.photo_type_cover_photo_add))) {
            Log.i(TAG, "uploadNewPhoto: Uploading a new cover photo " + imgURL);

            // Make sure the user selected some picture, if not do nothing.
            if (imgURL != null) {

                Toast.makeText(mContext, mContext.getString(R.string.preparing_your_picture), Toast.LENGTH_SHORT).show();

                FilePaths filePaths = new FilePaths();

                if (mAuth.getCurrentUser() != null) {

                    String user_id = mAuth.getCurrentUser().getUid();

                    // 1) Creates the Storage Reference;
                    // "photos/users/user_id/cover_photo3"
                    // (dojoCount + 1) If the user has 3 dojos registered, this next picture will be
                    // cover_photo4, and the next, cover_photo5, and the next, cover_photo6 and so on.
                    StorageReference storageReference = mStorageReference
                            .child(filePaths.FIREBASE_STORAGE_PATH + "/" + user_id
                                    + "/" + mContext.getString(R.string.photo_type_cover) + dojoCount);

                    // 2) Convert imgURL into bitmap and then to bytes;
                    Bitmap bitmap = ImageManager.getBitMapFromImgURL(imgURL);
                    byte[] bytes = ImageManager.getBytesFromBitmap(bitmap, 50);


                    // 3) Upload the image to the Firebase Storage.
                    UploadTask uploadTask = null;
                    uploadTask = storageReference.putBytes(bytes);

                    // 4) Navigate back to Profile Page.
                    Intent intentProfileActivity = new Intent(mContext, ProfileActivity.class);
                    intentProfileActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    mContext.startActivity(intentProfileActivity);

                    // 5) Handle the Loading.
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.i(TAG, "onSuccess: Cover Photo uploaded");
                            Toast.makeText(mContext, mContext.getString(R.string.photo_added_successfully), Toast.LENGTH_SHORT).show();

                            Uri firebaseUri = taskSnapshot.getDownloadUrl();

                            if (firebaseUri != null) {

                                // Add the photo into "photos" node;
                                addCoverPhoto(firebaseUri.toString(), dojoCount);

                                // Add the link of the photo into "dojos_info" node (field cover_img_url).
                                updateCoverImgURL(firebaseUri.toString(),
                                        mContext.getString(R.string.field_dojo) + dojoCount);

                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: Failed to upload Cover Photo " + e.getMessage());
                            // Notify the user about the ERROR.
                            Toast.makeText(mContext, mContext.getString(R.string.photo_failure)
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            Log.i(TAG, "onProgress: Uploading the Cover Photo " + String.valueOf(progress));

                            if (progress - 15 > mPhotoProgress) {
                                Toast.makeText(mContext, mContext.getString(R.string.loading_picture)
                                        + new DecimalFormat("##.#").format(progress) + "%", Toast.LENGTH_SHORT).show();
                                mPhotoProgress = progress;
                            }

                        }
                    });


                }

            }
        }

        // Case 3) ********************** Editing a dojo - Editing the Dojo's COVER Photo. **********************
        else if (photoType.equals(mContext.getString(R.string.photo_type_cover_photo_edit))) {
            Log.i(TAG, "uploadNewPhoto: Uploading a Cover Photo for EditDojoActivity");

            final FilePaths filePaths = new FilePaths();

            if (mAuth.getCurrentUser() != null) {

                String user_id = mAuth.getCurrentUser().getUid();

                // 1) Creates the StorageReference;
                // dojoCount here is the number of the dojo that is going to change.
                // Suppose I am editing dojo3, so dojoCount is 3.
                StorageReference storageReference = mStorageReference.child(filePaths.FIREBASE_STORAGE_PATH
                        + "/" + user_id + "/" + mContext.getString(R.string.photo_type_cover) + dojoCount);

                // 2) Converts the imgURL into Bitmap and then to bytes;
                Bitmap bitmap = ImageManager.getBitMapFromImgURL(imgURL);
                byte[] bytes = ImageManager.getBytesFromBitmap(bitmap, 50);

                // 3) Creates the uploadTask;
                UploadTask uploadTask = null;
                uploadTask = storageReference.putBytes(bytes);

                // 4) Navigates back to the Profile Screen;
                Intent intentProfileActivity = new Intent(mContext, ProfileActivity.class);
                intentProfileActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(intentProfileActivity);

                // 5) Handle the image loading.
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG, "onSuccess: Uploaded the image");
                        Toast.makeText(mContext, mContext.getString(R.string.photo_added_successfully), Toast.LENGTH_SHORT).show();

                        Uri firebaseUri = taskSnapshot.getDownloadUrl();

                        if (firebaseUri != null) {

                            Log.i(TAG, "onSuccess: Picture: " + firebaseUri.toString());

                            // Add or Update the photo into the "photos" node.
                            addCoverPhoto(firebaseUri.toString(), dojoCount);

                            // Update the cover photo into the "dojos_info" node
                            updateCoverImgURL(firebaseUri.toString(),
                                    mContext.getString(R.string.field_dojo) + String.valueOf(dojoCount));

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: Failed to upload the image");
                        // Warn the user that the image could not be uploaded.
                        Toast.makeText(mContext, mContext.getString(R.string.photo_failure), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        // Get the percentage number of the progress to upload the image.
                        double progress = (100*taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        Log.i(TAG, "onProgress: Uploading Cover Photo" + progress);

                        if (progress - 15 > mPhotoProgress) {
                            Toast.makeText(mContext, mContext.getString(R.string.loading_picture)
                                    + new DecimalFormat("##.#").format(progress) + "%", Toast.LENGTH_SHORT).show();
                            mPhotoProgress = progress;
                        }

                    }
                });
            }

        }

    }

    /*=================================================== END OF Upload ===================================================*/

    /*=================================================== Signout ===================================================*/

    /**
     * 1) Sign out;
     * 2) Finish actual activity.
     * 3) Move to Login Screen;
     */
    public void ifNoUserSignOut(Activity activity) {

        if (mAuth.getCurrentUser() == null) {
            Log.i(TAG, "ifNoUserSignOut: Signing out because there is no user");
            mAuth.signOut();

            Intent intentLogin = new Intent(mContext, LoginActivity.class);
            intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.finish();
            activity.startActivity(intentLogin);

        }
    }

    /*=================================================== END OF Signout ===================================================*/

}
