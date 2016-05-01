package edu.cmu.jsphdev.picky.fragment;


import android.Manifest;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Photo;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.service.UploadPickyService;

/**
 * TabFragment for Uploading pickies (two pictures and a title)
 */
public class UploadFragment extends Fragment {

    private View view;
    private ImageView leftPicky;
    private ImageView rightPicky;
    private EditText title;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload, container, false);

        /* Initializing location manager. */
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        /*
        Permissions check.
         */
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) !=
                        PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity().getApplicationContext(),
                    "Write to external source, gps and internet permissions are required", Toast.LENGTH_LONG).show();
            return view;
        }

        selectImageOnClick(R.id.choice1);
        selectImageOnClick(R.id.choice2);

        /*
        Event handling for OnClick of the upload buttons.
         */
        ((Button) view.findViewById(R.id.uploadPicky)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                leftPicky = (ImageView) view.findViewById(R.id.choice1);
                rightPicky = (ImageView) view.findViewById(R.id.choice2);
                title = (EditText) view.findViewById(R.id.descriptionEditText);

                if (leftPicky.getBackground() == null && rightPicky.getBackground() == null) {
                    Toast.makeText(getActivity(), "Uploading", Toast.LENGTH_SHORT).show();

                    /* Fetching LastKnownLocation */
                    Location location = getLastKnownLocation();

                    /* Preparing Picky Data. */
                    Picky picky = new Picky();
                    picky.setTitle(title.getText().toString().trim());
                    picky.setLeftPhoto(new Photo(getBase64StringFromImageView(leftPicky)));
                    picky.setRightPhoto(new Photo(getBase64StringFromImageView(rightPicky)));
                    if (null != location) {
                        picky.setLocation(new edu.cmu.jsphdev.picky.entities.Location(location.getLatitude(),
                                location.getLongitude()));
                    }

                    /*
                    Calling server-side to upload.
                     */
                    Callback<Boolean> callback = new Callback<Boolean>() {
                        @Override
                        public void process(Boolean result) {
                            if (result) {
                                Toast.makeText(getActivity(), "Upload Successful!", Toast.LENGTH_SHORT).show();
                                refreshUploadFragment();
                            } else {
                                Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    };
                    new UploadPickyService(callback).execute(picky);

                } else {
                    Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    /**
     * Check different providers and returns the best location. If location is null
     * throws an exception.
     */
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;

        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }

    /**
     * Refreshes upload view after a successful server-upload.
     */
    private void refreshUploadFragment() {
        leftPicky.setBackground(getResources().getDrawable(R.drawable.add_photo_icon));
        rightPicky.setBackground(getResources().getDrawable(R.drawable.add_photo_icon));
        leftPicky.setImageDrawable(null);
        rightPicky.setImageDrawable(null);
        title.setText("");
    }

    /**
     * Returns a Base-64 encoded string for an ImageView.
     *
     * @param leftPicky
     * @return
     */
    private String getBase64StringFromImageView(ImageView leftPicky) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) leftPicky.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Starting process of image capture.
     *
     * @param imageId
     */
    private void selectImageOnClick(final int imageId) {
        ImageView image = (ImageView) view.findViewById(imageId);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) view.findViewById(R.id.selectedPicky)).setText(String.valueOf(imageId));
                selectImage(imageId);
            }
        });
    }

    /**
     * Generating a dialog box for the user to select capture type of image.
     *
     * @param imageId
     */
    private void selectImage(final int imageId) {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        Builder builder = new Builder(getActivity());
        builder.setTitle("Add a picky!");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "outputImage.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    getActivity().startActivityForResult(intent, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    getActivity().startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        }).show();
    }

}
