package com.deroyque.mapper;

import com.deroyque.domain.ActivationRequest;
import com.deroyque.dto.ActivationResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivationMapper {

    public ActivationResponse toResponse(ActivationRequest entity) {
        return new ActivationResponse(
                entity.getId(),
                entity.getExternalId(),
                entity.getCustomerId(),
                entity.getProductCode(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
