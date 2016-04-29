package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public class TimelineService extends AsyncTask<String, Void, List<Picky>>  {

    private Callback<List<Picky>> callback;

    public TimelineService(Callback<List<Picky>> callback) {
        this.callback = callback;
    }

    @Override
    protected List<Picky> doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseService.getAbsoluteUrl("/picky/timeline"));
        } catch (MalformedURLException e) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("GET");

            urlConnection.setUseCaches(false);
            BaseService.setAuthHeader(urlConnection);
            int responseCode = urlConnection.getResponseCode();

            return null;
        } catch (IOException ex) {
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(List<Picky> result) {
        callback.process(result);
    }

}
