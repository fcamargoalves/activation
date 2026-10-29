package com.deroyque.repository;

import com.deroyque.domain.ActivationRequest;
import com.deroyque.domain.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActivationRequestRepository extends JpaRepository<ActivationRequest, UUID> {
    Optional<ActivationRequest> findByExternalId(String externalI);
    Page<ActivationRequest> findByCustomerId(String customerId, Pageable pageable);
    Page<ActivationRequest> findByStatusAndCustomerId(RequestStatus status, String customerId, Pageable pageable);

    Page<ActivationRequest> findByStatus(RequestStatus status, Pageable pageable);
}
