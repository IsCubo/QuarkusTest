package org.acme.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.acme.domain.TypeMethod;
import org.acme.domain.TypePayment;
import org.acme.dto.PaymentRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class PaymentServiceValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testAmountValidation() {
        // Arrange: Monto inválido (0.0)
        PaymentRequest request = new PaymentRequest(
                "REF-INVALID", 1L, 0.0, TypePayment.USD, TypeMethod.CARD
        );

        // Act
        Set<ConstraintViolation<PaymentRequest>> violations = validator.validate(request);

        // Assert
        Assertions.assertFalse(violations.isEmpty(), "Debería haber violaciones de validación");
        boolean hasAmountError = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("amount"));
        Assertions.assertTrue(hasAmountError, "Debería fallar la validación del campo 'amount'");
    }
}