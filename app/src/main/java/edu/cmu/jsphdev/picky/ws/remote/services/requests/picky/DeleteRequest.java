package edu.cmu.jsphdev.picky.ws.remote.services.requests.picky;

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
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class DeleteRequest extends AsyncTask<String, Void, Picky> {

    private static final String TAG = DeleteRequest.class.getSimpleName();

    private Callback<Boolean> callback;

    public DeleteRequest(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Picky doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/picky/delete"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            String urlParameters = String.format("id=%s", params[0]);
            byte[] postData = urlParameters.getBytes(BaseRequest.UTF8);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", BaseRequest.UTF8);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));

            BaseRequest.setAuthHeader(urlConnection);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();

            if (urlConnection.getResponseCode() != BaseRequest.OK_STATUS) {
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return new Gson().fromJson(in, Picky.class);

        } catch (IOException ex) {
            Log.e(TAG, "Problem making the request", ex);
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
