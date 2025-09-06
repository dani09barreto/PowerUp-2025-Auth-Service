package co.com.pragma.bootcamp.auth.api.auth;

import co.com.pragma.bootcamp.auth.api.dto.TokenResponse;
import co.com.pragma.bootcamp.auth.api.dto.UserLoginRequest;
import co.com.pragma.bootcamp.auth.api.error.ApiError;
import co.com.pragma.bootcamp.auth.model.role.gateways.IRoleRepository;
import co.com.pragma.bootcamp.auth.model.token.Token;
import co.com.pragma.bootcamp.auth.model.token.gateways.TokenRepository;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserCredentialsException;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
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
public class AuthHandler {

    private final IUserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final IRoleRepository roleRepository;

    @Operation(
            summary = "Autenticar usuario",
            description = "Este endpoint permite autenticar un usuario en el sistema",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del usuario a autenticar",
                    content = @Content(schema = @Schema(implementation = UserLoginRequest.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario autenticado exitosamente",
                            content = @Content(schema = @Schema(implementation = TokenResponse.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Credenciales inválidas",
                            content = @Content(schema = @Schema(implementation = ApiError.class))
                    )

            }
    )

    public Mono<ServerResponse> loginUser(ServerRequest serverRequest) {

        log.info("Received request to login user");

        return serverRequest.bodyToMono(UserLoginRequest.class)
                .flatMap(userLoginRequest -> this.login(userLoginRequest.email(), userLoginRequest.password()))
                .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse));
    }

    private Mono<Token> login(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> roleRepository.getRoleById(user.getRole().getId())
                        .map(role -> {
                            user.setRole(role);
                            return user;
                        }))
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with email: " + email)))
                .filter(user -> tokenRepository.passwordEncoderMatches(password, user.getPassword()))
                .map(tokenRepository::generateToken)
                .map(Token::new)
                .log()
                .switchIfEmpty(Mono.error(new InvalidUserCredentialsException("Invalid credentials")));
    }
}
