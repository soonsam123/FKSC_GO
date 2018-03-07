package com.example.karat.fksc.Members;

import android.content.Context;
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

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.BottomNavigationHelper;
import com.example.karat.fksc.Utils.SectionsPageAdapter;

public class MembersActivity extends AppCompatActivity {

    private static final String TAG = "MembersActivity";

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private Context mContext = MembersActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        Log.i(TAG, "onCreate: Starting Activity");

        setupWidgets();
        setupViewPager();
        setupBottomNavigationView();
        setupToolBar();

    }


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
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    /*=================================== END OF Menu ===================================*/

}
