package edu.cmu.jsphdev.picky.ws.remote;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyConsumerWebServiceInterface;

/**
 * Implementation to interact with the server.
 */
public class PickyWebService extends BaseService implements PickyConsumerWebServiceInterface {

    @Override
    public void nextPicky(JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams requestParams = new RequestParams();
        client.get(getAbsoluteUrl("/picky"), requestParams, jsonHttpResponseHandler);
    }

    @Override
    public Picky vote(int pickyId, Vote vote) {
        return null;
    }

}
