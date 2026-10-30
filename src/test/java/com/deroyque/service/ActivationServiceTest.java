package com.deroyque.service;

import com.deroyque.domain.ActivationRequest;
import com.deroyque.domain.RequestStatus;
import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.exception.ResourceNotFoundException;
import com.deroyque.mapper.ActivationMapper;
import com.deroyque.repository.ActivationRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivationServiceTest {

    @Mock
    private ActivationRequestRepository repository;

    @Mock
    private ActivationMapper mapper;

    @Mock
    private ProviderActivationService providerService;

    @InjectMocks
    private ActivationService service;

    private ActivationRequest activationRequest;


    @BeforeEach
    void setup() {
        activationRequest = new ActivationRequest();
        activationRequest.setId(UUID.randomUUID());
        activationRequest.setExternalId("EXT-001");
        activationRequest.setCustomerId("CLIENTE-01");
        activationRequest.setProductCode("PRODUTO-01");
        activationRequest.setStatus(RequestStatus.PENDING);
        activationRequest.setCreatedAt(Instant.now());
        activationRequest.setUpdatedAt(Instant.now());
    }

    @Test
    void shouldCreateRequestSuccessfully() {

        CreateActivationRequest request =
                new CreateActivationRequest(
                        "EXT-001",
                        "CLIENTE-01",
                        "PRODUTO-01"
                );

        when(repository.save(any())).thenReturn(activationRequest);
        service.create(request);
        verify(repository, atLeastOnce())
                .save(any());

        verify(providerService)
                .activate(
                        "CLIENTE-01",
                        "PRODUTO-01"
                );
    }

    @Test
    void shouldFindRequestById() {

        when(repository.findById(activationRequest.getId()))
                .thenReturn(Optional.of(activationRequest));

        service.findById(activationRequest.getId());
        verify(repository).findById(activationRequest.getId());
    }

    @Test
    void shouldThrowWhenRequestNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(id)
        );
    }

}
