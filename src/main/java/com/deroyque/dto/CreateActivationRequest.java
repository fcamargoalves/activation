package com.deroyque.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateActivationRequest(
        @NotBlank
        String externalId,

        @NotBlank
        String customerId,

        @NotBlank
        String productCode) {
}
