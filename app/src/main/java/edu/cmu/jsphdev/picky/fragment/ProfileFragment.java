package edu.cmu.jsphdev.picky.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.service.PickyHistoryService;

/**
 * TabFragment to display user's profile page.
 */
public class ProfileFragment extends Fragment {

    View view;
    private PickiesAdapter pickiesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        Callback<List<Picky>> callback = new Callback<List<Picky>>() {
            @Override
            public void process(List<Picky> pickies) {
                if (pickies != null && !pickies.isEmpty()) {
                    pickiesAdapter = new PickiesAdapter(getActivity(), pickies);
                    ListView listView = (ListView) view.findViewById(R.id.profilePickyList);
                    listView.setAdapter(pickiesAdapter);
                }
            }
        };
        new PickyHistoryService(callback).execute();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "YAYAYAY", Toast.LENGTH_LONG).show();
    }
}
