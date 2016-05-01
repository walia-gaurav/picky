package org.cmu.picky.services;

import org.cmu.picky.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Service to handle authentication.
 */
public class AuthService  {

    private static final String AUTH_HEADER = "X-Auth-Token";
    private static final  Logger logger = LoggerFactory.getLogger(AuthService.class);

    private UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get token from header.
     */
    private String getToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AUTH_HEADER);
    }

    /**
     * Search for the token in the header and look for an user that matches with it.
     */
    public User getUser(HttpServletRequest httpServletRequest) {
        String token = getToken(httpServletRequest);

        if (token != null) {
            return userService.getUserByToken(token);
        }
        return null;
    }

}
