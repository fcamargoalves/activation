package com.deroyque.service;

import com.deroyque.domain.ActivationRequest;
import com.deroyque.domain.RequestStatus;
import com.deroyque.dto.ActivationResponse;
import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.exception.DuplicateRequestException;
import com.deroyque.mapper.ActivationMapper;
import com.deroyque.repository.ActivationRequestRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
public class ActivationService {

    private final ActivationRequestRepository repository;
    private final ActivationMapper mapper;

    public ActivationService(ActivationRequestRepository repository, ActivationMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public ActivationResponse create(CreateActivationRequest request) {
        try {

            ActivationRequest entity = new ActivationRequest();

            entity.setId(UUID.randomUUID());
            entity.setExternalId(request.externalId());
            entity.setCustomerId(request.customerId());
            entity.setProductCode(request.productCode());
            entity.setStatus(RequestStatus.PENDING);
            entity.setCreatedAt(Instant.now());
            entity.setUpdatedAt(Instant.now());

            ActivationRequest saved = repository.save(entity);

            return mapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateRequestException("ExternalId already exists");
        }
    }
}