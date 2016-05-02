package edu.cmu.jsphdev.picky.ws.remote.services.requests.user;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class LogoutRequest extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = LogoutRequest.class.getSimpleName();

    private Callback<Boolean> callback;

    public LogoutRequest(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/logout"));
        } catch (MalformedURLException e) {
            return false;
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("GET");

            BaseRequest.setAuthHeader(urlConnection);

            return urlConnection.getResponseCode() == BaseRequest.OK_STATUS;
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
    protected void onPostExecute(Boolean result) {
        callback.process(result);
    }
}
