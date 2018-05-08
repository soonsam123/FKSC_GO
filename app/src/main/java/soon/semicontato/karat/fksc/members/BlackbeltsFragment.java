package soon.semicontato.karat.fksc.members;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.FirebaseMethods;
import soon.semicontato.karat.fksc.utils.RecyclerAdapterBlackBelts;
import soon.semicontato.karat.fksc.models.UserAndUserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by karat on 06/03/2018.
 */

public class BlackbeltsFragment extends Fragment{

    private static final String TAG = "BlackbeltsFragment";

    // Layout
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout_PleaseWait;

    // Firebase
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;

    // Vars
    private List<UserAndUserSettings> all_users;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting the fragment");
        
        View view = inflater.inflate(R.layout.fragment_blackbelts_members, container, false);

        setupWidgets(view);
        setupFirebaseAuth();

        return view;
    }

    /*====================================== Setups ======================================*/
    /**
     * Set up the RecyclerAdapter to show the: Profile image, Name, DojoName and BeltColor.
     * @param all_users a list with all "users" and "user_settings" node information
     *                  from all the user's registered in the app
     */
    private void setupRecyclerAdapter(List<UserAndUserSettings> all_users){
        Log.i(TAG, "setupRecyclerAdapter: " + all_users);

        // List only for the VERIFIED BLACK BELTS.
        List<UserAndUserSettings> all_users_blackBelts = new ArrayList<>();

        for (UserAndUserSettings sampleUser: all_users){

            // 1) Get the first 5 letters of belt_color;
            // 2) Pass it to lower case;
            // 3) Check if the result is equal to "preta" (which means black belt);
            // 4) Display only the black belt users.
            if (sampleUser.getUserSettings().getBelt_color().length() >= 5 ) {

                // "Preta 3º Dan" --> "preta"
                String belt_color = sampleUser.getUserSettings().getBelt_color().substring(0, 5).toLowerCase();

                // a. User is black belt.
                if (belt_color.equals(getString(R.string.black_belt))){

                    // b. User is verified.
                    if (sampleUser.getUser().isVerified()) {

                        // c. Add User.
                        all_users_blackBelts.add(sampleUser);
                    }
                }
            }
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapterBlackBelts(all_users_blackBelts, getActivity()));

    }

    /**
     * Set up the widgets to the Layout id values.
     * @param view get the activity view to be able to access the activity's widgets
     *             from inside the fragment
     */
    private void setupWidgets(View view){

        recyclerView = view.findViewById(R.id.recyclerView_fragmentBlackBelts);
        relativeLayout_PleaseWait = view.findViewById(R.id.relLayout_progressBar_snippetPleaseWait);
        all_users = new ArrayList<>();

    }
    /*====================================== END OF Setups ======================================*/


    /*====================================== Firebase ======================================*/

    /**
     * This methos set up the Firebase Authentication for users.
     */
    private void setupFirebaseAuth(){
        Log.i(TAG, "setupFirebaseAuth: Setting up FirebaseAuth");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(getActivity());


    }

    /**
     * Life-cycle 2) This method is called after the onCreate.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Get database reference.
        myRef = FirebaseDatabase.getInstance().getReference();

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 1) Get a list with all the users;
                all_users = firebaseMethods.getAllUserAndUserSettings(dataSnapshot);

                // 2) Dismiss the progressBar;
                relativeLayout_PleaseWait.setVisibility(View.GONE);

                // 3) Set up the recycler adapter with the users' list.
                setupRecyclerAdapter(all_users);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        // Attach the listener to the reference.
        myRef.addListenerForSingleValueEvent(listener);

    }

    /**
     * Life-cycle 3) This method is called when the activity becomes completed invisible.
     */
    @Override
    public void onStop() {

        if (myRef != null && listener != null){
            myRef.removeEventListener(listener);
        }
        super.onStop();
    }


    /*====================================== END OF Firebase ======================================*/

}
