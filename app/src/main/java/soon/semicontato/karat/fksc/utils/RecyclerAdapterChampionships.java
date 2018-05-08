package soon.semicontato.karat.fksc.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soon.semicontato.karat.fksc.championships.ChampionshipActivity;
import soon.semicontato.karat.fksc.models.ChampionshipInfo;

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

    @NonNull
    @Override
    public ViewHolderChampionships onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(soon.semicontato.karat.fksc.R.layout.snippet_cardview_championships, parent, false);

        return new ViewHolderChampionships(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChampionships holder, final int position) {

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

                // 1) Creates the view where the Snackbar will be displayed. (ChampionshipActivity)
                View rootView = ((ChampionshipActivity)mContext).getWindow().getDecorView()
                        .findViewById(soon.semicontato.karat.fksc.R.id.relLayout_container_activityChampionship);

                // 2) Creates the snackbar.
                final Snackbar snackbar = Snackbar.make(rootView, soon.semicontato.karat.fksc.R.string.contact_for_more_information, Snackbar.LENGTH_INDEFINITE);
                // 3) If the user clicks OK the snackbar will dismiss.
                snackbar.setAction(mContext.getString(soon.semicontato.karat.fksc.R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.setDuration(4000);
                snackbar.show();
            }
        });
    }

    @Override
    public int getItemCount() {return championshipInfos.size();}
}
