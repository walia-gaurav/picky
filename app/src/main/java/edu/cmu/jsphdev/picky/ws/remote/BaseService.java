package edu.cmu.jsphdev.picky.ws.remote;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Common webservice to interact with the server.
 */
public class BaseService {

    protected static final String HOST = "http://localhost:8080";
    protected static AsyncHttpClient client = new AsyncHttpClient();

    protected String getAbsoluteUrl(String path) {
        return HOST + path;
    }

}
