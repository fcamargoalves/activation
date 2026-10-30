package com.deroyque.domain;

import com.deroyque.exception.InvalidStatusTransitionException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Table(
        name = "activation_requests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_external_id",
                        columnNames = "external_id"
                )
        }
)
@Getter
@Setter
@Entity
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


    public void changeStatus(RequestStatus newStatus) {

        if (status == null) {
            this.status = newStatus;
            return;
        }

        switch (status) {
            case PENDING -> {
                if (newStatus != RequestStatus.PROCESSING) {
                    throw new InvalidStatusTransitionException(
                            "Invalid transition from PENDING to "
                                    + newStatus
                    );
                }
            }
            case PROCESSING -> {
                if (newStatus != RequestStatus.COMPLETED
                        && newStatus != RequestStatus.FAILED) {
                    throw new InvalidStatusTransitionException(
                            "Invalid transition from PROCESSING to "
                                    + newStatus
                    );
                }
            }
            case COMPLETED, FAILED ->
                    throw new InvalidStatusTransitionException(
                            "Terminal state cannot be changed"
                    );
        }

        this.status = newStatus;
    }

}