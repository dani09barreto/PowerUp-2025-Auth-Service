package co.com.pragma.bootcamp.auth.api.user;

import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationRequest;
import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationResponse;
import co.com.pragma.bootcamp.auth.api.error.ApiError;
import co.com.pragma.bootcamp.auth.api.mapper.UserDtoMapper;
import co.com.pragma.bootcamp.auth.usecase.registrationuser.IRegistrationUserUseCase;
import co.com.pragma.bootcamp.auth.usecase.userbyidentification.IUserByIdentificationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
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
            summary = "Registrar un nuevo usuario/a",
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
                    @ApiResponse(responseCode = "400", description = "Error de validación en los datos",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    ),
                    @ApiResponse(responseCode = "409", description = "Correo ya registrado",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    )
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'ADVISOR')")
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
            security = { @SecurityRequirement(name = "bearerAuth") },
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

    @Operation(
            summary = "Obtener usuario por id",
            description = "Este endpoint permite obtener el usuario por id.",
            security = { @SecurityRequirement(name = "bearerAuth") },
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "id",
                            description = "The id of the user",
                            required = true,
                            in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH,
                            example = "1"
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
    public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
        Long id = Long.valueOf(serverRequest.pathVariable("id"));
        log.info("Received request to get user by id: {}", id);
        return userByIdentificationUseCase.getUserById(id)
                .map(UserDtoMapper::toUserRegistrationResponse)
                .flatMap(user -> ServerResponse.ok().bodyValue(user));
    }
}
