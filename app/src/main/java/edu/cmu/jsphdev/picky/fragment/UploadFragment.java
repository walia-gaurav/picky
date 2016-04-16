package edu.cmu.jsphdev.picky.fragment;


import android.Manifest;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.Location;
import edu.cmu.jsphdev.picky.entities.Photo;
import edu.cmu.jsphdev.picky.entities.Picky;
import edu.cmu.jsphdev.picky.ws.remote.service.ProducerService;

/**
 * TabFragment for Uploading pickies (two pictures and a title)
 */
public class UploadFragment extends Fragment {

    private static final String[] ALL = {
            Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .WRITE_EXTERNAL_STORAGE
    };
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

                if (leftPicky.getBackground() == null && rightPicky.getBackground() == null) {
                    Toast.makeText(getActivity(), "Uploading", Toast.LENGTH_SHORT).show();

                    Picky picky = new Picky();
                    picky.setTitle("My first picky");
                    picky.setLocation(new Location(1d, 1d));
                    picky.setLeftPhoto(new Photo(getBinaryStreamFromImageView(leftPicky)));
                    picky.setRightPhoto(new Photo(getBinaryStreamFromImageView(rightPicky)));

                    new ProducerService().uploadPicky(picky);

                } else {
                    Toast.makeText(getActivity(), "Upload Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private byte[] getBinaryStreamFromImageView(ImageView leftPicky) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) leftPicky.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100,
                stream);
        return stream.toByteArray();
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

}
