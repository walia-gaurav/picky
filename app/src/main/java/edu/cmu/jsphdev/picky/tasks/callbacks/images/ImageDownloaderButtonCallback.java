package edu.cmu.jsphdev.picky.tasks.callbacks.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

import java.util.List;

import edu.cmu.jsphdev.picky.R;

public class ImageDownloaderButtonCallback extends ImageDownloaderCallback<Button> {

    public ImageDownloaderButtonCallback(Resources resources, Button leftButton, Button rightButton) {
        super(resources, leftButton, rightButton);
    }

    @Override
    public void process(List<Bitmap> bitmaps) {
        setImageToElement(leftElement, bitmaps.get(0));
        setImageToElement(rightElement, bitmaps.get(0));
    }

    private void setImageToElement(Button element, Bitmap image) {
        if (element != null) {
            if (image != null) {
                element.setBackground(new BitmapDrawable(resources, image));
            } else {
                element.setBackground(resources.getDrawable(R.drawable.broken_image));
            }
        }
    }

}
