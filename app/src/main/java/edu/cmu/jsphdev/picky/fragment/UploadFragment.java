package edu.cmu.jsphdev.picky.fragment;


import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import edu.cmu.jsphdev.picky.R;

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


        return view;
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
                ((TextView) view.findViewById(R.id.selectedButtonForCapture)).setText(String.valueOf(imageId));
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
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
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
