package com.example.karat.fksc.Login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.SectionsPageAdapter;

/**
 * Created by karat on 07/03/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout relLayout_container;
    private RelativeLayout relLayout_logoFKSC;

    // vars
    private static final int NUMBER_OF_TABS = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate: Starting Activity");

        setupWidgets();
        setupViewPager();
        setupClickListeners();

    }

    /**
     * Hide the keyboard from the screen.
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


    /*===================================== Setups =====================================*/

    /**
     * Setting up the widgets with the layout values.
     */
    private void setupWidgets(){
        viewPager = findViewById(R.id.viewPagerContainer);
        tabLayout = findViewById(R.id.tabLayout_login);
        relLayout_container = findViewById(R.id.relLayout_container_activityLogin);
        relLayout_logoFKSC = findViewById(R.id.relLayout_logoFKSC_activityLogin);

    }

    /**
     * Set up all the types of CLICK listeners and KEY listeners.
     */
    private void setupClickListeners(){

        relLayout_logoFKSC.setOnClickListener(this);
        relLayout_container.setOnClickListener(this);

    }

    /**
     * Setting up the View Pager.
     * 1) Create the Adapter and add the fragments;
     * 2) Set the adapter to the view pager;
     * 3) Set the tabLayout with the view pager.
     */
    private void setupViewPager(){

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        sectionsPageAdapter.addFragment(new LoginFragment());
        sectionsPageAdapter.addFragment(new RegisterFragment());

        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (tabLayout.getTabCount() == NUMBER_OF_TABS) {
            tabLayout.getTabAt(0).setText(getString(R.string.login));
            tabLayout.getTabAt(1).setText(getString(R.string.register));
        }

    }

    /*===================================== END OF Setups =====================================*/


    /*===================================== Clicks =====================================*/
    /**
     * If the user click anywhere in the screen. Check where it was clicked and perform an action if necessary.
     * @param view
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.relLayout_container_activityLogin:
                hideKeyBoard();
                break;
            case R.id.relLayout_logoFKSC_activityLogin:
                hideKeyBoard();
                break;
        }

    }
    /*===================================== END OF Clicks =====================================*/

}
