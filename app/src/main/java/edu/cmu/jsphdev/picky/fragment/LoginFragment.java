package edu.cmu.jsphdev.picky.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import edu.cmu.jsphdev.picky.R;
import edu.cmu.jsphdev.picky.activity.HomeActivity;
import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.util.CurrentSession;
import edu.cmu.jsphdev.picky.ws.remote.service.LoginService;

/**
 * TabFragment to handle user log-in.
 */
public class LoginFragment extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ImageButton loginButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        usernameEditText = (EditText) view.findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        loginButton = (ImageButton) view.findViewById(R.id.loginButton);

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
                        CurrentSession.setActiveUser(user);
                        Intent intent = new Intent(getActivity(), HomeActivity.class);

                        startActivity(intent);
                    }
                };
                LoginService loginService = new LoginService(callback);

                loginService.execute(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
        return view;
    }

}
