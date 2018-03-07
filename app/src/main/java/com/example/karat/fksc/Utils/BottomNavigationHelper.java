package com.example.karat.fksc.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.karat.fksc.Championships.ChampionshipActivity;
import com.example.karat.fksc.Members.MembersActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.Ranking.RankingActivity;

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
                    case R.id.affiliates_menu_bottomNaView:
                        Intent intentAffiliates = new Intent(context, MembersActivity.class);
                        context.startActivity(intentAffiliates);
                        break;
                    case R.id.championShips_menu_bottomNaView:
                        Intent intentChampionship = new Intent(context, ChampionshipActivity.class);
                        context.startActivity(intentChampionship);
                        break;
                    case R.id.ranking_menu_bottomNaView:
                        Intent intentRanking = new Intent(context, RankingActivity.class);
                        context.startActivity(intentRanking);
                        break;
                }
                return false;
            }
        });


    }

}
