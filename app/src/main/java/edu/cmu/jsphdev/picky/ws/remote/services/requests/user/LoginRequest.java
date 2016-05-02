package edu.cmu.jsphdev.picky.ws.remote.services.requests.user;

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

import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.BaseRequest;

public class LoginRequest extends AsyncTask<String, Void, User> {

    private Callback<User> callback;

    public LoginRequest(Callback<User> callback) {
        this.callback = callback;
    }

    @Override
    protected User doInBackground(String... params) {

        URL url = null;
        try {
            url = new URL(BaseRequest.getAbsoluteUrl("/login"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            String username = params[0];
            String password = params[1];
            String urlParameters = String.format("username=%s&password=%s", username, password);
            byte[] postData = urlParameters.getBytes(BaseRequest.UTF8);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", BaseRequest.UTF8);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.write(postData);
            wr.flush();
            wr.close();

            if (urlConnection.getResponseCode() != BaseRequest.OK_STATUS) {
                return null;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            return new Gson().fromJson(in, User.class);
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
    protected void onPostExecute(User user) {
        callback.process(user);
    }
}
