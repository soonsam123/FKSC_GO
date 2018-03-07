package com.example.karat.fksc.Championships;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.BottomNavigationHelper;
import com.example.karat.fksc.Utils.SectionsPageAdapter;

/**
 * Created by karat on 06/03/2018.
 */

public class ChampionshipActivity extends AppCompatActivity {

    private static final String TAG = "ChampionshipActivity";

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;

    private Context mContext = ChampionshipActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship);
        Log.i(TAG, "onCreate: Starting activity");

        setupWidgets();
        setupBottomNavigationView();

    }


    /**
     * Setting up the widgets to the layout values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        viewPager = findViewById(R.id.viewPagerContainer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        tabLayout = findViewById(R.id.tabLayout1);

    }


    /**
     * Enable the user to change from one activity to another when clicking in the Bottom Navigation View items.
     */
    private void setupBottomNavigationView(){
        Log.i(TAG, "setupBottomNavigationView: Setting up the Bottom Navigation View");

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);

    }
}
