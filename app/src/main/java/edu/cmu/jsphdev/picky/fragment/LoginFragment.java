package edu.cmu.jsphdev.picky.fragment;


import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.activity.HomeActivity;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.UserServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.UserService;

/**
 * TabFragment to handle user log-in.
 */
public class LoginFragment extends Fragment {

    private static final String TAG = LoginFragment.class.getSimpleName();

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ImageButton loginButton;
    private UserServiceInterface userService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = (EditText) view.findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        loginButton = (ImageButton) view.findViewById(R.id.loginButton);
        userService = new UserService();

        /*
        Takes the user to the HomeActivity on successful login.
         */
        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Callback<User> callback = new Callback<User>() {
                    @Override
                    public void process(User user) {
                        if (user == null) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Invalid username and/or password!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        /*
                        Persisting session in SharedPreferences.
                         */
                        CurrentSession.setActiveUser(user);
                        Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                        editor.putString("existingUser", (new Gson()).toJson(user));
                        editor.apply();
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                    }
                };
                userService.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), callback);
            }
        });
        return view;
    }

}
