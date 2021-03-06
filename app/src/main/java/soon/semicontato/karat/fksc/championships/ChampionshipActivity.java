package soon.semicontato.karat.fksc.championships;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.BottomNavigationHelper;
import soon.semicontato.karat.fksc.utils.FirebaseMethods;
import soon.semicontato.karat.fksc.utils.RecyclerAdapterChampionships;
import soon.semicontato.karat.fksc.models.ChampionshipInfo;
import soon.semicontato.karat.fksc.utils.UniversalImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import soon.semicontato.karat.fksc.models.ChampionshipInfo;
import soon.semicontato.karat.fksc.utils.BottomNavigationHelper;
import soon.semicontato.karat.fksc.utils.FirebaseMethods;
import soon.semicontato.karat.fksc.utils.UniversalImageLoader;

/**
 * Created by karat on 06/03/2018.
 */

public class ChampionshipActivity extends AppCompatActivity {

    private static final String TAG = "ChampionshipActivity";

    // Layout
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout_PleaseWait;

    // Context
    private Context mContext = ChampionshipActivity.this;

    // Firebase
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;
    private ValueEventListener listener;


    /**
     * Life-cycle 1) First method to run.
     * @param savedInstanceState save the state of the application, or it's null
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_championship);
        Log.i(TAG, "onCreate: Starting activity");

        setupFirebaseAuth();
        initImageLoader();
        setupWidgets();
        setupBottomNavigationView();
        setupToolBar();

    }


    /*=================================================== Setups ===================================================*/
    /**
     * Set up my customized toolbar to be the Support Action Bar for the Championship Activity.
     */
    private void setupToolBar(){

        setSupportActionBar(toolbar);

    }

    /**
     * Setting up the widgets to the layout values.
     */
    private void setupWidgets(){
        Log.i(TAG, "setupWidgets: Setting up the widgets");

        toolbar = findViewById(R.id.toolBar_layout_top_bar);

        recyclerView = findViewById(R.id.recyclerView_activityChampionship);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        relativeLayout_PleaseWait = findViewById(R.id.relLayout_progressBar_snippetPleaseWait);

    }


    /**
     * Enable the user to change from one activity to another when clicking in the Bottom Navigation View items.
     */
    private void setupBottomNavigationView(){
        Log.i(TAG, "setupBottomNavigationView: Setting up the Bottom Navigation View");

        BottomNavigationHelper.enablePagination(mContext, bottomNavigationView);
        BottomNavigationHelper.removeShiftMode(bottomNavigationView);


    }

    /**
     * Set up the recycler adapter with all the championships that are
     * registered in the "championships" node. The children of this node
     * will be: championship1, championship2, championship3 and so on.
     * @param championshipInfos list with all the "championships" node children
     */
    private void setupRecyclerAdapter(List<ChampionshipInfo> championshipInfos){
        Log.i(TAG, "setupRecyclerAdapter: Setting up the recycler adapter");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecyclerAdapterChampionships(championshipInfos, mContext));
    }
    /*=================================================== END OF Setups ===================================================*/


    /*==================================== Initialize ====================================*/

    public void initImageLoader(){
        Log.i(TAG, "initImageLoader: Initializing the imageLoader");

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    /*==================================== END OF Initialize ====================================*/


    /*=================================================== Firebase ===================================================*/

    /**
     * Set up the Firebase User Authentication and the Database Reference.
     */
    private void setupFirebaseAuth(){

        firebaseMethods = new FirebaseMethods(mContext);
        myRef = FirebaseDatabase.getInstance().getReference();

    }

    /**
     * Life-cycle 2) Runs right after the onCreate method.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Starting");
        
        /*==== Code to make the selected item use a different color ====*/
        // Members = 0 / Championship = 1 / Ranking = 2 / Profile = 3
        int ACTIVITY_NUM = 1;
        // 1) Get the bottomNavigationView menu;
        Menu menu = bottomNavigationView.getMenu();
        // 2) Get the current item;
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        // 3) Select it, so it'll display a different color for this icon.
        menuItem.setChecked(true);

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // 1) Get a list with all the Championships;
                List<ChampionshipInfo> allChampionshipsInfo = firebaseMethods.getAllChampionshipsInfo(dataSnapshot);

                // 2) Dismiss the progressBar;
                relativeLayout_PleaseWait.setVisibility(View.GONE);

                // 3) Set up the Recycler Adapter with the championships' list.
                setupRecyclerAdapter(allChampionshipsInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(listener);
    }

    /**
     * Runs when the activity is 100% invisible.
     */
    @Override
    protected void onStop() {
        super.onStop();

        if (listener != null && myRef != null){
            myRef.removeEventListener(listener);
        }

    }


    /*=================================================== END OF Firebase ===================================================*/

}
