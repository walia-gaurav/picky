package edu.cmu.jsphdev.picky.tasks.callbacks.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public class ImageDownloaderButtonCallback extends ImageDownloaderCallback<Button> {

    public ImageDownloaderButtonCallback(Resources resources, Button reference) {
        super(resources, reference);
    }

    @Override
    public void process(Bitmap bitmap) {
        if (element != null) {
            if (bitmap != null) {
                Drawable drawable = new BitmapDrawable(resources, bitmap);

                element.setBackground(drawable);
            } else {
                //TODO: Set placeholder
            }
        }
    }

}
