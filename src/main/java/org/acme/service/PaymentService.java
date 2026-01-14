package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.PaymentEntity;
import org.acme.domain.Status;
import org.acme.domain.TypeMethod;
import org.acme.domain.TypePayment;
import org.acme.dto.PaymentRequest;
import org.acme.dto.PaymentResponse;
import org.acme.dto.StatusRequest;
import org.acme.exception.ServiceException;
import org.acme.repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse create(PaymentRequest request) {
        if (paymentRepository.find("reference", request.reference()).firstResult() != null) {
            throw new ServiceException("DUPLICATE_REFERENCE", "Payment with reference " + request.reference() + " already exists", 409);
        }

        PaymentEntity entity = new PaymentEntity();
        entity.reference = request.reference();
        entity.customerId = request.customerId();
        entity.amount = request.amount();
        entity.currency = request.currency();
        entity.method = request.method();

        paymentRepository.persist(entity);

        return mapToResponse(entity);
    }

    public List<PaymentResponse> findAll(Status status, Long customerId, LocalDateTime from, LocalDateTime to, int page, int size) {

        String query = "1 = 1";
        Map<String, Object> params = new HashMap<>();

        if (status != null) {
            query += " AND status = :status";
            params.put("status", status);
        }
        if (customerId != null) {
            query += " AND customerId = :customerId";
            params.put("customerId", customerId);
        }
        if (from != null && to != null) {
            query += " AND createdAt BETWEEN :from AND :to";;
            params.put("from", from);
            params.put("to", to);
        }
        query += " ORDER BY createdAt ASC";

        return paymentRepository.find(query, params)
                .page(page, size)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public PaymentResponse findById(Long id) {
        PaymentEntity entity = paymentRepository.findById(id);
        if (entity == null) {
            throw new ServiceException("PAYMENT_NOT_FOUND", "Payment with id " + id + " not found", 404);
        }
        return mapToResponse(entity);
    }

    @Transactional
    public PaymentResponse update(Long id, StatusRequest statusRequest) {
        PaymentEntity entity = paymentRepository.findById(id);
        if (entity == null) {
            throw new ServiceException("PAYMENT_NOT_FOUND", "Payment with id " + id + " not found", 404);
        }

        Status currentStatus = entity.status;
        Status newStatus = statusRequest.status();

        if (newStatus == null) {
            throw new ServiceException("PAYMENT_STATUS_REQUIRED", "Status is required", 400);
        }

        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new ServiceException("PAYMENT_STATUS_INVALID", "Cannot change status from " + currentStatus + " to " + newStatus, 409);
        }

        entity.status = newStatus;
        paymentRepository.persist(entity);

        return mapToResponse(entity);
    }

    @Transactional
    public boolean delete(Long id) {
        return paymentRepository.deleteById(id);
    }

    private PaymentResponse mapToResponse(PaymentEntity entity) {
        return new PaymentResponse(
                entity.id,
                entity.reference,
                entity.customerId,
                entity.amount,
                entity.currency.name(),
                entity.method.name(),
                entity.status != null ? entity.status.name() : null
        );
    }
}
