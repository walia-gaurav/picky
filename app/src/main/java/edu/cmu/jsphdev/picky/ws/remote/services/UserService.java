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
        new LoginRequest(callback).execute(username, password);
    }

    @Override
    public void logout(Callback<Boolean> callback) {
        new LogoutRequest(callback).execute();
    }

    @Override
    public void signUp(String username, String password, Callback<User> callback) {
        new SignUpRequest(callback).execute(username, password);
    }

    @Override
    public void updatePassword(String password, Callback<Boolean> callback) {
        new UpdatePasswordRequest(callback).execute(password);
    }

}
