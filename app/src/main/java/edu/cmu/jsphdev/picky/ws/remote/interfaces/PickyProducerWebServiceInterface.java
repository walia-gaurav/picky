package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;

/**
 * Interface for all picky producers.
 */
public interface PickyProducerWebServiceInterface {


    /**
     * Gets the list of all pickies of a user.
     *
     * @return
     */
    List<Picky> myPickies();

    /**
     * Marks a picky as complete.
     *
     * @param pickyId
     * @return
     */
    boolean markCompleted(int pickyId);

    /**
     * Service to upload a picky to the server.
     *
     * @param picky
     */
    void uploadPicky(Picky picky);

    /**
     * Deletes a picky from the server.
     *
     * @param pickyId
     * @return
     */
    boolean deletePicky(int pickyId);

}
