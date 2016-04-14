package edu.cmu.jsphdev.picky.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Photo;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.ImageDownloaderTask;
import edu.cmu.jsphdev.picky.tasks.callbacks.images.ImageDownloaderButtonCallback;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        //TODO: Get list from server
        ArrayList<Picky> pickies = new ArrayList<Picky>();

        pickies.add(getPicky());
        pickies.add(getPicky());
        PckiesAdapter pckiesAdapter = new PckiesAdapter(getActivity(), 0, pickies);
        ListView listView = (ListView) view.findViewById(R.id.profilePickyList);

        listView.setAdapter(pckiesAdapter);
        return view;
    }

    private class PckiesAdapter extends ArrayAdapter<Picky> {

        private static final String TAG = "PckiesAdapter";
        private LayoutInflater inflater = null;

        public PckiesAdapter(Activity activity, int textViewResourceId, ArrayList<Picky> pickies) {
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

            ImageDownloaderButtonCallback leftButtonCallback = new ImageDownloaderButtonCallback(getResources(), leftButton);
            ImageDownloaderButtonCallback rightButtonCallback = new ImageDownloaderButtonCallback(getResources(), rightButton);
            ImageDownloaderTask<Button> leftImageDownloaderTask = new ImageDownloaderTask<>(leftButtonCallback);
            ImageDownloaderTask<Button> rightImageDownloaderTask = new ImageDownloaderTask<>(rightButtonCallback);

            int total = picky.getLeftVotes() + picky.getRightVotes();

            leftImageDownloaderTask.execute(picky.getLeftPhoto().getUrl());
            rightImageDownloaderTask.execute(picky.getRightPhoto().getUrl());
            title.setText(picky.getTitle());
            leftVotes.setText(String.format(Locale.US, "%.2f %% (%d)", ((double) picky.getLeftVotes() * 100.0)/total, picky.getLeftVotes()));
            rightVotes.setText(String.format(Locale.US, "%.2f %% (%d)", ((double) picky.getRightVotes() * 100.0)/total, picky.getRightVotes()));
            return convertView;
        }
    }

    @NonNull
    private Picky getPicky() {
        Picky picky = new Picky();
        Photo leftPhoto = new Photo();
        Photo rightPhoto = new Photo();

        picky.setTitle("AWESOME PICKY");
        leftPhoto.setUrl("http://g-ec2.images-amazon.com/images/G/31/img15/Shoes/CatNav/p._V293117552_.jpg");
        rightPhoto.setUrl("http://www.vegetarian-shoes.co.uk/Portals/42/product/images/prd06da61c8-f8a9-402a-8590-fbec98bfbf1a.jpg");
        picky.setLeftPhoto(leftPhoto);
        picky.setRightPhoto(rightPhoto);
        picky.setLeftVotes(20);
        picky.setRightVotes(80);
        return picky;
    }

}
