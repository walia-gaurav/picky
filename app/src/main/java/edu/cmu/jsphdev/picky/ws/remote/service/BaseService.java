package edu.cmu.jsphdev.picky.ws.remote.service;

import java.net.HttpURLConnection;

import edu.cmu.jsphdev.picky.util.CurrentSession;

public class BaseService {

    public static final String IP = "http://172.29.93.234:8080";
    public static final String AUTH_HEADER = "X-Auth-Token";
    public static final String UTF8 = "UTF-8";
    public static final int OK_STATUS = 200;
    private static final String HOST_ENDPOINT = String.format("/picky-rest-amazing-servlets/api", IP);

    public static String getAbsoluteUrl(String path) {
        return HOST_ENDPOINT + path;
    }

    public static void setAuthHeader(HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty(AUTH_HEADER, CurrentSession.getActiveUserToken());
    }

}
