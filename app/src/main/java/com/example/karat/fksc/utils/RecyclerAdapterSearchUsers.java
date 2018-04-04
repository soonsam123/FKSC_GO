package com.example.karat.fksc.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.fksc.profile.OtherProfileActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.models.User;
import com.example.karat.fksc.models.UserSettings;

import java.util.List;

/**
 * Created by karat on 12/03/2018.
 */

public class RecyclerAdapterSearchUsers extends RecyclerView.Adapter<ViewHolder> {

    private List<User> users;
    private List<String> users_ids;
    private Context mContext;

    public RecyclerAdapterSearchUsers(List<User> users, List<String> users_ids, Context mContext) {
        this.users = users;
        this.users_ids = users_ids;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_affiliates_recyclerview, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final User sampleUser = users.get(position);
        final String userID = users_ids.get(position);

        holder.fullName.setText(sampleUser.getFull_name());
        holder.dojo.setText(sampleUser.getDojo());
        holder.registrationNumber.setText(sampleUser.getRegistration_number());
        UniversalImageLoader.setImage(sampleUser.getProfile_img_url(),
                holder.circleImageView, null, "");

        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOtherProfile = new Intent(mContext, OtherProfileActivity.class);
                intentOtherProfile.putExtra(mContext.getString(R.string.field_user_id), userID);
                mContext.startActivity(intentOtherProfile);
            }
        });

    }

    @Override
    public int getItemCount() {return users.size();}
}
