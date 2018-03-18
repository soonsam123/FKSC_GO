package com.example.karat.fksc.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.fksc.DojoActivity.DojoActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.models.DojoInfo;
import com.example.karat.fksc.models.DojoInfoAndSettings;

import java.util.List;

/**
 * Created by karat on 17/03/2018.
 */

public class RecyclerAdapterDojos extends RecyclerView.Adapter<ViewHolderSquareImage> {

    private List<DojoInfoAndSettings> dojos;
    private Context mContext;

    public RecyclerAdapterDojos(List<DojoInfoAndSettings> dojos, Context mContext) {
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

        final DojoInfoAndSettings sampleDojo = dojos.get(position);

        holder.dojoName.setText(sampleDojo.getDojoInfo().getName());
        holder.city.setText(sampleDojo.getDojoInfo().getCity());
        holder.registrationNumber.setText(sampleDojo.getDojoInfo().getRegistration_number());
        UniversalImageLoader.setImage(sampleDojo.getDojoInfo().getCover_img_url(),
                holder.imageView, null, "");


        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDojo = new Intent(mContext, DojoActivity.class);
                intentDojo.putExtra(mContext.getString(R.string.field_user_id), sampleDojo.getDojoSettings().getUser_id());
                intentDojo.putExtra(mContext.getString(R.string.field_dojo_number), sampleDojo.getDojoSettings().getDojo_number());
                mContext.startActivity(intentDojo);
            }
        });
    }

    @Override
    public int getItemCount() {return dojos.size();}
}
