package co.com.pragma.bootcamp.auth.api;

import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationRequest;
import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationResponse;
import co.com.pragma.bootcamp.auth.api.mapper.UserDtoMapper;
import co.com.pragma.bootcamp.auth.usecase.registrationuser.IRegistrationUserUseCase;
import co.com.pragma.bootcamp.auth.usecase.userbyidentification.IUserByIdentificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserHandler {

    private final IRegistrationUserUseCase registrationUserUseCase;
    private final IUserByIdentificationUseCase userByIdentificationUseCase;

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
        log.info("Received request to register user");
        return serverRequest.bodyToMono(UserRegistrationRequest.class)
                .map(UserDtoMapper::toUser)
                .flatMap(registrationUserUseCase::registerUser)
                .map(UserDtoMapper::toUserRegistrationResponse)
                .flatMap(savedUser -> ServerResponse.ok().bodyValue(savedUser));
    }

    @Operation(
            summary = "Obtener usuario por número de identificación",
            description = "Este endpoint permite obtener los detalles de un usuario utilizando su número de identificación.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "document",
                            description = "The identification number of the user",
                            required = true,
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                            example = "123456789"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario encontrado exitosamente",
                            content = @Content(schema = @Schema(implementation = UserRegistrationResponse.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Error usuario no existe", content = @Content),
            }
    )
    public Mono<ServerResponse> getUserByDocument(ServerRequest serverRequest) {
        String document = serverRequest.pathVariable("document");
        log.info("Received request to get user by document: {}", document);
        return userByIdentificationUseCase.getUserByIdentification(document)
                .map(UserDtoMapper::toUserRegistrationResponse)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }
}
