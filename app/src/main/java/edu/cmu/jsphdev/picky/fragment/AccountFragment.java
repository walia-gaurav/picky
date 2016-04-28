package edu.cmu.jsphdev.picky.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.service.UpdatePasswordService;

/**
 * TabFragment that takes care of the user account.
 */
public class AccountFragment extends Fragment {

    private EditText newPasswordEditText;
    private EditText newPasswordConfirmationEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button uploadButton = (Button) view.findViewById(R.id.saveButton);
        newPasswordEditText = (EditText) view.findViewById(R.id.newPasswordConfirm);
        newPasswordConfirmationEditText = (EditText) view.findViewById(R.id.newPasswordConfirmationEditText);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback<Boolean> callback = new Callback<Boolean>() {
                    @Override
                    public void process(Boolean result) {
                        if (result) {
                            TabHost tabHost = (TabHost) getActivity().findViewById(R.id.homeTabHost);

                            tabHost.setCurrentTab(0);
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Problem updating password", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                UpdatePasswordService updatePasswordService = new UpdatePasswordService(callback);
                String password = newPasswordEditText.getText().toString();
                String confirmation = newPasswordConfirmationEditText.getText().toString();

                if (password.equals(confirmation)) {
                    updatePasswordService.equals(password);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Passwords does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

}
