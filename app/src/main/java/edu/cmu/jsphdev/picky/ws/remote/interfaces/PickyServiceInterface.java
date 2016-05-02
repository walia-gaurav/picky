package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

/**
 * Different methods needed to interact with Pickies.
 */
public interface PickyServiceInterface {

    /**
     * Deletes Picky
     */
    void delete(String id, Callback<Boolean> callback);

    /**
     * Get user history.
     */
    void history(Callback<List<Picky>> callback);

    /**
     * Returns next user Picky.
     */
    void next(Callback<Picky>  callback);

    /**
     * Uploads a Picky.
     */
    void upload(Picky picky, Callback<Boolean> callback);

    /**
     * Vote for a Picky.
     */
    void vote(String id, Vote vote, Callback<Boolean> callback);

}
