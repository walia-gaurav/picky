package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public class TimelineService extends AsyncTask<String, Void, Picky>  {

    private Callback<Picky> callback;

    public TimelineService(Callback<Picky> callback) {
        this.callback = callback;
    }

    @Override
    protected Picky doInBackground(String... params) {
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

            if (responseCode != BaseService.OK_STATUS) {
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Gson gson = new Gson();

            return gson.fromJson(in, Picky.class);
        } catch (IOException ex) {
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
