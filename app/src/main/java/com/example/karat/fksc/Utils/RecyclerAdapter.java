package com.example.karat.fksc.Utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.fksc.Profile.OtherProfileActivity;
import com.example.karat.fksc.R;
import com.example.karat.fksc.models.UserAndUserSettings;

import java.util.List;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by karat on 12/03/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<UserAndUserSettings> users;
    private Context mContext;

    public RecyclerAdapter(List<UserAndUserSettings> users, Context mContext) {
        this.users = users;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_affiliates_recyclerview, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final UserAndUserSettings sampleUser = users.get(position);

        holder.fullName.setText(sampleUser.getUser().getFull_name());
        holder.dojo.setText(sampleUser.getUser().getDojo());
        holder.registrationNumber.setText(sampleUser.getUser().getRegistration_number());
        Log.i(TAG, "onBindViewHolder: " + sampleUser.getUser().getProfile_img_url());
        UniversalImageLoader.setImage(sampleUser.getUser().getProfile_img_url(),
                holder.circleImageView, null, "");


        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOtherProfile = new Intent(mContext, OtherProfileActivity.class);
                intentOtherProfile.putExtra(mContext.getString(R.string.field_user_id), sampleUser.getUserSettings().getUser_id());
                mContext.startActivity(intentOtherProfile);
            }
        });
    }

    @Override
    public int getItemCount() {return users.size();}
}
