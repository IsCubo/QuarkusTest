package org.acme.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JacksonExceptionMapper
        implements ExceptionMapper<InvalidFormatException> {

    @Override
    public Response toResponse(InvalidFormatException e) {

        String message = e.getMessage().toLowerCase();
        Object value = e.getValue();

        if (message.contains("status")) {
            return build("INVALID_STATUS", value, "status, Allowed values: PENDING, APPROVED, REJECTED");
        }

        if (message.contains("currency")) {
            return build("INVALID_CURRENCY", value, "currency, Allowed payments: COP, USD, EUR");
        }

        if (message.contains("method")) {
            return build("INVALID_METHOD", value, "method, Allowed methods: CARD, PSE, TRANSFER");
        }

        // fallback genérico
        return build("INVALID_ENUM", value, "field");
    }

    private Response build(String code, Object value, String field) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(
                        code,
                        "Invalid value '" + value + "' for " + field
                ))
                .build();
    }
}


