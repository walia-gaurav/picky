package org.cmu.picky.services;

import org.cmu.picky.model.User;
import org.cmu.picky.model.auth.PickyServerPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class AuthService  {

    private final static Logger logger = LoggerFactory.getLogger(AuthService.class);

    private UserService userService;
    public static final String AUTH_HEADER = "X-Auth-Token";

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    private String getToken(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(AUTH_HEADER);
    }

    public PickyServerPrincipal getPrincipal(HttpServletRequest httpServletRequest) {
        String token = getToken(httpServletRequest);

        if (token != null) {
            User user = userService.getUserByToken(token);

            if (user != null) {
                return new PickyServerPrincipal(user);
            }
        }
        return null;
    }

}
