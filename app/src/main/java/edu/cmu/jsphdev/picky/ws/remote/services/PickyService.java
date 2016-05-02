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
        DeleteRequest deleteRequest = new DeleteRequest(callback);

        deleteRequest.execute(id);
    }

    @Override
    public void history(Callback<List<Picky>> callback) {
        HistoryRequest historyRequest = new HistoryRequest(callback);

        historyRequest.execute();
    }

    @Override
    public void next(Callback<Picky> callback) {
        TimelineRequest timelineRequest = new TimelineRequest(callback);

        timelineRequest.execute();
    }

    @Override
    public void upload(Picky picky, Callback<Boolean> callback) {
        UploadRequest uploadRequest = new UploadRequest(callback);

        uploadRequest.execute(picky);
    }

    @Override
    public void vote(String id, Vote vote, Callback<Boolean> callback) {
        VoteRequest voteRequest = new VoteRequest(callback);

        voteRequest.execute(id, vote.name());
    }

}
