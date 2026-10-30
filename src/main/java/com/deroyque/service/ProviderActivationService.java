package com.deroyque.service;

import com.deroyque.exception.IntegrationException;
import com.deroyque.integration.ProviderClient;
import com.deroyque.integration.ProviderRequest;
import com.deroyque.integration.ProviderResponse;
import org.springframework.stereotype.Service;

@Service
public class ProviderActivationService {

    private final ProviderClient providerClient;

    public ProviderActivationService(ProviderClient providerClient) {
        this.providerClient = providerClient;
    }

    public ProviderResponse activate(String customerId, String productCode) {

        ProviderResponse response =
                providerClient.activate(new ProviderRequest(customerId, productCode));

        if (!response.success()) {
            throw new IntegrationException(response.message());
        }

        return response;
    }

}
