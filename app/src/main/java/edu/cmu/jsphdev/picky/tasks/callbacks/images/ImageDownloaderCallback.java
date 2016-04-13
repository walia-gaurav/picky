package edu.cmu.jsphdev.picky.tasks.callbacks.images;

import android.content.res.Resources;
import android.graphics.Bitmap;

public abstract class ImageDownloaderCallback<T> {

    protected final T element;
    protected final Resources resources;

    public ImageDownloaderCallback(Resources resources, T element) {
        this.element = element;
        this.resources = resources;
    }

    public abstract void process(Bitmap bitmap);

}
