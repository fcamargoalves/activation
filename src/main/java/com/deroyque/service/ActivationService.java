package com.deroyque.service;

import com.deroyque.domain.ActivationRequest;
import com.deroyque.domain.RequestStatus;
import com.deroyque.dto.ActivationResponse;
import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.exception.DuplicateRequestException;
import com.deroyque.exception.ResourceNotFoundException;
import com.deroyque.mapper.ActivationMapper;
import com.deroyque.repository.ActivationRequestRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;


@Service
public class ActivationService {

    private final ActivationRequestRepository repository;
    private final ActivationMapper mapper;
    private final ProviderActivationService providerService;

    public ActivationService(ActivationRequestRepository repository, ActivationMapper mapper, ProviderActivationService providerService) {
        this.repository = repository;
        this.mapper = mapper;
        this.providerService = providerService;
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

            processActivation(saved);

            return mapper.toResponse(saved);

        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateRequestException("ExternalId already exists");
        }
    }

    @Transactional(readOnly = true)
    public ActivationResponse findById(UUID id) {

        ActivationRequest entity =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Request not found"
                                ));

        return mapper.toResponse(entity);
    }

    @Transactional(readOnly = true)
    public Page<ActivationResponse> findAll(RequestStatus status, String customerId, Pageable pageable) {

        Page<ActivationRequest> requests;

        if (status != null && customerId != null) {
            requests = repository.findByStatusAndCustomerId(status, customerId, pageable);

        } else if (status != null) {
            requests = repository.findByStatus(status, pageable);

        } else if (customerId != null) {
            requests = repository.findByCustomerId(customerId, pageable);

        } else {
            requests = repository.findAll(pageable);
        }

        return requests.map(mapper::toResponse);
    }

    private void processActivation(ActivationRequest request) {

        try {
            //request.setStatus(RequestStatus.PROCESSING);
            request.changeStatus(RequestStatus.PROCESSING);
            repository.save(request);
            providerService.activate(request.getCustomerId(), request.getProductCode());

            //request.setStatus(RequestStatus.COMPLETED);
            request.changeStatus(RequestStatus.COMPLETED);
            request.setUpdatedAt(Instant.now());
            repository.save(request);

        } catch (Exception ex) {
            //request.setStatus(RequestStatus.FAILED);
            request.changeStatus(RequestStatus.FAILED);
            request.setUpdatedAt(Instant.now());
            repository.save(request);
        }
    }
}