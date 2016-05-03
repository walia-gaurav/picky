package edu.cmu.jsphdev.picky.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.util.CurrentSession;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        /*
        Handling for Tilt switch.
         */
        Switch tiltCheckbox = (Switch) view.findViewById(R.id.tiltButton);
        tiltCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                User user = CurrentSession.getActiveUser();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

                user.setTiltActive(isChecked);
                editor.putString("existingUser", (new Gson()).toJson(user));
                editor.apply();
            }
        });
        tiltCheckbox.setChecked(CurrentSession.isTiltActive());

        /*
        Handling UpdatePassword button click.
         */
        (view.findViewById(R.id.updatePasswordButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdatePasswordFragment().show(getFragmentManager(), "");
            }
        });

        /*
        Handling Logout button click.
         */
        (view.findViewById(R.id.logoutButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LogoutFragment().show(getFragmentManager(), "");
            }
        });

        return view;
    }

}
