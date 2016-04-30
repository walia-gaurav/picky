package edu.cmu.jsphdev.picky.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.service.PickyHistoryService;

/**
 * TabFragment to display user's profile page.
 */
public class ProfileFragment extends Fragment {

    private View view;
    private PickiesAdapter pickiesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        loadPickyHistory();
        return view;
    }

    public void loadPickyHistory() {
        Callback<List<Picky>> callback = new Callback<List<Picky>>() {
            @Override
            public void process(List<Picky> pickies) {
                if (pickies != null && !pickies.isEmpty()) {
                    if (null != pickiesAdapter && pickiesAdapter.getPickies().containsAll(pickies) && pickies
                            .containsAll(pickiesAdapter.getPickies())) {
                        return;
                    }
                    pickiesAdapter = new PickiesAdapter(getActivity(), pickies);
                    ListView listView = (ListView) view.findViewById(R.id.profilePickyList);
                    listView.setAdapter(pickiesAdapter);
                }
            }
        };
        new PickyHistoryService(callback).execute();
    }
}
