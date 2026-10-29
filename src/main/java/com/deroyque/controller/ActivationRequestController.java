package com.deroyque.controller;

import com.deroyque.domain.RequestStatus;
import com.deroyque.dto.ActivationResponse;
import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.service.ActivationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
public class ActivationRequestController {

    private final ActivationService service;

    public ActivationRequestController(ActivationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ActivationResponse create(@Valid @RequestBody CreateActivationRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ActivationResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public Page<ActivationResponse> findAll(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) String customerId,
            Pageable pageable) {

        return service.findAll(status, customerId, pageable);
    }

}
