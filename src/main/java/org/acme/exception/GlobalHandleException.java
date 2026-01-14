package org.acme.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalHandleException implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof ServiceException se) {
            return buildResponse(se.getStatusCode(), se.getCode(), se.getMessage());
        }

        if (exception instanceof ConstraintViolationException) {
            return buildResponse(Response.Status.BAD_REQUEST.getStatusCode(), "VALIDATION_ERROR", exception.getMessage());
        }

        if (exception instanceof NotFoundException) {
            return buildResponse(Response.Status.NOT_FOUND.getStatusCode(), "RESOURCE_NOT_FOUND", exception.getMessage());
        }
        
        if (exception instanceof IllegalArgumentException) {
             return buildResponse(Response.Status.BAD_REQUEST.getStatusCode(), "INVALID_ARGUMENT", exception.getMessage());
        }

        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "INTERNAL_SERVER_ERROR", "An unexpected error occurred");
    }

    private Response buildResponse(int status, String code, String message) {
        return Response.status(status)
                .entity(new ErrorResponse(code, message))
                .build();
    }
}
