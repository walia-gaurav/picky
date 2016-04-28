package edu.cmu.jsphdev.picky.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.activity.HomeActivity;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
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

        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = newUsername.getText().toString();
                String pass = newPassword.getText().toString();
                String passConfirm = newPasswordConfirm.getText().toString();

                // validate username
                if (!isValidUsername(username)) {
                    newUsername.setError("Username has to be at least 3 characters long");
                }
                // validate passwords
                else if (!isValidPassword(pass)) {
                    newPassword.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1 symbol");
                }

                else if (!isValidPassword(passConfirm)) {
                    newPasswordConfirm.setError("Password must has at least 4 characters contains 1 capital letter, 1 number, 1 symbol");
                }
                else if (!passConfirm.equals(pass)) {
                    newPasswordConfirm.setError("Password must match");
                }
                else {

                    Callback<User> callback = new Callback<User>() {
                        @Override
                        public void process(User user) {
                            if (user == null) {
                                Toast.makeText(getActivity().getApplicationContext(), "Username already taken!",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                            CurrentSession.setActiveUser(user);
                            startActivity(new Intent(getActivity(), HomeActivity.class));
                        }
                    };
                    SignUpService signUpService = new SignUpService(callback);
                    signUpService.execute(newUsername.getText().toString(), newPassword.getText().toString());
                }
            }
        });
        return view;
    }


    // validating password with retype password
    private boolean isValidUsername(String username) {
        if (username != null && username.length() > 3) {
            return true;
        }
        return false;
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
