package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public class PickyHistoryService extends AsyncTask<String, Void, List<Picky>> {

    private Callback<List<Picky>> callback;

    public PickyHistoryService(Callback<List<Picky>> callback) {
        this.callback = callback;
    }

    @Override
    protected List<Picky> doInBackground(String... params) {

        URL url = null;
        try {
            url = new URL(BaseService.getAbsoluteUrl("/user/pickies"));
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
            return new Gson().fromJson(in, new TypeToken<List<Picky>>() {
            }.getType());

        } catch (IOException ex) {
            Log.d("DEBUG", ex.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Picky> myPickies) {
        callback.process(myPickies);
    }
}
