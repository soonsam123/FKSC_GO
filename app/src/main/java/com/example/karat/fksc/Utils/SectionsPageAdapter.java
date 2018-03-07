package com.example.karat.fksc.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karat on 06/03/2018.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionsPageAdapter";

    private final List<Fragment> fragmentListMember = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }


    /**
     * Give the position and get the item of the PagerAdapter that is in that position.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        return fragmentListMember.get(position);
    }

    /**
     * Get the number of fragments that is inside this PagerAdapter.
     * @return
     */
    @Override
    public int getCount() {
        return fragmentListMember.size();
    }

    /**
     * Adds a fragment to the PagerAdapter.
     * @param fragment
     */
    public void addFragment(Fragment fragment){
        Log.i(TAG, "addFragment: Adding the fragment " + fragment.toString() + "to the PagerAdapter");

        fragmentListMember.add(fragment);

    }

    /**
     * Delete the fragment at position : position of the PagerAdapter.
     * @param position
     */
    public void deleteFragment(int position){
        Log.i(TAG, "deleteFragment: Deleting the fragment in the position: " + String.valueOf(position));

        fragmentListMember.remove(position);

    }


    /**
     * Delete all the fragments of the PagerAdapter
     */
    public void deleteAllFragments(){
        Log.i(TAG, "deleteAllFragments: Deleting all the fragments");

        if (fragmentListMember.size() != 0) {
            for (int i = 0; i < fragmentListMember.size(); i++) {
                fragmentListMember.remove(i);
            }
        }

    }

}
