package edu.cmu.jsphdev.picky.ws.remote;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyWebServiceInterface;

public class PickyWebService extends BaseService implements PickyWebServiceInterface {

    @Override
    public void nextPicky(JsonHttpResponseHandler jsonHttpResponseHandler) {
        RequestParams requestParams = new RequestParams();
        client.get(getAbsoluteUrl("/picky"), requestParams, jsonHttpResponseHandler);
    }

    @Override
    public Picky vote(int pickyId, Vote vote) {
        return null;
    }

    @Override
    public List<Picky> myPickies() {
        return null;
    }

}
