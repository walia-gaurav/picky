package edu.cmu.jsphdev.picky.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

/**
 * Helper class to process images from the device.
 */
public class UploadHelper {

    /**
     * Retrieve Bitmap from ImageCapture Intent.
     */
    public static Bitmap retrieveImageFromCamera() {

        File file = new File(Environment.getExternalStorageDirectory().toString());
        for (File iterator : file.listFiles()) {
            if (iterator.getName().equals("outputImage.jpg")) {
                file = iterator;
                break;
            }
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath(), new BitmapFactory.Options());
    }

    /**
     * Retrieve Image from Gallery.
     */
    public static Bitmap retrieveImageFromGallery(Activity activity, Intent data) {
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = activity.getContentResolver().query(data.getData(), filePath, null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String picturePath = c.getString(columnIndex);
        c.close();
        return BitmapFactory.decodeFile(picturePath);
    }

    /**
     * Update ImageView to latest Bitmap.
     */
    public static void updateImageView(ImageView imageView, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        Log.d("TESTING", bitmap.toString());
        imageView.setImageBitmap(getScaledDownImage(bitmap));
        imageView.setBackground(null);
    }

    /**
     * Scaling down image for less load on the server, and faster retrievals.
     */
    public static Bitmap getScaledDownImage(Bitmap originalImage) {
        int newHeight = (int) (originalImage.getHeight() * (512.0 / originalImage.getWidth()));
        return Bitmap.createScaledBitmap(originalImage, 512, newHeight, true);
    }
}
