package org.cmu.picky.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.cmu.picky.model.Error;

public class PickyServerWebAppException extends WebApplicationException {
    private static final long serialVersionUID = 6990612164246143007L;

    private Error error;

    public PickyServerWebAppException(Response.Status status, String errorCode, String... arguments) {
        this(Response.status(status).build(), errorCode, arguments);
    }

    public PickyServerWebAppException(Response response, String errorCode, String... arguments) {
        this(response, new Error(errorCode, arguments));
    }

    private PickyServerWebAppException(Response response, Error error) {
        super(response);
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}