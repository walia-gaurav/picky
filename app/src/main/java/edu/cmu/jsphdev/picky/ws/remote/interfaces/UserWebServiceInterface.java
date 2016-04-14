package edu.cmu.jsphdev.picky.ws.remote.interfaces;

/**
 * Created by walia-mac on 4/14/16.
 */
public interface UserWebServiceInterface {

    String login(String username, String password);

    void logout();

    boolean updatePassword(String newPassword);

}
