package edu.cmu.jsphdev.picky.ws.remote.interfaces;

/**
 * Interface to access all user services.
 */
public interface UserWebServiceInterface {

    /**
     * Service to login to the server.
     *
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * Logs out from the server.
     */
    void logout();

    /**
     * Requests to update the password on the server.
     *
     * @param newPassword
     * @return
     */
    boolean updatePassword(String newPassword);

}
