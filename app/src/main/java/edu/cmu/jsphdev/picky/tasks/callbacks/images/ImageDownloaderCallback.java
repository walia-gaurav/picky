package edu.cmu.jsphdev.picky.tasks.callbacks.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.View;

import java.util.List;

public abstract class ImageDownloaderCallback<T> {

    protected static final String TAG = ImageDownloaderCallback.class.getSimpleName();

    protected final T leftElement;
    protected final T rightElement;
    protected final Resources resources;

    public ImageDownloaderCallback(Resources resources, T leftElement, T rightElement) {
        this.leftElement = leftElement;
        this.rightElement = rightElement;
        this.resources = resources;
    }

    public abstract void process(List<Bitmap> bitmap);

}
