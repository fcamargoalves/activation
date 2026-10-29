package com.deroyque.controller;

import com.deroyque.dto.ActivationResponse;
import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.service.ActivationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

}
