package org.cmu.picky.model.auth;

import org.cmu.picky.model.User;

import java.security.Principal;

public class PickyServerPrincipal implements Principal {

    private User user;

    public PickyServerPrincipal(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

}
