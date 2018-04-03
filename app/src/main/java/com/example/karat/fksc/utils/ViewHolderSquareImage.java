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

public class ViewHolderSquareImage extends RecyclerView.ViewHolder{

    public ImageView imageView;
    public TextView dojoName;
    public TextView city;
    public TextView registrationNumber;
    public LinearLayout linearLayout_eachItem;


    public ViewHolderSquareImage(View itemView) {
        super(itemView);

        // Cast the variables using itemView.
        imageView = itemView.findViewById(R.id.imageView_snippetAffiliatesDojos);
        dojoName = itemView.findViewById(R.id.dojoName_snippetAffiliatesDojos);
        city = itemView.findViewById(R.id.city_snippetAffiliatesDojos);
        registrationNumber = itemView.findViewById(R.id.registrationNumber_snippetAffiliatesDojos);
        linearLayout_eachItem = itemView.findViewById(R.id.linearLayout_container_snippetAffiliatesDojos);
    }
}
