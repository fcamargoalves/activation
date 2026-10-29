package com.deroyque.dto;

import com.deroyque.domain.RequestStatus;

import java.time.Instant;
import java.util.UUID;

public record ActivationResponse(

        UUID id,

        String externalId,

        String customerId,

        String productCode,

        RequestStatus status,

        Instant createdAt,

        Instant updatedAt) {
}