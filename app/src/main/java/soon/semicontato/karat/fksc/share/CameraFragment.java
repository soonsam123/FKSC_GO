package soon.semicontato.karat.fksc.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import soon.semicontato.karat.fksc.R;
import soon.semicontato.karat.fksc.utils.ImageManager;
import soon.semicontato.karat.fksc.utils.Permissions;

public class CameraFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CameraFragment";

    // Layout
    private AppCompatButton mOpenCameraButton;

    // Vars
    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private static final int ADD_DOJO_REQUEST_CODE = 2;
    private static final int EDIT_DOJO_REQUEST_CODE = 3;
    String photoType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Creating the fragment");

        View view = inflater.inflate(R.layout.fragment_camera_share, container, false);

        // Get the intent that brought us here to discover if we are in
        // EditProfile, AddDojo or EditDojo activity by getting the photoType String.
        Intent intent = getActivity().getIntent();
        photoType = intent.getStringExtra(getResources().getString(R.string.photo_type));

        setupWidgets(view);
        setClickListeners();

        return view;
    }

    /*=========================================== Setups ===========================================*/
    /**
     * Set up the widgets to the id layout values.
     */
    private void setupWidgets(View view){
        Log.d(TAG, "setupWidgets: Setting up the widgets");

        mOpenCameraButton = view.findViewById(R.id.button_openCamera_cameraFragment);

    }

    /**
     * Set up the OnClickListeners.
     */
    private void setClickListeners(){
        Log.d(TAG, "setClickListeners: Setting the OnClickListeners");

        mOpenCameraButton.setOnClickListener(this);
    }

    // The camera opens in two cases, the first when the fragment become visible
    // and the second when the user clicks the open camera button.
    // Case 1) **************** This method is executed when the activity becomes visible to the user. ****************
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){

            // Get the intent that brought us here to discover if we are in
            // EditProfile, AddDojo or EditDojo activity by getting the photoType String.
            if (getActivity() != null) {
                Intent intent = getActivity().getIntent();
                photoType = intent.getStringExtra(getResources().getString(R.string.photo_type));


                // A) *********************** EditProfile - Changing Profile Photo ***********************
                if (photoType.equals(getResources().getString(R.string.photo_type_profile))) {

                    if (((ShareActivity) getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "setUserVisibleHint: Starting the camera");
                        // Start the Camera.
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, EDIT_PROFILE_REQUEST_CODE);
                    } else {
                        restartShareActivity();
                    }
                }


                // B) *********************** AddDojo - Adding Cover Photo ***********************
                if (photoType.equals(getResources().getString(R.string.photo_type_cover_photo_add))) {

                    if (((ShareActivity) getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "setUserVisibleHint: Starting the camera");

                        // Start the Camera
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, ADD_DOJO_REQUEST_CODE);

                    } else {
                        restartShareActivity();
                    }

                }


                // C) *********************** EditDojo - Editing Cover Photo ***********************
                if (photoType.equals(getResources().getString(R.string.photo_type_cover_photo_edit))) {

                    if (((ShareActivity) getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "setUserVisibleHint: Starting the camera");

                        // Start the Camera
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, EDIT_DOJO_REQUEST_CODE);

                    } else {
                        restartShareActivity();
                    }

                }

            }

        }

    }


    /*=========================================== END OF Setups ===========================================*/


    /*=========================================== OnClick and OnActiviyResult ===========================================*/
    /**
     * The user clicks somewhere in the screen.
     * @param view where the user clicked
     */
    @Override
    public void onClick(View view) {
        // Case 2) **************** The user click in the Open Camera button. ****************
        if (view.getId() == R.id.button_openCamera_cameraFragment){
            Log.d(TAG, "onClick: Opening the camera");


            // A) *********************** EditProfile - Editing Profile Photo ***********************
            if (photoType.equals(getResources().getString(R.string.photo_type_profile))) {

                if (getActivity() != null) {
                    if (((ShareActivity) getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "onClick: Starting the camera");

                        // Start the camera.
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, EDIT_PROFILE_REQUEST_CODE);

                    } else {
                        restartShareActivity();
                    }
                }

            }

            // B) *********************** AddDojo - Adding Cover Photo ***********************
            if (photoType.equals(getResources().getString(R.string.photo_type_cover_photo_add))) {

                if (getActivity() != null) {
                    if (((ShareActivity) getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "onClick: Starting the camera");

                        // Starts the Camera
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, ADD_DOJO_REQUEST_CODE);

                    } else {
                        restartShareActivity();
                    }
                }

            }

            // C) *********************** EditDojo - Editing Cover Photo ***********************
            if (photoType.equals(getResources().getString(R.string.photo_type_cover_photo_edit))) {

                if (getActivity() != null) {
                    if (((ShareActivity) getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])) {
                        Log.d(TAG, "onClick: Starting the camer");

                        // Starts the Camera
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intentCamera, EDIT_DOJO_REQUEST_CODE);

                    } else {
                        restartShareActivity();
                    }
                }

            }


        }
    }

    /**
     * When the user finish taking the photo.
     * @param requestCode a number to see where did I asked for this method
     * @param resultCode OK or NOT
     * @param data the intent (Activity) where it came from to this activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // A) *********************** EditProfile - Editing Profile Photo ***********************
        if (requestCode == EDIT_PROFILE_REQUEST_CODE){
            Log.i(TAG, "onActivityResult: Attenpting to save the taken photo");

            try {
                // 1) Get the bitmap from the taken picture;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                // 2) Converts the bitmap into image String URL;
                Uri uri = ImageManager.getImageUriFromBitmap(getActivity(), bitmap, 50);
                String imgURL = ImageManager.getRealPathFromUri(getActivity(), uri);

                // Returns to the activity that brought us here passing the imgURL.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getResources().getString(R.string.img_url), imgURL);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();

            } catch (NullPointerException e){e.printStackTrace();}

        }


        // B) *********************** AddProfile - Adding Cover Photo ***********************
        else if (requestCode == ADD_DOJO_REQUEST_CODE){
            Log.i(TAG, "onActivityResult: " + ADD_DOJO_REQUEST_CODE);

            try {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                Uri uri = ImageManager.getImageUriFromBitmap(getActivity(), bitmap, 50);
                String imgURL = ImageManager.getRealPathFromUri(getActivity(), uri);

                // Returns to the activity that brought us here passing the imgURL.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getResources().getString(R.string.img_url), imgURL);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();

            } catch (NullPointerException e){e.printStackTrace();}

        }


        // C) *********************** EditProfile - Editing Cover Photo ***********************
        else if (requestCode == EDIT_DOJO_REQUEST_CODE) {
            Log.i(TAG, "onActivityResult: " + EDIT_DOJO_REQUEST_CODE);

            try {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                Uri uri = ImageManager.getImageUriFromBitmap(getActivity(), bitmap, 50);
                String imgURL = ImageManager.getRealPathFromUri(getActivity(), uri);

                // Returns to the activity that brought us here passing the imgURL.
                Intent returnIntent = new Intent();
                returnIntent.putExtra(getResources().getString(R.string.img_url), imgURL);
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();

            } catch (NullPointerException e){e.printStackTrace();}

        }
    }


    /*=========================================== END OF OnClick and OnActiviyResult ===========================================*/


    /**
     * This method restart this Fragment's Activity which is ShareActivity.
     */
    private void restartShareActivity(){

        Intent intentShareActivity = new Intent(getActivity(), ShareActivity.class);
        intentShareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentShareActivity);

    }
}
