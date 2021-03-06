package soon.semicontato.karat.fksc.ranking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.BottomNavigationHelper;
import soon.semicontato.karat.fksc.utils.SectionsPageAdapter;


/**
 * Created by karat on 06/03/2018.
 */

public class RankingActivity extends AppCompatActivity {

    private static final String TAG = "RankingActivity";

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private Context mContext = RankingActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        Log.i(TAG, "onCreate: Stating the activity");

        setupWidgets();
        setupBottomNavigationView();
        setupViewPager();
        setupToolBar();

    }


    /**
     * Set up my customized toolbar to be the Support Action Bar for Ranking Activity.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }


    /**
     * Setting up the widgets to the layout values.
     */
    private void setupWidgets(){

        viewPager = findViewById(R.id.viewPagerContainer);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        tabLayout = findViewById(R.id.tabLayout1);
        toolbar = findViewById(R.id.toolBar_layout_top_bar);

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
    private void setupBottomNavigationView(){
        Log.i(TAG, "setupBottomNavigationView: Setting up the Bottom Navigation View");

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);


    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Starting");

        /*==== Code to make the selected item use a different color ====*/
        // Members = 0 / Championship = 1 / Ranking = 2 / Profile = 3
        int ACTIVITY_NUM = 2;
        // 1) Get the bottomNavigationView menu;
        Menu menu = bottomNavigationView.getMenu();
        // 2) Get the current item;
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        // 3) Select it, so it'll display a different color for this icon.
        menuItem.setChecked(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
