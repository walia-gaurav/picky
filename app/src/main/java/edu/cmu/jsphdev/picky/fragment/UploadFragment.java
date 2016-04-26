package edu.cmu.jsphdev.picky.fragment;


import android.app.AlertDialog.Builder;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Photo;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.ws.remote.service.UploadPickyService;

/**
 * TabFragment for Uploading pickies (two pictures and a title)
 */
public class UploadFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        selectImageOnClick(R.id.choice1);
        selectImageOnClick(R.id.choice2);

        ((Button) view.findViewById(R.id.uploadPicky)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView leftPicky = (ImageView) view.findViewById(R.id.choice1);
                ImageView rightPicky = (ImageView) view.findViewById(R.id.choice2);
                String title = ((EditText) view.findViewById(R.id.descriptionEditText)).getText().toString().trim();

                if (leftPicky.getBackground() == null && rightPicky.getBackground() == null) {
                    Toast.makeText(getActivity(), "Uploading", Toast.LENGTH_SHORT).show();

                    /*
                    Fetching LastKnownLocation
                     */
                    Location l = null;
                    LocationManager locManager = (LocationManager) getActivity().getSystemService(getActivity()
                            .LOCATION_SERVICE);
                    try {
                        l = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    } catch (SecurityException e) {
                        Log.d("WARN", "No location permissions");
                    }

                    /*
                    Prepareing Picky Data.
                     */
                    Picky picky = new Picky();
                    picky.setTitle(title);
                    picky.setLeftPhoto(new Photo(getBase64StringFromImageView(leftPicky)));
                    picky.setRightPhoto(new Photo(getBase64StringFromImageView(rightPicky)));
                    if (null != l) {
                        picky.setLocation(new edu.cmu.jsphdev.picky.entities.Location(l.getLatitude(), l.getLongitude
                                ()));
                    }


                    Callback<Boolean> callback = new Callback<Boolean>() {
                        @Override
                        public void process(Boolean result) {
                            if (result) {
                                //Clear upload frame
                                Toast.makeText(getActivity(), "Upload Successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    };

                    UploadPickyService uploadService = new UploadPickyService(callback);
                    String json = (new Gson()).toJson(picky);
                    uploadService.execute(json);

                } else {
                    Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

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
        });

        builder.show();
    }

    /**
     * Using PackageManager to check for permission grants.
     *
     * @param perm
     * @return
     */
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getActivity(), perm));
    }
}
