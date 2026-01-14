package org.acme.service;

import org.acme.domain.Status;
import org.acme.dto.StatusRequest;
import org.acme.exception.ServiceException;
import org.acme.repository.PaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentService paymentService;

    @Test
    public void testUpdateStatusTransitionError() {
        // Arrange
        Long paymentId = 1L;

        StatusRequest status = new StatusRequest(Status.APPROVED);

        // Act & Assert
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
            paymentService.update(paymentId, status);
        });

        Assertions.assertEquals("PAYMENT_NOT_FOUND", exception.getCode());
        Assertions.assertEquals(404, exception.getStatusCode());
    }
}
