package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public class PickyDeleteService extends AsyncTask<String, Void, Picky> {


    private Callback<Boolean> callback;

    public PickyDeleteService(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Picky doInBackground(String... params) {

        URL url = null;
        try {
            url = new URL(BaseService.getAbsoluteUrl("/picky/delete"));
        } catch (MalformedURLException e) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        try {
            String pickyId = params[0];
            String urlParameters = String.format("id=%s", pickyId);
            byte[] postData = urlParameters.getBytes(BaseService.UTF8);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", BaseService.UTF8);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));

            BaseService.setAuthHeader(urlConnection);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

            wr.write(postData);
            wr.flush();
            wr.close();

            if (urlConnection.getResponseCode() != BaseService.OK_STATUS) {
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return new Gson().fromJson(in, Picky.class);

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
    protected void onPostExecute(Picky picky) {
        callback.process(picky != null);
    }
}
