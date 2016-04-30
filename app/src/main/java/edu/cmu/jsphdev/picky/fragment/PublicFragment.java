package edu.cmu.jsphdev.picky.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import edu.cmu.jsphdev.picky.entities.Photo;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.ImageDownloaderTask;
import edu.cmu.jsphdev.picky.tasks.callbacks.images.ImageDownloaderButtonCallback;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.PickyConsumerWebServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.service.ConsumerService;

/**
 * TabFragment to display the picky wall.
 */
public class PublicFragment extends Fragment {

    private PickyConsumerWebServiceInterface pickyService;
    private TextView titleTextView;
    private Button leftButton;
    private Button rightButton;

    public PublicFragment() {
        pickyService = new ConsumerService();
    }

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
                    "Internet Permission is required", Toast.LENGTH_LONG).show();
            return view;
        }
        loadPicky();
        return view;
    }

    /**
     * Custom TouchListener to visually represent selection of a picky.
     *
     * @param button
     */
    private void customTouchListener(final Button button) {
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
                        button.setText("picked !");
                        ((LinearLayout) getActivity().findViewById(R.id.frame)).setClickable(true);
                        leftButton.setEnabled(false);
                        rightButton.setEnabled(false);
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

    private void loadPicky() {
//TODO: Complete callback.
//        pickyService.nextPicky(new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//            }
//
//        });

//TODO: Remove wired data.
        Picky picky = getPicky();

//        ImageDownloaderButtonCallback buttonCallback = new ImageDownloaderButtonCallback(getResources(),
//                leftButton, rightButton);
//        ImageDownloaderTask<Button> imageDownloaderTask = new ImageDownloaderTask<>(buttonCallback);
//        imageDownloaderTask.execute(picky.getLeftPhoto().getUrl(), picky.getRightPhoto().getUrl());

        titleTextView.setText(picky.getTitle());
        customTouchListener(leftButton);
        customTouchListener(rightButton);
    }

    @NonNull
    private Picky getPicky() {
        Picky picky = new Picky();
        Photo leftPhoto = new Photo();
        Photo rightPhoto = new Photo();

        picky.setTitle("AWESOME PICKY");
        leftPhoto.setUrl("http://g-ec2.images-amazon.com/images/G/31/img15/Shoes/CatNav/p._V293117552_.jpg");
        rightPhoto.setUrl("http://www.vegetarian-shoes.co" +
                ".uk/Portals/42/product/images/prd06da61c8-f8a9-402a-8590-fbec98bfbf1a.jpg");
        picky.setLeftPhoto(leftPhoto);
        picky.setRightPhoto(rightPhoto);
        return picky;
    }

}
