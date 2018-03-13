package com.example.karat.fksc.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.fksc.R;
import com.example.karat.fksc.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karat on 12/03/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<User> users;
    private Context mContext;

    public RecyclerAdapter(List<User> users, Context mContext) {
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

        User sampleUser = users.get(position);

        holder.fullName.setText(sampleUser.getFull_name());
        holder.dojo.setText(sampleUser.getDojo());
        holder.registrationNumber.setText(sampleUser.getRegistration_number());

        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Item: " + (position + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {return users.size();}
}
