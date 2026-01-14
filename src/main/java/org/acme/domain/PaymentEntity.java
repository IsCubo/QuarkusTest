package org.acme.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.smallrye.common.constraint.NotNull;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
public class PaymentEntity extends PanacheEntity {

    @NotNull
    @Column(unique = true)
    public String reference;

    @NotNull
    public Long customerId;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    public double amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    public TypePayment currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    public TypeMethod method;

    public LocalDateTime createdAt;

    public Status status;

    @PrePersist
    void create(){
        createdAt = LocalDateTime.now();
        status = Status.PENDING;
    }
}

