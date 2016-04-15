package edu.cmu.jsphdev.picky.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.activity.MainActivity;

/**
 * LogoutFragment to handle loggin out of the user.
 */
public class LogoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        Button okButton = (Button) view.findViewById(R.id.okButton);
        Button cancelButton = (Button) view.findViewById(R.id.cancelButton);

        /*
        Takes you the login page, after successful logout.
         */
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        /*
        Takes you to the home page on cancel event.
         */
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabHost tabHost = (TabHost) getActivity().findViewById(R.id.homeTabHost);

                tabHost.setCurrentTab(0);
            }
        });

        return view;
    }

}
