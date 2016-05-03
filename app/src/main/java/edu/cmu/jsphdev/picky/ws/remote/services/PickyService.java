package edu.cmu.jsphdev.picky.ws.remote.services;

import java.util.List;

import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.picky.DeleteRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.picky.HistoryRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.picky.TimelineRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.picky.UploadRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.picky.VoteRequest;

public class PickyService implements PickyServiceInterface {

    @Override
    public void delete(String id, Callback<Boolean> callback) {
        new DeleteRequest(callback).execute(id);
    }

    @Override
    public void history(Callback<List<Picky>> callback) {
        new HistoryRequest(callback).execute();
    }

    @Override
    public void next(Callback<Picky> callback) {
        new TimelineRequest(callback).execute();
    }

    @Override
    public void upload(Picky picky, Callback<Boolean> callback) {
        new UploadRequest(callback).execute(picky);
    }

    @Override
    public void vote(String id, Vote vote, Callback<Boolean> callback) {
        new VoteRequest(callback).execute(id, vote.name());
    }

}
