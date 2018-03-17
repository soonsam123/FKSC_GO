package com.example.karat.fksc.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.fksc.R;
import com.example.karat.fksc.models.DojoInfo;

import java.util.List;

/**
 * Created by karat on 17/03/2018.
 */

public class RecyclerAdapterDojos extends RecyclerView.Adapter<ViewHolderSquareImage> {

    private List<DojoInfo> dojos;
    private Context mContext;

    public RecyclerAdapterDojos(List<DojoInfo> dojos, Context mContext) {
        this.dojos = dojos;
        this.mContext = mContext;
    }

    @Override
    public  ViewHolderSquareImage onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_affiliates_dojos_recyclerview, parent, false);

        return new ViewHolderSquareImage(view);

    }

    @Override
    public void onBindViewHolder(ViewHolderSquareImage holder, final int position) {

        DojoInfo sampleDojo = dojos.get(position);

        holder.dojoName.setText(sampleDojo.getName());
        holder.city.setText(sampleDojo.getCity());
        holder.registrationNumber.setText(sampleDojo.getRegistration_number());
        UniversalImageLoader.setImage(sampleDojo.getCover_img_url(),
                holder.imageView, null, "");


        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item: " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {return dojos.size();}
}
