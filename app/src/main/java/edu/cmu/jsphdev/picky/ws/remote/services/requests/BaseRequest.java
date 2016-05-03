package edu.cmu.jsphdev.picky.ws.remote.services.requests;

import java.net.HttpURLConnection;

import edu.cmu.jsphdev.picky.util.CurrentSession;

/**
 * Basic server properties needed by other services.
 */
public class BaseRequest {

    public static final String IP = "http://172.29.92.93:8080";
    public static final String AUTH_HEADER = "X-Auth-Token";
    public static final String UTF8 = "UTF-8";
    public static final int OK_STATUS = 200;
    private static final String HOST_ENDPOINT = String.format("%s/api", IP);

    /**
     * Returns the server-endpoint for the context passed.
     *
     * @param path
     * @return
     */
    public static String getAbsoluteUrl(String path) {
        return HOST_ENDPOINT + path;
    }

    /**
     * Sets the current user as the authorization header in URLConnection.
     *
     * @param urlConnection
     */
    public static void setAuthHeader(HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty(AUTH_HEADER, CurrentSession.getActiveUserToken());
    }

}
