package edu.cmu.jsphdev.picky.ws.remote.services;

import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;
import edu.cmu.jsphdev.picky.ws.remote.interfaces.UserServiceInterface;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.user.LoginRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.user.LogoutRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.user.SignUpRequest;
import edu.cmu.jsphdev.picky.ws.remote.services.requests.user.UpdatePasswordRequest;

public class UserService implements UserServiceInterface {

    public void login(String username, String password, Callback<User> callback) {
        LoginRequest loginRequest = new LoginRequest(callback);

        loginRequest.execute(username, password);
    }

    @Override
    public void logout(Callback<Boolean> callback) {
        LogoutRequest logoutRequest = new LogoutRequest(callback);

        logoutRequest.execute();
    }

    @Override
    public void signUp(String username, String password, Callback<User> callback) {
        SignUpRequest signUpRequest = new SignUpRequest(callback);

        signUpRequest.execute(username, password);
    }

    @Override
    public void updatePassword(String password, Callback<Boolean> callback) {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(callback);

        updatePasswordRequest.execute(password);
    }

}
