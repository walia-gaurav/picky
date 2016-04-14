package org.cmu.picky.resources;

import com.sun.jersey.api.core.ResourceContext;
import org.cmu.picky.model.auth.PickyServerPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.*;

public class BaseResource {

    private final static Logger logger = LoggerFactory.getLogger(BaseResource.class);

    public static final CacheControl NO_CACHE;
    static {
        CacheControl cc = new CacheControl();
        cc.setNoCache(true);
        NO_CACHE = cc;
    }

    @Context
    protected ResourceContext resourceCtx;
    @Context
    protected UriInfo uriInfo;
    @Context
    protected PickyServerPrincipal principal;
    @Context
    protected HttpServletRequest httpServletRequest;
    @Context
    protected Request request;

}
