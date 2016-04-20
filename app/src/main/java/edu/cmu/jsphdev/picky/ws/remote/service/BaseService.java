package edu.cmu.jsphdev.picky.ws.remote.service;

import java.net.HttpURLConnection;

import edu.cmu.jsphdev.picky.util.CurrentSession;

public class BaseService {

    private static final String HOST = "http://192.168.0.102:8080/api";
    public static final String AUTH_HEADER = "X-Auth-Token";
    public static final String UTF8 = "UTF-8";
    public static final int OK_STATUS = 200;

    public static String getAbsoluteUrl(String path) {
        return HOST + path;
    }

    public static void setAuthHeader(HttpURLConnection urlConnection) {
        urlConnection.setRequestProperty(AUTH_HEADER, CurrentSession.getActiveUserToken());
    }

}
