package edu.cmu.jsphdev.picky.ws.remote.service;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyProducerWebServiceInterface;

public class ProducerService extends BaseService implements PickyProducerWebServiceInterface {

    @Override
    public List<Picky> myPickies() {
        return null;
    }

    @Override
    public boolean markCompleted(int pickyId) {
        return false;
    }

    @Override
    public void uploadPicky(Picky picky) {

    }

    @Override
    public boolean deletePicky(int pickyId) {
        return false;
    }
}
