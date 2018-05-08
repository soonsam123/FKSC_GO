package soon.semicontato.karat.fksc.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import soon.semicontato.karat.fksc.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by karat on 12/03/2018.
 */

public class ViewHolder extends RecyclerView.ViewHolder{

    public CircleImageView circleImageView;
    public TextView fullName;
    public TextView dojo;
    public TextView registrationNumber;
    public LinearLayout linearLayout_eachItem;


    public ViewHolder(View itemView) {
        super(itemView);

        // Cast the variables using itemView.
        circleImageView = itemView.findViewById(R.id.circleImageView_snippetAffiliates);
        fullName = itemView.findViewById(R.id.fullName_snippetAffiliates);
        dojo = itemView.findViewById(R.id.dojo_snippetAffiliates);
        registrationNumber = itemView.findViewById(R.id.registrationNumber_snippetAffiliates);
        linearLayout_eachItem = itemView.findViewById(R.id.linearLayout_container_snippetAffiliates);
    }
}
