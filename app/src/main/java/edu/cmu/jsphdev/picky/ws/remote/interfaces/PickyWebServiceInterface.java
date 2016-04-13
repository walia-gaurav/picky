package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import com.loopj.android.http.JsonHttpResponseHandler;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;

public interface PickyWebServiceInterface {

    void nextPicky(JsonHttpResponseHandler jsonHttpResponseHandler);
    Picky vote(int pickyId, Vote vote);
    List<Picky> myPickies();

}
