package com.example.karat.fksc.EditProfile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karat.fksc.R;

/**
 * Created by karat on 14/03/2018.
 */

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = "EditProfileActivity";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        Log.i(TAG, "onCreate: Stating the activity");

    }
}
