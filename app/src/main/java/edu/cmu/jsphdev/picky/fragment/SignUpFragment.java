package edu.cmu.jsphdev.picky.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Pattern;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.activity.HomeActivity;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.util.TextValidator;
import edu.cmu.jsphdev.picky.ws.remote.service.SignUpService;

/**
 * TabFragment for signing up.
 */
public class SignUpFragment extends Fragment {

    EditText newUsername;
    EditText newPassword;
    EditText newPasswordConfirm;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);


        newUsername = (EditText) view.findViewById(R.id.newUsername);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        newPasswordConfirm = (EditText) view.findViewById(R.id.newPasswordConfirm);

        ImageButton signUpButton = (ImageButton) view.findViewById(R.id.signupButton);

        newUsername.addTextChangedListener(new TextValidator(newUsername) {
            @Override
            public void validate(TextView textView, String text) {
                // validate username
                if (!isValidUsername(text)) {
                    textView.setError("Username has to be at least 3 characters long");
                }
            }
        });

        newPassword.addTextChangedListener(new TextValidator(newPassword) {
            @Override
            public void validate(TextView textView, String text) {
                // validate passwords
                if (!isValidPassword(text)) {
                    textView.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1" +
                            " symbol");
                }
            }
        });

        newPasswordConfirm.addTextChangedListener(new TextValidator(newPasswordConfirm) {
            @Override
            public void validate(TextView textView, String text) {
                // validate passwords confirmation
                if (!isValidPassword(text)) {
                    textView.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1" +
                            " symbol");
                }
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String pass = newPassword.getText().toString();
                String passConfirm = newPasswordConfirm.getText().toString();


                if (passConfirm.equals(pass)) {

                    Callback<User> callback = new Callback<User>() {
                        @Override
                        public void process(User user) {
                            if (user == null) {
                                Toast.makeText(getActivity().getApplicationContext(), "Username already taken!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            /*
                            Session persistence in SharedPreferences.
                             */
                            CurrentSession.setActiveUser(user);
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences
                                    (getActivity()).edit();
                            editor.putString("existingUser", (new Gson()).toJson(user));
                            editor.apply();
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }
                    };
                    SignUpService signUpService = new SignUpService(callback);
                    signUpService.execute(newUsername.getText().toString(), newPassword.getText().toString());
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Password must match!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }


    /**
     * Validates the username.
     */
    private boolean isValidUsername(String username) {
        if (username != null && username.length() > 3) {
            return true;
        }
        return false;
    }

    /**
     * Validates the password.
     */
    private boolean isValidPassword(String password) {
        final String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        return pattern.matcher(password).matches();
    }
}
