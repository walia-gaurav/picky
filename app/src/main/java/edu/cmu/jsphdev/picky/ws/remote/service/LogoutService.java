package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;

public class LogoutService extends AsyncTask<String, Void, Boolean>  {

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
            urlConnection.setRequestMethod("GET");

            urlConnection.setUseCaches(false);
            BaseService.setAuthHeader(urlConnection);
            int responseCode = urlConnection.getResponseCode();

            return responseCode == BaseService.OK_STATUS;
        } catch (IOException ex) {
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
