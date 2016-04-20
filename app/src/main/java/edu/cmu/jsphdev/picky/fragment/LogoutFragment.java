package edu.cmu.jsphdev.picky.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.activity.MainActivity;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.service.LogoutService;

/**
 * LogoutFragment to handle loggin out of the user.
 */
public class LogoutFragment extends Fragment {

    private Button okButton;
    private Button cancelButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        okButton = (Button) view.findViewById(R.id.okButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        /*
        Takes you the login page, after successful logout.
         */
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback<Boolean>  callback = new Callback<Boolean>() {
                    @Override
                    public void process(Boolean element) {
                        if (!element) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Problem performing logout", Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(getActivity(), MainActivity.class);

                        startActivity(intent);
                    }
                };
                LogoutService logoutService = new LogoutService(callback);

                logoutService.execute();
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
