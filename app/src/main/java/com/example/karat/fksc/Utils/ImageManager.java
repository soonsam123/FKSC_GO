package com.example.karat.fksc.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ImageManager {

    private static final String TAG = "ImageManager";

    /**
     * Convert an imgURL into a BitMap.
     * @param imgURL
     * @return
     */
    public static Bitmap getBitMapFromImgURL(String imgURL){

        // 1) Create the file, fileInputStream and the Bitmap.
        File imageFile = new File(imgURL);
        FileInputStream fileInputStream = null;
        Bitmap bitmap = null;

        try {
            // 1) Instantiate fileInpuStream within the File;
            // 2) Decode the fileInputStream to a bitmap.
            fileInputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fileInputStream);

        } catch (FileNotFoundException e){
            Log.d(TAG, "getBitMapFromImgURL: FileNotFoundException: " + e.getMessage());
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e){
                Log.d(TAG, "getBitMapFromImgURL: IOException: " + e.getMessage());
            }
        }

        return bitmap;

    }


    /**
     * Convert a Bitmap image into Bytes;
     * quality is the quality of the image and it can be from 0 to 100
     * @param bitmap
     * @param quality
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);

        return stream.toByteArray();
    }

}
