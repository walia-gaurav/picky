package edu.cmu.jsphdev.picky.ws.remote.services.requests.picky;

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
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class HistoryRequest extends AsyncTask<String, Void, List<Picky>> {

    private static final String TAG = HistoryRequest.class.getSimpleName();

    private Callback<List<Picky>> callback;

    public HistoryRequest(Callback<List<Picky>> callback) {
        this.callback = callback;
    }

    @Override
    protected List<Picky> doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/user/pickies"));
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
            return new Gson().fromJson(in, new TypeToken<List<Picky>>() {
            }.getType());

        } catch (IOException ex) {
            Log.e("ERROR", ex.getMessage());
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
