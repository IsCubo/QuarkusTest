package org.acme.dto;

public record PaymentResponse(
        Long id,
        String reference,
        Long customerId,
        double amount,
        String currency,
        String method,
        String status
){}
