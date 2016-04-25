package org.cmu.picky.services;

import org.cmu.picky.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class AuthService  {

    public static final String AUTH_HEADER = "X-Auth-Token";

    private static final  Logger logger = LoggerFactory.getLogger(AuthService.class);

    private UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    private String getToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AUTH_HEADER);
    }

    public User getUser(HttpServletRequest httpServletRequest) {
        String token = getToken(httpServletRequest);

        if (token != null) {
            return userService.getUserByToken(token);
        }
        return null;
    }

}
