package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;

public interface PickyProducerWebServiceInterface {


    List<Picky> myPickies();

    boolean markCompleted(int pickyId);

    void uploadPicky(Picky picky);

    boolean deletePicky(int pickyId);


}
