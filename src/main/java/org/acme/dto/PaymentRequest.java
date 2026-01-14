package org.acme.dto;

import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.DecimalMin;
import org.acme.domain.TypeMethod;
import org.acme.domain.TypePayment;

public record PaymentRequest(
        @NotNull
        String reference,
        @NotNull
        Long customerId,
        @NotNull
        @DecimalMin("0.01")
        double amount,
        @NotNull
        TypePayment currency,
        @NotNull
        TypeMethod method
){}
