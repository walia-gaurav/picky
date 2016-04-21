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


                if (newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())) {

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

                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Passwords do not match!", Toast.LENGTH_LONG).show();
                    newPasswordConfirm.setText("");
                }
            }
        });
        return view;
    }
}
