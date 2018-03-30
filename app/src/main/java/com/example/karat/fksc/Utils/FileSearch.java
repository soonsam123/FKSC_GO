package com.example.karat.fksc.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.karat.fksc.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StreamCorruptedException;
import java.net.URLConnection;
import java.util.ArrayList;

public class FileSearch {

    /**
     * Returns all the directories that are inside a specific directorie.
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();

        // E.G directory = "storage/emulated/0/Pictures
        // 1) Create the directory and list all (Files or Directories) inside it.
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        // 2) Loop through each one and check if it is a directory.
        // 3) If so, add to the list.
        for (int i = 0; i < listfiles.length; i++){
            if (listfiles[i].isDirectory()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }


    /**
     * Returns all the files paths that are inside a directory
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory){

        ArrayList<String> pathArray = new ArrayList<>();

        // 1) Create the directory and list all (Files or Directories) inside it.
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        // 2) Loop through each one and check if it is a file.
        // 3) If so, add to the list.
        for (int i = 0; i < listfiles.length; i++){
            if (listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }

        return pathArray;

    }

}
