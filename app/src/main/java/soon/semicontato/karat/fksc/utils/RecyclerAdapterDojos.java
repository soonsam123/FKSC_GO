package soon.semicontato.karat.fksc.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soon.semicontato.karat.fksc.dojo.DojoActivity;
import soon.semicontato.karat.fksc.dojo.EditDojoActivity;
import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.models.DojoInfoAndSettings;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


/**
 * Created by karat on 17/03/2018.
 */

public class RecyclerAdapterDojos extends RecyclerView.Adapter<ViewHolderSquareImage> {

    private List<DojoInfoAndSettings> dojos;
    private Context mContext;
    private FirebaseMethods firebaseMethods;
    private FirebaseAuth mAuth;

    public RecyclerAdapterDojos(List<DojoInfoAndSettings> dojos, Context mContext) {
        this.dojos = dojos;
        this.mContext = mContext;
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public  ViewHolderSquareImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snippet_affiliates_dojos_recyclerview, parent, false);

        return new ViewHolderSquareImage(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSquareImage holder, final int position) {

        final DojoInfoAndSettings sampleDojo = dojos.get(position);

        holder.dojoName.setText(sampleDojo.getDojoInfo().getName());
        holder.city.setText(sampleDojo.getDojoInfo().getCity());
        holder.registrationNumber.setText(sampleDojo.getDojoInfo().getRegistration_number());
        UniversalImageLoader.setImage(sampleDojo.getDojoInfo().getCover_img_url(),
                holder.imageView, null, "");


        // Case 1) ****************** CLICK in the item ******************
        holder.linearLayout_eachItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDojo = new Intent(mContext, DojoActivity.class);
                intentDojo.putExtra(mContext.getString(R.string.field_user_id), sampleDojo.getDojoSettings().getUser_id());
                intentDojo.putExtra(mContext.getString(R.string.field_dojo_number), sampleDojo.getDojoSettings().getDojo_number());
                mContext.startActivity(intentDojo);
            }
        });

        // Case 1) ****************** LONG CLICK in the item ******************
        // Check if user is logged in.
        if (mAuth.getCurrentUser() != null) {

            // Allow the user to edit and delete dojos only if he is the owner.
            if (sampleDojo.getDojoSettings().getUser_id().equals(mAuth.getCurrentUser().getUid())) {

                holder.linearLayout_eachItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        AlertDialog.Builder builder;
                        firebaseMethods = new FirebaseMethods(mContext);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(mContext);
                        }
                        builder.setTitle(sampleDojo.getDojoInfo().getName())
                                .setMessage(mContext.getString(R.string.what_to_do_with_this_dojo))
                                .setPositiveButton(mContext.getString(R.string.edit), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        // Navigate to EditDojo Activity.
                                        Intent intentEditDojo = new Intent(mContext, EditDojoActivity.class);

                                        // Put as extras variables the userID and the dojoNumber to know
                                        // what specific single dojo to display in the editText fields.
                                        intentEditDojo.putExtra(mContext.getString(R.string.field_user_id),
                                                sampleDojo.getDojoSettings().getUser_id());
                                        intentEditDojo.putExtra(mContext.getString(R.string.field_dojo_number),
                                                sampleDojo.getDojoSettings().getDojo_number());

                                        // Starts the activity.
                                        mContext.startActivity(intentEditDojo);
                                    }
                                })
                                .setNegativeButton(mContext.getString(R.string.delete), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        // 1) Remove selected dojo.
                                        firebaseMethods.removeDojo(sampleDojo.getDojoSettings().getDojo_number());

                                        // 2) Refresh the activity;
                                        ((Activity) mContext).finish();
                                        (mContext).startActivity(((Activity) mContext).getIntent());
                                    }
                                })
                                .show();

                        return false;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {return dojos.size();}
}
