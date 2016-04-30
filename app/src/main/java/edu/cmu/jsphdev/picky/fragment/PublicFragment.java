package edu.cmu.jsphdev.picky.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.entities.Vote;
import edu.cmu.jsphdev.picky.tasks.ImageDownloaderTask;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.tasks.callbacks.images.ImageDownloaderButtonCallback;
import edu.cmu.jsphdev.picky.ws.remote.service.TimelineService;
import edu.cmu.jsphdev.picky.ws.remote.service.VoteService;

/**
 * TabFragment to display the picky wall.
 */
public class PublicFragment extends Fragment {

    private Picky picky;
    private TextView titleTextView;
    private Button leftButton;
    private Button rightButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public, container, false);


        leftButton = (Button) view.findViewById(R.id.leftChoiceButton);
        rightButton = (Button) view.findViewById(R.id.rightChoiceButton);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED)  {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Internet permission is required", Toast.LENGTH_LONG).show();
            return view;
        }
        customTouchListener(leftButton, Vote.LEFT);
        customTouchListener(rightButton, Vote.RIGHT);
        loadPicky();
        return view;
    }

    /**
     * Custom TouchListener to visually represent selection of a picky.
     *
     * @param button
     */
    private void customTouchListener(final Button button, final Vote vote) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        button.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        button.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        button.setText(R.string.picked);
                        Callback<Boolean> callback = new Callback<Boolean>() {
                            @Override
                            public void process(Boolean element) {
                                loadPicky();
                            }
                        };
                        VoteService voteService = new VoteService(callback);
                        voteService.execute(String.format("%d", picky.getId()), vote.name());
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        button.getBackground().clearColorFilter();
                        button.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void disableButtons() {
        ((LinearLayout) getActivity().findViewById(R.id.frame)).setClickable(true);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
    }

    private void loadPicky() {
        Callback<Picky> callback = new Callback<Picky>() {
            @Override
            public void process(Picky pickyResult) {
                picky = pickyResult;
                if (pickyResult != null) {
                    ImageDownloaderButtonCallback buttonsCallback = new ImageDownloaderButtonCallback(getResources(),
                            leftButton, rightButton);
                    ImageDownloaderTask<Button> buttonsDownloaderTask = new ImageDownloaderTask<>(buttonsCallback);

                    buttonsDownloaderTask.execute(pickyResult.getLeftPhoto().getUrl(), pickyResult.getRightPhoto().getUrl());
                    titleTextView.setText(pickyResult.getTitle());
                } else {
                    disableButtons();
                }
            }
        };
        TimelineService timelineService = new TimelineService(callback);
        timelineService.execute();
    }

}
