package com.deroyque.dto;

import com.deroyque.domain.RequestStatus;

public record RequestFilter(

        RequestStatus status,

        String customerId) {
}
