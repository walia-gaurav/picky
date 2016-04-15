package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import com.loopj.android.http.JsonHttpResponseHandler;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;

/**
 * Interface for all picky consumers.
 */
public interface PickyConsumerWebServiceInterface {

    /**
     * Asks for the next picky from the server.
     *
     * @param jsonHttpResponseHandler
     */
    void nextPicky(JsonHttpResponseHandler jsonHttpResponseHandler);

    /**
     * Casts a vote for a picky.
     *
     * @param pickyId
     * @param vote
     * @return
     */
    Picky vote(int pickyId, Vote vote);


}
