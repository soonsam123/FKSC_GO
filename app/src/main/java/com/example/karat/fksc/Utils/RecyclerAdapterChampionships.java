package com.example.karat.fksc.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.fksc.Profile.OtherProfileActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.models.ChampionshipInfo;
import com.example.karat.fksc.models.UserAndUserSettings;

import java.util.List;

/**
 * Created by karat on 12/03/2018.
 */

public class RecyclerAdapterChampionships extends RecyclerView.Adapter<ViewHolderChampionships> {

    private List<ChampionshipInfo> championshipInfos;
    private Context mContext;

    public RecyclerAdapterChampionships(List<ChampionshipInfo> championshipInfos, Context mContext) {
        this.championshipInfos = championshipInfos;
        this.mContext = mContext;
    }

    @Override
    public ViewHolderChampionships onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_cardview_championships, parent, false);

        return new ViewHolderChampionships(view);

    }

    @Override
    public void onBindViewHolder(ViewHolderChampionships holder, final int position) {

        final ChampionshipInfo championshipInfo = championshipInfos.get(position);

        holder.championshipName.setText(championshipInfo.getName());
        holder.city.setText(championshipInfo.getCity());
        holder.dateAndHour.setText(championshipInfo.getDate_hour());
        holder.address.setText(championshipInfo.getAddress());
        UniversalImageLoader.setImage(championshipInfo.getCover_img_url()
                , holder.imageView, null, "");

        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {return championshipInfos.size();}
}
