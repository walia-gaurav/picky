package edu.cmu.jsphdev.picky.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.util.TextValidator;
import edu.cmu.jsphdev.picky.ws.remote.service.UpdatePasswordService;

/**
 * TabFragment that takes care of the user account.
 */
public class AccountFragment extends Fragment {

    private EditText newPasswordEditText;
    private EditText newPasswordConfirmationEditText;
    private CheckBox tiltCheckbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        Button uploadButton = (Button) view.findViewById(R.id.saveButton);
        newPasswordEditText = (EditText) view.findViewById(R.id.newPasswordEditText);
        newPasswordConfirmationEditText = (EditText) view.findViewById(R.id.newPasswordConfirmationEditText);
        tiltCheckbox = (CheckBox) view.findViewById(R.id.tiltCheckBox);

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

        newPasswordEditText.addTextChangedListener(new TextValidator(newPasswordEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!isValidPassword(text)) {
                    textView.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1 symbol");
                }
            }
        });

        newPasswordConfirmationEditText.addTextChangedListener(new TextValidator(newPasswordConfirmationEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!isValidPassword(text)) {
                    textView.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1 symbol");
                }
                if(!text.equals(newPasswordEditText.getText().toString())) {
                    textView.setError("Password must match!");
                }
            }
        });

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

                if (password.equals(null) || confirmation.equals(null)) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Passwords cant be null", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmation)) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Passwords does not match", Toast.LENGTH_LONG).show();
                } else {
                    updatePasswordService.equals(password);
                }
            }
        });
        return view;
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(pass);

        return matcher.matches();
    }

}
