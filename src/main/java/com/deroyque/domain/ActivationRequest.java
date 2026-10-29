package com.deroyque.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "activation_requests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_external_id",
                        columnNames = "external_id"
                )
        }
)
public class ActivationRequest {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String externalId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String productCode;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    @Version
    private Long version;

}