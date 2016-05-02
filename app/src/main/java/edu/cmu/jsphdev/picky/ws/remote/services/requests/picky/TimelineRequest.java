package edu.cmu.jsphdev.picky.ws.remote.services.requests.picky;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class TimelineRequest extends AsyncTask<String, Void, Picky> {

    private static final String TAG = TimelineRequest.class.getSimpleName();

    private Callback<Picky> callback;

    public TimelineRequest(Callback<Picky> callback) {
        this.callback = callback;
    }

    @Override
    protected Picky doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/picky/timeline"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");

            BaseRequest.setAuthHeader(urlConnection);

            if (urlConnection.getResponseCode() != BaseRequest.OK_STATUS) {
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return new Gson().fromJson(in, Picky.class);

        } catch (IOException ex) {
            Log.e(TAG, "Problem making the request", ex);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Picky result) {
        callback.process(result);
    }

}
