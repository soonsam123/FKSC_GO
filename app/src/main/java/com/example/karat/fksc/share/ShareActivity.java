package com.example.karat.fksc.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karat.fksc.R;
import com.example.karat.fksc.utils.Permissions;
import com.example.karat.fksc.utils.SectionsPageAdapter;

public class ShareActivity extends AppCompatActivity{

    private static final String TAG = "ShareActivity";

    // Layout
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    // Vars
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    // Context
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.i(TAG, "onCreate: Creating activity");

        // Case 1) Permissions are GRANTED.
        // a. Check if all the permissions were already granted.
        // b. Start the processing.
        if (checkPermissionsArray(Permissions.PERMISSIONS)){
            setupWidgets();
            setupViewPager();
        }
        // Case 2) Permissions are NOT GRANTED.
        // a. Verify the permissions.
        // b. Restart the activity
        else{
            verifyPermissions(Permissions.PERMISSIONS);

            Intent intentShareActivity = new Intent(mContext, ShareActivity.class);
            finish();
            startActivity(intentShareActivity);
        }

    }


    /*===================================== Setups =====================================*/

    /**
     * Set up the widgets to the layout id values.
     */
    private void setupWidgets(){
        Log.d(TAG, "setupWidgets: Setting up widgets");

        mViewPager = findViewById(R.id.viewPagerContainer);
        mTabLayout = findViewById(R.id.tabLayout1);

        mContext = ShareActivity.this;

    }

    /**
     * Set up the view pager with the Gallery and the Camera Fragments.
     */
    private void setupViewPager(){
        Log.d(TAG, "setupViewPager: Settipng up the View Pager");

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        sectionsPageAdapter.addFragment(new GalleryFragment());
        sectionsPageAdapter.addFragment(new CameraFragment());

        mViewPager.setAdapter(sectionsPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        if (mTabLayout.getTabCount() == 2) {
            mTabLayout.getTabAt(0).setText(R.string.gallery);
            mTabLayout.getTabAt(1).setText(R.string.camera);
        }

    }

    /*===================================== END OF Setups =====================================*/


    /*===================================== Verifying and Checking Permissions =====================================*/

    /**
     * Check an entire Array of Permissions.
     * @param permissions a list with some app permissions
     * @return "true" if the array is all verified, "false" if any of the items in the array
     * are not verified
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.i(TAG, "checkPermissions: Checking an array of permissions");

        // Loop through all the permission and check one by one.
        // If any of them are not granted, return false.
        for (int i = 0; i < permissions.length; i++){
            String singlePermission = permissions[i];
            if (!checkSinglePermission(singlePermission)){
                return  false;
            }
        }
        return true;
    }


    /**
     * Check a single permission
     * @param permission a single permission
     * @return "true" if this permission is verified, "false" if this permission is NOT verified
     */
    public boolean checkSinglePermission(String permission){
        Log.i(TAG, "checkSinglePermission: Checking a single permission " + permission);

        // Get the permission Request Code from this permission and check if it was granted or not.
        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "checkSinglePermission: Permission was NOT granted " + permission);
            return false;
        } else {
            Log.i(TAG, "checkSinglePermission: Permission was granted " + permission);
            return true;
        }

    }

    /**
     * Verify and entire Array of permissions.
     * @param permissions a list with some permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.i(TAG, "verifyPermissions: Verifying an array of permissions");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST);

    }

    /*===================================== END OF Verifying and Checking Permissions =====================================*/

}
