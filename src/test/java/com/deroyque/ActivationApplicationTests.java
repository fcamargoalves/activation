package com.deroyque;

import com.deroyque.domain.ActivationRequest;
import com.deroyque.domain.RequestStatus;
import com.deroyque.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ActivationApplicationTests {

    @Test
    void shouldAllowPendingToProcessing() {
        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.PENDING);
        request.changeStatus(RequestStatus.PROCESSING);
    }

    @Test
    void shouldAllowProcessingToCompleted() {
        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.PROCESSING);
        request.changeStatus(RequestStatus.COMPLETED);
    }

    @Test
    void shouldAllowProcessingToFailed() {
        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.PROCESSING);
        request.changeStatus(RequestStatus.FAILED);
    }

    @Test
    void shouldNotAllowPendingToCompleted() {
        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.PENDING);
        assertThrows(
                InvalidStatusTransitionException.class,
                () -> request.changeStatus(
                        RequestStatus.COMPLETED
                )
        );
    }

    @Test
    void shouldNotAllowCompletedToPending() {

        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.COMPLETED);
        assertThrows(
                InvalidStatusTransitionException.class,
                () -> request.changeStatus(
                        RequestStatus.PENDING
                )
        );
    }

    @Test
    void shouldNotAllowFailedToProcessing() {

        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.FAILED);

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> request.changeStatus(
                        RequestStatus.PROCESSING
                )
        );
    }

    @Test
    void shouldNotAllowCompletedToFailed() {
        ActivationRequest request = new ActivationRequest();
        request.setStatus(RequestStatus.COMPLETED);

        assertThrows(
                InvalidStatusTransitionException.class,
                () -> request.changeStatus(
                        RequestStatus.FAILED
                )
        );
    }

}
