package edu.cmu.jsphdev.picky.ws.remote.interfaces;

public interface UserWebServiceInterface {

    String login(String username, String password);

    void logout();

    boolean updatePassword(String newPassword);

}
