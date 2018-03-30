package com.example.karat.fksc.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.fksc.R;
import com.example.karat.fksc.Utils.FileSearch;
import com.example.karat.fksc.Utils.FirebaseMethods;
import com.example.karat.fksc.Utils.Permissions;

public class CameraFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CameraFragment";

    // Layout
    private AppCompatButton mOpenCameraButton;

    // Firebase
    private FirebaseMethods firebaseMethods;

    // Vars
    private static final int CAMERA_REQUEST_CODE = 5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Creating the fragment");

        View view = inflater.inflate(R.layout.fragment_camera_share, container, false);

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

        firebaseMethods = new FirebaseMethods(getActivity());
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
    // Case 1) This method is executed when the activity becomes visible to the user.
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){

            if (((ShareActivity)getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])){
                Log.d(TAG, "setUserVisibleHint: Starting the camera");
                // Start the Camera.
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);
            } else {
                // Restart ShareActivity;
                Intent intentShareActivity = new Intent(getActivity(), ShareActivity.class);
                intentShareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentShareActivity);
            }

        }

    }


    /*=========================================== END OF Setups ===========================================*/


    /*=========================================== OnClick and OnActiviyResult ===========================================*/
    /**
     * The user clicks somewhere in the screen.
     * @param view
     */
    @Override
    public void onClick(View view) {
        // Case 1) The user click in the Open Camera button.
        if (view.getId() == R.id.button_openCamera_cameraFragment){
            Log.d(TAG, "onClick: Opening the camera");

            if (((ShareActivity)getActivity()).checkSinglePermission(Permissions.CAMERA_PERMISSION[0])){
                // Start the camera.
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);

            } else {
                // Restart ShareActivity;
                Intent intentShareActivity = new Intent(getActivity(), ShareActivity.class);
                intentShareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentShareActivity);
            }
        }
    }

    /**
     * When the user finish taking the photo.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE){
            Log.i(TAG, "onActivityResult: Attenpting to save the taken photo");

            try {
                // 1) Get the bitmap from the taken picture;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                // 2) Converts the bitmap into image String URL;
                Uri uri = FileSearch.getImageUriFromBitmap(getActivity(), bitmap, 50);
                String imgURL = FileSearch.getRealPathFromUri(getActivity(), uri);

                // 3) Upload the photo to Firebase Storage.
                firebaseMethods.uploadNewPhoto(getResources().getString(R.string.photo_type_profile), imgURL);

            } catch (NullPointerException e){e.printStackTrace();}

        }

    }


    /*=========================================== END OF OnClick and OnActiviyResult ===========================================*/
}
