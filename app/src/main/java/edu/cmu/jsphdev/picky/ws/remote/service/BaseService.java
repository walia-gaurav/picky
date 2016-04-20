package edu.cmu.jsphdev.picky.ws.remote.service;

/**
 * Common webservice to interact with the server.
 */
public class BaseService {

    private static final String HOST = "http://192.168.0.102:8080/api";
    public static final String UTF8 = "UTF-8";
    public static final int OK_STATUS = 200;

    public static String getAbsoluteUrl(String path) {
        return HOST + path;
    }

}
