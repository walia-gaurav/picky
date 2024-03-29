package edu.cmu.jsphdev.picky.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.TextValidator;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.UserServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.UserService;

/**
 * TabFragment that takes care of the user account.
 */
public class UpdatePasswordFragment extends android.support.v4.app.DialogFragment {

    private static final String TAG = UpdatePasswordFragment.class.getSimpleName();

    private EditText newPasswordEditText;
    private EditText newPasswordConfirmationEditText;
    private CheckBox tiltCheckbox;
    private UserServiceInterface userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);
        Button uploadButton = (Button) view.findViewById(R.id.saveButton);
        newPasswordEditText = (EditText) view.findViewById(R.id.newPasswordEditText);
        newPasswordConfirmationEditText = (EditText) view.findViewById(R.id.newPasswordConfirmationEditText);

        userService = new UserService();

        newPasswordEditText.addTextChangedListener(new TextValidator(newPasswordEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!isValidPassword(text)) {
                    textView.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1" +
                            " symbol");
                }
            }
        });

        newPasswordConfirmationEditText.addTextChangedListener(new TextValidator(newPasswordConfirmationEditText) {
            @Override
            public void validate(TextView textView, String text) {
                if (!isValidPassword(text)) {
                    textView.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1" +
                            " symbol");
                }
                if (!text.equals(newPasswordEditText.getText().toString())) {
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
                            getDialog().dismiss();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Password update successful.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Problem updating password", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                String password = newPasswordEditText.getText().toString();
                String confirmation = newPasswordConfirmationEditText.getText().toString();

                if (password.isEmpty() || confirmation.isEmpty()) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Passwords cant be null", Toast.LENGTH_LONG).show();
                } else if (!password.equals(confirmation)) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Passwords does not match", Toast.LENGTH_LONG).show();
                } else {
                    userService.updatePassword(password, callback);
                }
            }
        });

        ((Button) view.findViewById(R.id.cancelButtonPasswordUpdate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

    /**
     * Returns true if the password is valid.
     */
    private boolean isValidPassword(String password) {
        final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        return pattern.matcher(password).matches();
    }

}
