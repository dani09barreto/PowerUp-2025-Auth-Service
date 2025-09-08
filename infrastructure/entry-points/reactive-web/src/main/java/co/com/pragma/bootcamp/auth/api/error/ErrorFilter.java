package co.com.pragma.bootcamp.auth.api.error;

import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserCredentialsException;
import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserDataException;
import co.com.pragma.bootcamp.auth.usecase.error.UserAlreadyExistsException;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
public class ErrorFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        return next.handle(request)
                .onErrorResume(InvalidUserDataException.class,
                        ex -> buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request, ex.getStackTrace()))
                .onErrorResume(InvalidUserCredentialsException.class,
                        ex -> buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, ex.getStackTrace()))
                .onErrorResume(UserAlreadyExistsException.class,
                        ex -> buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request, ex.getStackTrace()))
                .onErrorResume(UserNotFoundException.class,
                        ex -> buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request, ex.getStackTrace()))
                .onErrorResume(AuthorizationDeniedException.class,
                        ex -> buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), request, ex.getStackTrace()))
                .onErrorResume(Exception.class,
                        ex -> buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request, ex.getStackTrace()));

    }

    private Mono<ServerResponse> buildErrorResponse(HttpStatus status, String message, ServerRequest request, StackTraceElement[] stackTrace) {
        log.error("Error occurred: {}", message);
        log.debug("Stack trace: ", new Throwable().initCause(new Exception()).fillInStackTrace());
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(message)
                .path(request.path())
                .build();

        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(apiError);
    }
}
