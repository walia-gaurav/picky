package org.cmu.picky.providers;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;
import org.cmu.picky.model.auth.PickyServerPrincipal;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Type;

@Provider
public class PrincipalProvider extends AbstractHttpContextInjectable<PickyServerPrincipal> implements
        InjectableProvider<Context, Type> {
    @Context
    private SecurityContext securityContext;

    @Override
    public PickyServerPrincipal getValue(HttpContext httpContext) {
        return (PickyServerPrincipal) securityContext.getUserPrincipal();
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable getInjectable(ComponentContext componentContext, Context context, Type type) {
        if (type.equals(PickyServerPrincipal.class)) {
            return this;
        }
        return null;
    }
}
