package com.example.karat.fksc.Utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.karat.fksc.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;

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



    /**
     * Check if a path is an IMAGE.
     * @param path
     * @return
     */
    public static boolean isImageFile(String path){
        String mineType = URLConnection.guessContentTypeFromName(path);
        return mineType != null && mineType.startsWith("image");
    }


    /**
     * Check if a path is an VIDEO.
     * @param path
     * @return
     */
    public static boolean isVideoFile(String path) {
        String mineType = URLConnection.guessContentTypeFromName(path);
        return mineType != null && mineType.startsWith("video");
    }


    /**
     * This method gets an Uri by parsing a Bitmap
     * Convert Bitmap to Uri
     * @param context
     * @param bitmap
     * @param quality
     * @return
     */
    public static Uri getImageUriFromBitmap(Context context, Bitmap bitmap, int quality) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver()
                , bitmap, context.getString(R.string.photo_type_profile), null);
        return Uri.parse(path);

    }


    /**
     * Get the real image path by giving its Uri.
     * Converts Uri to String path.
     * @param context
     * @param uri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri uri){

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        } catch (Exception e){e.printStackTrace();}
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return null;

    }

}
