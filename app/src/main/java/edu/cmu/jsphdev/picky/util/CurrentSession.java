package edu.cmu.jsphdev.picky.util;

import edu.cmu.jsphdev.picky.entities.User;

public class CurrentSession {

    private static User activeUser;

    public static User getActiveUser() {
        return activeUser;
    }

    public static void setActiveUser(User activeUser) {
        CurrentSession.activeUser = activeUser;
    }

}