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

/**
 * Created by walia-mac on 4/21/16.
 */
public class SignUpService extends AsyncTask<String, Void, User> {

    private Callback<User> callback;

    public SignUpService(Callback<User> callback) {
        this.callback = callback;
    }

    @Override
    protected User doInBackground(String... params) {
        URL url = null;
        try {
            url = new URL(BaseService.getAbsoluteUrl("/signup"));
        } catch (MalformedURLException e) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        try {
            String username = params[0];
            String password = params[1];
            String urlParameters = String.format("username=%s&password=%s", username, password);
            byte[] postData = urlParameters.getBytes(BaseService.UTF8);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setInstanceFollowRedirects(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("charset", BaseService.UTF8);
            urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            urlConnection.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

            wr.write(postData);
            wr.flush();
            wr.close();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode != BaseService.OK_STATUS) {
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            Gson gson = new Gson();

            return gson.fromJson(in, User.class);
        } catch (IOException ex) {
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
