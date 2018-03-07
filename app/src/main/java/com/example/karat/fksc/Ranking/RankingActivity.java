package com.example.karat.fksc.Ranking;

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

public class RankingActivity extends AppCompatActivity {

    private static final String TAG = "RankingActivity";

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;

    private Context mContext = RankingActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Log.i(TAG, "onCreate: Stating the activity");

        setupWidgets();
        setupBottomNavigationView();
        setupViewPager();

    }


    /**
     * Setting up the widgets to the layout values.
     */
    public void setupWidgets(){

        viewPager = findViewById(R.id.viewPagerContainer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        tabLayout = findViewById(R.id.tabLayout1);

    }


    /**
     * Setting up the View Pager.
     * 1) Adds the fragments to the SectionPageAdapter;
     * 2) Set the view pager's adapter to this one we've created;
     * 3) Set the tabLayout with this viewPager.
     */
    public void setupViewPager(){
        Log.i(TAG, "setupViewPager: Setting up the ViewPager");

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        sectionsPageAdapter.addFragment(new PorcampeonatoFragment());
        sectionsPageAdapter.addFragment(new GeralFragment());
        sectionsPageAdapter.addFragment(new GlobalFragment());

        viewPager.setAdapter(sectionsPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (tabLayout.getTabCount() == 3) {
            tabLayout.getTabAt(0).setText(R.string.por_campeonato);
            tabLayout.getTabAt(1).setText(R.string.geral);
            tabLayout.getTabAt(2).setText(R.string.global);
        }
    }


    /**
     * Enables the user to change from one activity to the another when clicking in the BottomNavigationView items.
     */
    public void setupBottomNavigationView(){
        Log.i(TAG, "setupBottomNavigationView: Setting up the Bottom Navigation View");

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);

    }
}
