package edu.cmu.jsphdev.picky.util;

import edu.cmu.jsphdev.picky.entities.User;

public class CurrentSession {

    private static User activeUser;

    public static User getActiveUser() {
        return activeUser;
    }

    public static String getActiveUserToken() {
        return activeUser.getToken();
    }

    public static boolean isTiltActive() {
        return activeUser.isTiltActive();
    }

    public static void setActiveUser(User activeUser) {
        CurrentSession.activeUser = activeUser;
    }

}
