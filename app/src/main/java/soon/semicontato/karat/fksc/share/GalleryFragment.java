package soon.semicontato.karat.fksc.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.FilePaths;
import soon.semicontato.karat.fksc.utils.FileSearch;
import soon.semicontato.karat.fksc.utils.FirebaseMethods;
import soon.semicontato.karat.fksc.utils.GridImageAdapter;
import soon.semicontato.karat.fksc.utils.ImageManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;


public class GalleryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "GalleryFragment";

    // Layout
    private Toolbar mToolBar;
    private Spinner mSpinner;

    private ImageView mBackButton;
    private ImageView mCheckButton;
    private ImageView mPreviewPhoto;

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private TextView mNoPictures;
    private TextView mSwipeToTakePictures;


    // Vars
    private ArrayList<String> directories;
    private static final int NUM_GRID_COLUMNS = 3;
    private String mAppend = "file:/";
    private String imgURL;

    // Firebase
    private FirebaseMethods firebaseMethods;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery_share, container, false);

        setupWidgets(view);
        setupToolBar();
        setupClickListeners();
        init();

        return view;
    }





    /*======================================== Init ========================================*/

    /**
     * Initialize the fragment by setting up the spinner and the gridView.
     */
    private void init(){

        FilePaths filePaths = new FilePaths();

        // 1) ********* Get all the directories and insert into an ArrayList *********.
        // Getting directories inside Pictures.
        // E.G = Pictures/Calisthenics && Pictures/Games ...
        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            directories = (FileSearch.getDirectoryPaths(filePaths.PICTURES));
        }

        // There is only one directory in Camera.
        directories.add(filePaths.CAMERA);


        // 2) ********* Exclude the empty directories *********.
        // Create a new directories ArrayList based on the previous one.
        final ArrayList<String> directoriesNotEmpty = new ArrayList<>(directories);

        // Removes the directories that are empty. (There are no pictures inside)
        // Remember it must be from backwards side (10 --> 0 instead of 0 --> 10)
        // by this way when you delete one item it doesn't disturb the others.
        for (int i = directories.size() - 1; i >= 0; i--) {
            if (FileSearch.getFilePaths(directories.get(i)).isEmpty()) {
                Log.i(TAG, "init: Removing Directory: " + directories.get(i));
                directoriesNotEmpty.remove(i);
            }
        }


        // 3) ********* Get only the final name of the directories *********.
        // Get only the final path name of all the directories
        // E.G = storage/emulated/0/Pictures/Calisthenics --> Calisthenics
        // If we are running in a device that has no Pictures directories and the Camera
        // directory is empty, then directories array will be null. We need to check if directories is null,
        // if so, just skip this part.
        final ArrayList<String> directoriesName = new ArrayList<>();

        for (int i = 0; i < directoriesNotEmpty.size(); i++) {

            int index = directoriesNotEmpty.get(i).lastIndexOf("/") + 1;
            String name = directoriesNotEmpty.get(i).substring(index);
            directoriesName.add(name);


        }



        // 4) ********* Set up the Spinner *********.
        // Set the directories to the spinner.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, directoriesName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        // Execute this task when the user select some item in the spinner.
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: selected " + directoriesNotEmpty.get(i));
                setupGridView(directoriesNotEmpty.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // 5) ********* Display a "NO PICTURES" message if the user has no files (Pictures) *********.
        // This happens when the phone has 0 directories, and then 0 pictures.
        if (directoriesNotEmpty.size() == 0){
            Log.i(TAG, "init: Displaying NO PICTURES Message");
            displayNoPicturesMessage();
        }

    }

    /*======================================== END OF Init ========================================*/


    /*======================================== Setups ========================================*/

    /**
     * Set up the widgets with the id layout values.
     * @param view the fragment view so we can access the widgets
     */
    private void setupWidgets(View view){
        Log.d(TAG, "setupWidgets: Setting up the widgets");

        mToolBar = view.findViewById(R.id.toolBar_topBarGallery);
        mSpinner = view.findViewById(R.id.spinner_topBarGallery);

        mPreviewPhoto = view.findViewById(R.id.imgView_previewPhoto_GalleryFragment);
        mBackButton = view.findViewById(R.id.imgView_back_topBarGallery);
        mCheckButton = view.findViewById(R.id.imgView_check_topBarGallery);

        mGridView = view.findViewById(R.id.gridView_GalleryFragment);
        mProgressBar = view.findViewById(R.id.progressBar_GalleryFragment);

        mNoPictures = view.findViewById(R.id.txtView_noPictures_GalleryFragment);
        mSwipeToTakePictures = view.findViewById(R.id.txtView_takeNewPictures_GalleryFragment);

        directories = new ArrayList<>();

        firebaseMethods = new FirebaseMethods(getActivity());

    }

    /**
     * Set up the layout toolbar to be the Default Support Action Bar.
     */
    private void setupToolBar(){
        Log.i(TAG, "setupToolBar: Setting up the toolbar");
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(mToolBar);
        }
    }

    /**
     * Set up the OnClickListeners.
     */
    private void setupClickListeners(){
        Log.i(TAG, "setupClickListeners: Setting up the click listeners");
        mBackButton.setOnClickListener(this);
        mCheckButton.setOnClickListener(this);
    }

    /**
     * The user clicks somewhere in the screen./
     * @param view where the user clicked
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgView_back_topBarGallery){
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else if (view.getId() == R.id.imgView_check_topBarGallery){
            if (imgURL != null) {

                // ===== Get the intent from the activity that brought us here in GalleryFragment (ShareActivity). =====
                Intent intent = getActivity().getIntent();
                String photoType = intent.getStringExtra(getResources().getString(R.string.photo_type));
                Log.i(TAG, "onClick: PhotoType" + photoType);

                // Case 1) ****** It came from Edit Profile Activity. ******
                if (photoType.equals(getResources().getString(R.string.photo_type_profile))) {
                    Log.d(TAG, "onClick: Profile Photo from EditProfile");

                    // Returns to the activity that brought us here passing the imgURL.
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(getString(R.string.img_url), imgURL);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                }

                // Case 2) ****** It came from Add Dojo Activity. ******
                else if (photoType.equals(getResources().getString(R.string.photo_type_cover_photo_add))){
                    Log.d(TAG, "onClick: Cover Photo from AddDojo");

                    // Returns to the activity that brought us here passing the imgURL.
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(getString(R.string.img_url), imgURL);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();

                }

                // Case 3) ****** It came from Edit Dojo Activity. ******
                else if (photoType.equals(getResources().getString(R.string.photo_type_cover_photo_edit))) {
                    Log.d(TAG, "onClick: Cover Photo from EditDojo");

                    // Returns to the activity that brought us here passing the imgURL.
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(getResources().getString(R.string.img_url), imgURL);
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();

                }
            } else {
                Toast.makeText(getActivity(), R.string.choose_a_picture, Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Set up the gridView with the images of the selected directory in the spinner.
     * @param selectedDirectory a string which is a directory path
     */
    private void setupGridView(String selectedDirectory){
        Log.i(TAG, "setupGridView: Setting up the gridView with " + selectedDirectory);

        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        // 1) ********** Set the grid column width. **********
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        mGridView.setColumnWidth(imageWidth);


        // 2) ********** Add only the images from imgURLs to a new Array List. **********
        // a) Creates a new array list that will store only the images.
        final ArrayList<String> imgURLsOnlyImages = new ArrayList<>();

        for (String singleImgURL: imgURLs){
            // b) Add only images to the new Array List.
            if (ImageManager.isImageFile(singleImgURL)){
                imgURLsOnlyImages.add(singleImgURL);
            }
        }


        // 3) ********** Set up the GridView. **********
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_image, mAppend, imgURLsOnlyImages);
        mGridView.setAdapter(adapter);


        // 4) ********** Set the Preview Image Box to be the first one of the directories. **********
        if (imgURLsOnlyImages.size() > 0) {
            setImage(imgURLsOnlyImages.get(0), mPreviewPhoto, mAppend);
            imgURL = imgURLsOnlyImages.get(0);
        }

        // 5) ********** Set the Preview Image Box to be the one the user clicked in the gridView. **********
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (imgURLsOnlyImages.size() >= i) {
                    // a) Set the Preview Image Box to the one the use clicked.
                    // b) Update the global imgURL which is the last one the user has selected.
                    setImage(imgURLsOnlyImages.get(i), mPreviewPhoto, mAppend);
                    imgURL = imgURLsOnlyImages.get(i);
                }
            }
        });


    }

    /**
     * Set some image to some imageView.
     * @param imgURL an Url of some image
     * @param imageView a widget ImageView where the url will be placed
     * @param append the append for the url, it could be "https://" or "file:/" ...
     */
    private void setImage(String imgURL, ImageView imageView, String append){
        Log.i(TAG, "setImage: Setting some image to the Preview Image Box " + imgURL);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }
    /*======================================== END OF Setups ========================================*/


    /*======================================== Display ========================================*/

    /**
     * Display a message saying "You have no pictures, swipe to the side to take a new Picture!",
     * when the user hasn't any directories and any files.
     */
    private void displayNoPicturesMessage(){
        mProgressBar.setVisibility(View.GONE);
        mNoPictures.setVisibility(View.VISIBLE);
        mSwipeToTakePictures.setVisibility(View.VISIBLE);
    }

    /*======================================== END OF Display ========================================*/

}
