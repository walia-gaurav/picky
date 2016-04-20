package edu.cmu.jsphdev.picky.ws.remote.service;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyConsumerWebServiceInterface;

/**
 * Implementation to interact with the server.
 */
public class ConsumerService extends BaseService implements PickyConsumerWebServiceInterface {

    @Override
    public void nextPicky() {}

    @Override
    public Picky vote(int pickyId, Vote vote) {
        return null;
    }

}
