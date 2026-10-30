package com.deroyque.integration;

import org.springframework.stereotype.Component;

@Component
public class MockProviderClient implements ProviderClient {

    @Override
    public ProviderResponse activate(ProviderRequest request) {
        return new ProviderResponse(true, "Activation completed");
    }
}
