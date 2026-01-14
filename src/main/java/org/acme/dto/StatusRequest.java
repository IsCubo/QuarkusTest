package org.acme.dto;

import io.smallrye.common.constraint.NotNull;
import org.acme.domain.Status;

public record StatusRequest(
        @NotNull
        Status status
){}
