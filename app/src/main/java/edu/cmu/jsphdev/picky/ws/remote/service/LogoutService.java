package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public class LogoutService extends AsyncTask<String, Void, Boolean> {

    private Callback<Boolean> callback;

    public LogoutService(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        URL url = null;
        try {
            url = new URL(BaseService.getAbsoluteUrl("/logout"));
        } catch (MalformedURLException e) {
            return false;
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");

            BaseService.setAuthHeader(urlConnection);

            return urlConnection.getResponseCode() == BaseService.OK_STATUS;
        } catch (IOException ex) {
            Log.e("ERROR", ex.getMessage());
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        callback.process(result);
    }
}
