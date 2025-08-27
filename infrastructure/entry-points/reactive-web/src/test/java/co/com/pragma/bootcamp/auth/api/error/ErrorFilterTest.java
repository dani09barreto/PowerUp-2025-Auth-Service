package co.com.pragma.bootcamp.auth.api.error;

import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserDataException;
import co.com.pragma.bootcamp.auth.usecase.error.UserAlreadyExistsException;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class ErrorFilterTest {

    private ErrorFilter errorFilter;

    @Mock
    private ServerRequest serverRequest;

    @Mock
    private HandlerFunction<ServerResponse> handlerFunction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        errorFilter = new ErrorFilter();
    }

    @Test
    void testFilter_InvalidUserDataException() {
        // Arrange
        when(handlerFunction.handle(serverRequest)).thenReturn(Mono.error(new InvalidUserDataException("Invalid data")));

        // Act
        Mono<ServerResponse> response = errorFilter.filter(serverRequest, handlerFunction);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();
    }

    @Test
    void testFilter_UserAlreadyExistsException() {
        // Arrange
        when(handlerFunction.handle(serverRequest)).thenReturn(Mono.error(new UserAlreadyExistsException("User exists")));

        // Act
        Mono<ServerResponse> response = errorFilter.filter(serverRequest, handlerFunction);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.CONFLICT)
                .verifyComplete();
    }

    @Test
    void testFilter_UserNotFoundException() {
        // Arrange
        when(handlerFunction.handle(serverRequest)).thenReturn(Mono.error(new UserNotFoundException("User not found")));

        // Act
        Mono<ServerResponse> response = errorFilter.filter(serverRequest, handlerFunction);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    void testFilter_GenericException() {
        // Arrange
        when(handlerFunction.handle(serverRequest)).thenReturn(Mono.error(new RuntimeException("Generic error")));

        // Act
        Mono<ServerResponse> response = errorFilter.filter(serverRequest, handlerFunction);

        // Assert
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> serverResponse.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }
}