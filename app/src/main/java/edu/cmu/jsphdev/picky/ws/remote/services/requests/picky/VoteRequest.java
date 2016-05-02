package edu.cmu.jsphdev.picky.ws.remote.services.requests.picky;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class VoteRequest extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = VoteRequest.class.getSimpleName();

    private Callback<Boolean> callback;

    public VoteRequest(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/picky/vote"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            String urlParameters = String.format("id=%s&vote=%s", params[0], params[1]);
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

            return urlConnection.getResponseCode() != BaseRequest.OK_STATUS;

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
