package com.example.karat.fksc.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.karat.fksc.Championships.ChampionshipActivity;
import com.example.karat.fksc.Members.MembersActivity;
import com.example.karat.fksc.Profile.ProfileActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.Ranking.RankingActivity;

import java.lang.reflect.Field;

/**
 * Created by karat on 06/03/2018.
 */

public class BottomNavigationHelper {

    /**
     * This method just check which item was selected in the Bottom Navigation View and go to the Activity related to this item.
     * @param context
     * @param bottomNavigationView
     */
    public static void enablePagination(final Context context, BottomNavigationView bottomNavigationView){

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.affiliates_menu_bottomNaView: // menu item 0
                        Intent intentAffiliates = new Intent(context, MembersActivity.class);
                        context.startActivity(intentAffiliates);
                        break;
                    case R.id.championShips_menu_bottomNaView: // menu item 1
                        Intent intentChampionship = new Intent(context, ChampionshipActivity.class);
                        context.startActivity(intentChampionship);
                        break;
                    case R.id.ranking_menu_bottomNaView: // menu item 2
                        Intent intentRanking = new Intent(context, RankingActivity.class);
                        context.startActivity(intentRanking);
                        break;
                    case R.id.profile_menu_bottomNaView: // menu item 3
                        Intent intentProfile = new Intent(context, ProfileActivity.class);
                        context.startActivity(intentProfile);
                        break;
                }
                return true;
            }
        });


    }


    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BottomNav", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomNav", "Unable to change value of shift mode", e);
        }
    }

}
