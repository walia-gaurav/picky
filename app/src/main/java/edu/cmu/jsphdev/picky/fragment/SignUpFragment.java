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
import edu.cmu.jsphdev.picky.exception.InvalidCredentialGrammarExceptipn;
import edu.cmu.jsphdev.picky.exception.PickyException;
import edu.cmu.jsphdev.picky.exception.UsernameAlreadyPresentException;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.util.TextValidator;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.UserServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.UserService;

/**
 * TabFragment for signing up.
 */
public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getSimpleName();

    private EditText newUsername;
    private EditText newPassword;
    private EditText newPasswordConfirm;
    private UserServiceInterface userService;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        newUsername = (EditText) view.findViewById(R.id.newUsername);
        newPassword = (EditText) view.findViewById(R.id.newPassword);
        newPasswordConfirm = (EditText) view.findViewById(R.id.newPasswordConfirm);
        userService = new UserService();

        ImageButton signUpButton = (ImageButton) view.findViewById(R.id.signupButton);

        newUsername.addTextChangedListener(new TextValidator(newUsername) {
            @Override
            public void validate(TextView textView, String text) {
                // validate username
                try {
                    if (!isValidUsername(text)) {
                        throw new InvalidCredentialGrammarExceptipn(textView, "Username has to be at least 3 " +
                                "characters long!");
                    }
                } catch (PickyException e) {
                    e.fix();
                }
            }
        });

        newPassword.addTextChangedListener(new TextValidator(newPassword) {
            @Override
            public void validate(TextView textView, String text) {
                try {
                    // validate passwords
                    if (!isValidPassword(text)) {
                        throw new InvalidCredentialGrammarExceptipn(textView, "Password must has at least 4 " +
                                "characters contains 1 capital letter, 1 number, 1 symbol");
                    }
                } catch (PickyException e) {
                    e.fix();
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


                if (passConfirm.equals(pass) && !pass.isEmpty()) {

                    Callback<User> callback = new Callback<User>() {
                        @Override
                        public void process(User user) {

                            try {
                                if (user == null) {
                                    throw new UsernameAlreadyPresentException(getActivity().getApplicationContext());
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
                            } catch (PickyException e) {
                                e.fix();
                            }
                        }
                    };
                    userService.signUp(newUsername.getText().toString(), newPassword.getText().toString(), callback);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Passwords should not be empty, and must " +
                                    "always match!",
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
