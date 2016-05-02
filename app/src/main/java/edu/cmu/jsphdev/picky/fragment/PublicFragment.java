package edu.cmu.jsphdev.picky.fragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.PickyService;

/**
 * TabFragment to display the picky wall.
 */
public class PublicFragment extends Fragment implements SensorEventListener {

    private static final String TAG = PublicFragment.class.getSimpleName();

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float[] gravity;
    private float currentX;
    private float lastX;
    private boolean isRegistered;
    private long lastUpdate;

    private PickyServiceInterface pickyService;

    private TextView titleTextView;
    private Button leftButton;
    private Button rightButton;
    private Picky picky;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public, container, false);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        currentX = SensorManager.GRAVITY_EARTH;
        lastX = SensorManager.GRAVITY_EARTH;
        lastUpdate = 0;

        pickyService = new PickyService();

        leftButton = (Button) view.findViewById(R.id.leftChoiceButton);
        rightButton = (Button) view.findViewById(R.id.rightChoiceButton);
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Internet permission is required", Toast.LENGTH_LONG).show();
            return view;
        }
        loadPicky();

        return view;
    }

    /**
     * Refreshes this fragment.
     */
    public void refresh() {
        loadPicky();
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
                        vote(button, vote);
                        break;
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

    /**
     * This methods casts the vote on the server-side.
     *
     * @param button
     * @param vote
     */
    private void vote(Button button, Vote vote) {
        button.setText(R.string.picked);
        Callback<Boolean> callback = new Callback<Boolean>() {
            @Override
            public void process(Boolean element) {
                loadPicky();
            }
        };
        pickyService.vote(String.valueOf(picky.getId()), vote, callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerSensor();
    }

    /**
     * Registering accelerometer sensor.
     */
    private void registerSensor() {
        if (CurrentSession.isTiltActive()) {
            isRegistered = true;
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterSensor();
    }

    /**
     * Evicting registration of accelerometer.
     */
    private void unregisterSensor() {
        if (isRegistered) {
            isRegistered = false;
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualTime = System.currentTimeMillis();

            if ((actualTime - lastUpdate) > 100) {
                gravity = event.values.clone();
                // Shake detection
                float x = gravity[0];
                lastUpdate = actualTime;
                lastX = currentX;
                currentX = x;
                float delta = Math.abs(currentX - lastX);
                if (delta > 3 && Math.abs(currentX) > 3) {
                    if (currentX <= 0) {
                        vote(rightButton, Vote.RIGHT);
                    } else {
                        vote(leftButton, Vote.LEFT);
                    }
                }
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void enableButtons() {
        ((LinearLayout) getActivity().findViewById(R.id.frame)).setClickable(false);
        leftButton.setText("");
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        rightButton.setText("");
    }

    private void disableButtons() {
        ((LinearLayout) getActivity().findViewById(R.id.frame)).setClickable(true);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
    }

    /**
     * Helper method to fetch more pickies.
     */
    private void loadPicky() {
        Callback<Picky> callback = new Callback<Picky>() {
            @Override
            public void process(Picky pickyResult) {
                picky = pickyResult;
                if (pickyResult != null) {
                    ImageDownloaderButtonCallback buttonsCallback = new ImageDownloaderButtonCallback(getResources(),
                            leftButton, rightButton);
                    ImageDownloaderTask<Button> buttonsDownloaderTask = new ImageDownloaderTask<>(buttonsCallback);

                    buttonsDownloaderTask.execute(pickyResult.getLeftPhoto().getUrl(), pickyResult.getRightPhoto()
                            .getUrl());
                    titleTextView.setText(pickyResult.getTitle());
                    enableButtons();
                    customTouchListener(leftButton, Vote.LEFT);
                    customTouchListener(rightButton, Vote.RIGHT);
                    registerSensor();
                } else {
                    disableButtons();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No more pickies", Toast.LENGTH_LONG).show();
                    unregisterSensor();

                }
            }
        };
        pickyService.next(callback);
    }

}
