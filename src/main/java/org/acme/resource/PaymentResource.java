package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Status;
import org.acme.dto.PaymentRequest;
import org.acme.dto.PaymentResponse;
import org.acme.dto.StatusRequest;
import org.acme.repository.PaymentRepository;
import org.acme.service.PaymentService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("/api/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    PaymentService paymentService;
    PaymentRepository paymentRepository;

    @POST
    public Response create(@Valid PaymentRequest paymentRequest) {
        PaymentResponse created = paymentService.create(paymentRequest);
        return Response.created(URI.create("/api/payments/" + created.id())).entity(created).build();
    }


    // Endpoint with filters
    @GET
    public List<PaymentResponse> getAll(
            @QueryParam("status") Status status,
            @QueryParam("customerId") Long customerId,
            @QueryParam("from")LocalDateTime from,
            @QueryParam("to") LocalDateTime to,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size
            ) {

        return paymentService.findAll(status, customerId, from, to, page, size);
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        PaymentResponse payment = paymentService.findById(id);
        if (payment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(payment).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response update(@PathParam("id") Long id, @Valid StatusRequest statusRequest) {
        PaymentResponse updated = paymentService.update(id, statusRequest);
        if (updated == null) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean deleted = paymentService.delete(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
