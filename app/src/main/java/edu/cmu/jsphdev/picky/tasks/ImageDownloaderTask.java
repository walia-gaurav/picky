package edu.cmu.jsphdev.picky.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.jsphdev.picky.tasks.callbacks.images.ImageDownloaderCallback;

/**
 * Async Task to download an image from server.
 *
 * @param <T>
 */
public class ImageDownloaderTask<T> extends AsyncTask<String, Void, List<Bitmap>> {

    private final ImageDownloaderCallback<T> callback;

    public ImageDownloaderTask(ImageDownloaderCallback<T> callback) {
        this.callback = callback;
    }

    private static Bitmap downloadBitmap(String url) {

        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();

            if (statusCode != 200) {
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected List<Bitmap> doInBackground(String... params) {
        List<Bitmap> images = new ArrayList<>();
        images.add(downloadBitmap(params[0]));
        images.add(downloadBitmap(params[1]));
        return images;
    }

    @Override
    protected void onPostExecute(List<Bitmap> bitmaps) {
        if (isCancelled()) {
            bitmaps = null;
        }
        callback.process(bitmaps);
    }

}
