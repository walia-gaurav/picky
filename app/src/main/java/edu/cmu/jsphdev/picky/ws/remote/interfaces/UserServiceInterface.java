package edu.cmu.jsphdev.picky.ws.remote.interfaces;

import edu.cmu.jsphdev.picky.entities.User;
import edu.cmu.jsphdev.picky.tasks.callbacks.Callback;

/**
 * Different methods needed to interact with Users.
 */
public interface UserServiceInterface {

    /**
     * User login.
     */
    void login(String username, String password, Callback<User> callback);

    /**
     * User logout.
     */
    void logout(Callback<Boolean> callback);

    /**
     * User sign up.
     */
    void signUp(String username, String password, Callback<User> callback);

    /**
     * Update User password.
     */
    void updatePassword(String password, Callback<Boolean> callback);

}
