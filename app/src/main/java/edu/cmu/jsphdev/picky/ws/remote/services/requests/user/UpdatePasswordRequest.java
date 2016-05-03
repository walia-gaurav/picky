package edu.cmu.jsphdev.picky.ws.remote.services.requests.user;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class UpdatePasswordRequest extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = UpdatePasswordRequest.class.getSimpleName();

    private Callback<Boolean> callback;

    public UpdatePasswordRequest(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/user/password"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            String password = params[0];
            String urlParameters = String.format("password=%s", password);
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
