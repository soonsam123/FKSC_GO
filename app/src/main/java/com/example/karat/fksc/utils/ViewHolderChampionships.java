package com.example.karat.fksc.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.karat.fksc.R;

/**
 * Created by karat on 12/03/2018.
 */

public class ViewHolderChampionships extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView championshipName;
    public TextView city;
    public TextView dateAndHour;
    public TextView address;
    public LinearLayout linearLayout_eachItem;


    public ViewHolderChampionships(View itemView) {
        super(itemView);

        // Cast the variables using itemView.
        imageView = itemView.findViewById(R.id.imgView_coverPhoto_snippetChampionship);
        championshipName = itemView.findViewById(R.id.txtView_championshipName_snippetChampionShip);
        city = itemView.findViewById(R.id.txtView_city_snippetChampionship);
        dateAndHour = itemView.findViewById(R.id.txtView_date_snippetChampionship);
        address = itemView.findViewById(R.id.txtView_address_snippetChampionship);
        linearLayout_eachItem = itemView.findViewById(R.id.linearLayout_container_snippetChampionship);
    }
}
