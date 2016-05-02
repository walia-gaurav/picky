package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public interface PickyServiceInterface {

    void delete(String id, Callback<Boolean> callback);
    void getHistory(Callback<List<Picky>> callback);
    void next(Callback<Picky>  callback);
    void upload(Picky picky, Callback<Boolean> callback);
    void vote(String id, Vote vote, Callback<Boolean> callback);

}
