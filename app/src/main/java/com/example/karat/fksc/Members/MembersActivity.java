package com.example.karat.fksc.Members;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.karat.fksc.Login.LoginActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.BottomNavigationHelper;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.SectionsPageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MembersActivity extends AppCompatActivity {

    private static final String TAG = "MembersActivity";

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private Context mContext = MembersActivity.this;
    
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        Log.i(TAG, "onCreate: Starting Activity");

        setupFirebaseAuth();
        ifNoUserSignOut();
        setupWidgets();
        setupViewPager();
        setupBottomNavigationView();
        setupToolBar();

    }



    /*==================================== Helpers ====================================*/

    /**
     * 1) Sign out;
     * 2) Move to Login Screen;
     * 3) Finish MembersActivity.
     */
    public void ifNoUserSignOut() {
        Log.i(TAG, "ifNoUserSignOut: Signing out because there is no user");

        if (mAuth.getCurrentUser() == null) {
            mAuth.signOut();

            Intent intentLogin = new Intent(mContext, LoginActivity.class);
            startActivity(intentLogin);

            finish();
        }
    }
    /*==================================== END OF Helpers ====================================*/


    /*==================================== Setups ====================================*/
    /**
     * Setting up the widgets to the layout values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        viewPager = findViewById(R.id.viewPagerContainer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        tabLayout = findViewById(R.id.tabLayout1);
        toolbar = findViewById(R.id.toolBar_layout_top_bar);

    }


    /**
     * Setting up my customized toolbar to be the activity toolbar.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }


    /**
     * Setting up the View Pager.
     * 1) Adds the fragments to the SectionPageAdapter;
     * 2) Set the view pager's adapter to this one we've created;
     * 3) Set the tabLayout with this viewPager.
     */
    private void setupViewPager(){
        Log.i(TAG, "setupViewPager: Setting up the ViewPager");

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        sectionsPageAdapter.addFragment(new AthletesFragment());
        sectionsPageAdapter.addFragment(new DojosFragment());
        sectionsPageAdapter.addFragment(new BlackbeltsFragment());

        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (tabLayout.getTabCount() == 3) {
            tabLayout.getTabAt(0).setText(getString(R.string.athletes));
            tabLayout.getTabAt(1).setText(getString(R.string.dojos));
            tabLayout.getTabAt(2).setText(getString(R.string.black_belts));
        }

    }


    /**
     * This setup the properties for the Bottom Navigation View.
     * Enable the user to paginate between the different items and go to different activities.
     */
    private void setupBottomNavigationView(){
        Log.i(TAG, "setupBottomNavigationView: Setting up the bottom navigation view");

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);

    }
    /*==================================== END OF Setups ====================================*/



    /*=================================== Menu ===================================*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.contact_menu_actionbar:
                Toast.makeText(mContext, "Contato", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout_menu_actionbar:

                // Sign out.
                mAuth.signOut();

                // Clear the activity task and start login screen.
                Intent intentLogin = new Intent(mContext, LoginActivity.class);
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentLogin);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    /*=================================== END OF Menu ===================================*/


    /*=================================== Firebase ===================================*/
    
    private void setupFirebaseAuth(){
        
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null){
            Log.i(TAG, "setupFirebaseAuth: User is logged in: " + mAuth.getCurrentUser().getUid());
        } else {
            Log.i(TAG, "setupFirebaseAuth: User is signed out");
        }
    }

    
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    
    /*=================================== END OF Firebase ===================================*/
    
    
}
