package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

public interface UserServiceInterface {

    void login(String username, String password, Callback<User> callback);
    void logout(Callback<Boolean> callback);
    void signUp(String username, String password, Callback<User> callback);
    void updatePassword(String password, Callback<Boolean> callback);
}
