package org.cmu.picky.filters.security;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;
import org.cmu.picky.PickyServerSecurityContext;
import org.cmu.picky.exceptions.PickyServerWebAppException;
import org.cmu.picky.model.auth.PickyServerPrincipal;
import org.cmu.picky.services.AuthService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class AuthFilter implements ResourceFilter, ContainerRequestFilter {

    private static final String AUTH_PATH = "auth/login";
    private static final String STATUS_PATH = "status";

    @Context
    private HttpServletRequest httpServletRequest;

    private static AuthService authService;

    public static void init(AuthService _authService) {
        authService = _authService;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        if (request.getPath().startsWith(AUTH_PATH) || request.getPath().startsWith(STATUS_PATH)) {
            return request;
        }
        PickyServerPrincipal principal = authService.getPrincipal(httpServletRequest);

        if (principal != null) {
            request.setSecurityContext(new PickyServerSecurityContext(principal, request));
        } else {
            throw new PickyServerWebAppException(Response.Status.UNAUTHORIZED, "AE00001");
        }
        return request;
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

}
