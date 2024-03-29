package edu.cmu.jsphdev.picky.ws.remote.services.requests.picky;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import edu.cmu.jsphdev.picky.entities.Photo;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class UploadRequest extends AsyncTask<Picky, Void, Boolean> {

    private static final String TAG = UploadRequest.class.getSimpleName();

    private Callback<Boolean> callback;

    public UploadRequest(Callback<Boolean> callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(Picky... pickies) {
        URL url;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/picky/upload"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            Picky picky = pickies[0];
            Photo leftPhoto = picky.getLeftPhoto();
            Photo rightPhoto = picky.getRightPhoto();
            leftPhoto.setBase64Image(URLEncoder.encode(leftPhoto.getBase64Image(), "UTF-8"));
            rightPhoto.setBase64Image(URLEncoder.encode(rightPhoto.getBase64Image(), "UTF-8"));

            String urlParameters = String.format("picky=%s", new Gson().toJson(picky));
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
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        callback.process(isSuccess);
    }

}
