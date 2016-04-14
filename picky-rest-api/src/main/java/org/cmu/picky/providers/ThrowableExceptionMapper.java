package org.cmu.picky.providers;

import org.cmu.picky.util.ErrorStore;
import org.cmu.picky.model.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class ThrowableExceptionMapper implements ExceptionMapper<Throwable> {

    private final static Logger logger = LoggerFactory.getLogger(ThrowableExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        String errorId = UUID.randomUUID().toString();

        logger.error("Exception caught in ThrowableExceptionMapper, assigned ID {}", errorId, exception);

        Error error = new Error("SE00000", errorId);
        String message = ErrorStore.getMessage(error.getErrorCode(), error.getArguments());
        error.setMessage(message);

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(error)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }

}
