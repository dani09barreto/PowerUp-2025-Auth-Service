package co.com.pragma.bootcamp.auth.api;

import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationRequest;
import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationResponse;
import co.com.pragma.bootcamp.auth.api.mapper.UserDtoMapper;
import co.com.pragma.bootcamp.auth.usecase.registrationuser.IRegistrationUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final IRegistrationUserUseCase registrationUserUseCase;

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Este endpoint permite registrar un usuario en el sistema, validando datos como correo electrónico único y salario válido.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del usuario a registrar",
                    content = @Content(schema = @Schema(implementation = UserRegistrationRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario registrado exitosamente",
                            content = @Content(schema = @Schema(implementation = UserRegistrationResponse.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Error de validación en los datos", content = @Content),
                    @ApiResponse(responseCode = "409", description = "Correo ya registrado", content = @Content)
            }
    )
    public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserRegistrationRequest.class)
                .map(UserDtoMapper::toUser)
                .flatMap(registrationUserUseCase::registerUser)
                .map(UserDtoMapper::toUserRegistrationResponse)
                .flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(e.getMessage()));
    }
}
