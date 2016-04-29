package edu.cmu.jsphdev.picky.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.ImageDownloaderTask;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.tasks.callbacks.images.ImageDownloaderButtonCallback;
import edu.cmu.jsphdev.picky.ws.remote.service.PickyHistoryService;

/**
 * TabFragment to display user's profile page.
 */
public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Callback<List<Picky>> callback = new Callback<List<Picky>>() {
            @Override
            public void process(List<Picky> pickies) {
                if (pickies != null && !pickies.isEmpty()) {

                    PickiesAdapter pickiesAdapter = new PickiesAdapter(getActivity(), 0, pickies);
                    ListView listView = (ListView) view.findViewById(R.id.profilePickyList);
                    listView.setAdapter(pickiesAdapter);
                }
            }
        };
        PickyHistoryService pickyHistoryService = new PickyHistoryService(callback);
        pickyHistoryService.execute();

        return view;
    }

    private class PickiesAdapter extends ArrayAdapter<Picky> {

        private static final String TAG = "PickiesAdapter";
        private LayoutInflater inflater = null;

        public PickiesAdapter(Activity activity, int textViewResourceId, List<Picky> pickies) {
            super(activity, textViewResourceId, pickies);
            try {
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            } catch (Exception ex) {
                Log.e(TAG, "Problem getting LayoutInflater", ex);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.list_item_profile, null);
            }
            Picky picky = getItem(position);
            Button leftButton = (Button) convertView.findViewById(R.id.leftChoiceButton);
            Button rightButton = (Button) convertView.findViewById(R.id.rightChoiceButton);
            TextView title = (TextView) convertView.findViewById(R.id.profilePickyTitle);
            TextView leftVotes = (TextView) convertView.findViewById(R.id.leftVotes);
            TextView rightVotes = (TextView) convertView.findViewById(R.id.rightVotes);

            ImageDownloaderButtonCallback leftButtonCallback = new ImageDownloaderButtonCallback(getResources(),
                    leftButton);
            ImageDownloaderButtonCallback rightButtonCallback = new ImageDownloaderButtonCallback(getResources(),
                    rightButton);
            ImageDownloaderTask<Button> leftImageDownloaderTask = new ImageDownloaderTask<>(leftButtonCallback);
            ImageDownloaderTask<Button> rightImageDownloaderTask = new ImageDownloaderTask<>(rightButtonCallback);

            int total = picky.getLeftVotes() + picky.getRightVotes();

            leftImageDownloaderTask.execute(picky.getLeftPhoto().getUrl());
            rightImageDownloaderTask.execute(picky.getRightPhoto().getUrl());
            title.setText(picky.getTitle());
            leftVotes.setText(String.format(Locale.US, "%.2f %% (%d)", ((double) picky.getLeftVotes() * 100.0) /
                    total, picky.getLeftVotes()));
            rightVotes.setText(String.format(Locale.US, "%.2f %% (%d)", ((double) picky.getRightVotes() * 100.0) /
                    total, picky.getRightVotes()));
            return convertView;
        }
    }

}
