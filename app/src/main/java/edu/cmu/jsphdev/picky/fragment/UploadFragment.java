package edu.cmu.jsphdev.picky.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;

import edu.cmu.jsphdev.picky.R;

public class UploadFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        Button uploadButton = (Button) view.findViewById(R.id.uploadButton);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabHost tabHost = (TabHost) getActivity().findViewById(R.id.homeTabHost);

                tabHost.setCurrentTab(0);
            }
        });
        return view;
    }

}
