package com.example.karat.fksc.utils;

import android.os.Environment;

public class FilePaths {

    // "storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    // "storage/emulated/0/Pictures"
    public String PICTURES = ROOT_DIR + "/Pictures";

    // "storage/emulated/0/DCIM/camera
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

    // "photos/users/user_id/profile_photo or cover_photo"
    public String FIREBASE_STORAGE_PATH = "photos/users";
}
