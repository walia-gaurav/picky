package org.cmu.picky;

import com.sun.jersey.spi.container.ContainerRequest;
import org.cmu.picky.model.auth.PickyServerPrincipal;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class PickyServerSecurityContext implements SecurityContext {
    private PickyServerPrincipal principal;
    private ContainerRequest request;

    public PickyServerSecurityContext(PickyServerPrincipal principal, ContainerRequest request) {
        this.principal = principal;
        this.request = request;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return true;
    }

    @Override
    public boolean isSecure() {
        return request.isSecure();
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.BASIC_AUTH;
    }
}