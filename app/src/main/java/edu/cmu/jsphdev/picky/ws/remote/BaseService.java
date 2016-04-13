package edu.cmu.jsphdev.picky.ws.remote;

import com.loopj.android.http.AsyncHttpClient;

public class BaseService {

    protected static AsyncHttpClient client = new AsyncHttpClient();
    protected static final String HOST = "http://localhost:8080";

    protected String getAbsoluteUrl(String path) {
        return HOST + path;
    }

}
