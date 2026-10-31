package com.deroyque.integration;

import com.deroyque.dto.CreateActivationRequest;
import com.deroyque.repository.ActivationRequestRepository;
import com.deroyque.service.ActivationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class ConcurrentDuplicateRequestIT {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("activation_test")
                    .withUsername("postgres")
                    .withPassword("1234");

    @Autowired
    private ActivationService activationService;

    @Autowired
    private ActivationRequestRepository repository;

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateOnlyOneRecordForSameExternalId() throws Exception {

        String externalId = "EXT-CONCURRENT";

        int numberOfThreads = 10;

        ExecutorService executor =
                Executors.newFixedThreadPool(numberOfThreads);

        CountDownLatch startLatch = new CountDownLatch(1);

        List<Future<?>> futures = new ArrayList<>();

        try {

            for (int i = 0; i < numberOfThreads; i++) {

                futures.add(
                        executor.submit(() -> {

                            try {
                                startLatch.await();

                                activationService.create(
                                        new CreateActivationRequest(
                                                externalId,
                                                "CLIENTE-01",
                                                "PRODUTO-01"
                                        )
                                );

                            } catch (Exception ignored) {
                                // Duplicate requests may fail.
                            }
                        })
                );
            }

            startLatch.countDown();

            for (Future<?> future : futures) {
                future.get();
            }

        } finally {
            executor.shutdown();
        }

        long records = repository.count();

        assertEquals(1, records);
    }
}