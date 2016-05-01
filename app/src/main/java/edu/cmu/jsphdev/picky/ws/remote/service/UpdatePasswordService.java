package edu.cmu.jsphdev.picky.ws.remote.service;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public class UpdatePasswordService extends AsyncTask<String, Void, Boolean> {

    private Callback<Boolean> callback;

    public UpdatePasswordService(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        URL url = null;
        try {
            url = new URL(BaseService.getAbsoluteUrl("/user/password"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            String password = params[0];
            String urlParameters = String.format("password=%s", password);
            byte[] postData = urlParameters.getBytes(BaseService.UTF8);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", BaseService.UTF8);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();

            return urlConnection.getResponseCode() != BaseService.OK_STATUS;

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
