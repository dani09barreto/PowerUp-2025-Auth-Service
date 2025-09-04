package co.com.pragma.bootcamp.auth.usecase.loginuser;

import co.com.pragma.bootcamp.auth.model.role.Role;
import co.com.pragma.bootcamp.auth.model.role.gateways.IRoleRepository;
import co.com.pragma.bootcamp.auth.model.token.Token;
import co.com.pragma.bootcamp.auth.model.token.gateways.TokenRepository;
import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserCredentialsException;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class LoginUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private IRoleRepository roleRepository;

    @InjectMocks
    private LoginUserUseCase loginUserUseCase;

    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        mockUser = User.builder()
                .id(1L)
                .firstName("Daniel")
                .lastName("Barreto")
                .email("test@example.com")
                .password("encodedPassword")
                .role(mockRole)
                .build();
    }

    @Test
    void loginSuccess() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Mono.just(mockUser));
        when(roleRepository.getRoleById(1L)).thenReturn(Mono.just(mockRole));
        when(tokenRepository.passwordEncoderMatches("1234", "encodedPassword")).thenReturn(true);
        when(tokenRepository.generateToken(any(User.class))).thenReturn("mockToken");

        // Act
        Mono<Token> result = loginUserUseCase.login("test@example.com", "1234");

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(token -> token.token().equals("mockToken"))
                .verifyComplete();
    }

    @Test
    void loginUserNotFound() {
        // Arrange
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Mono.empty());

        // Act
        Mono<Token> result = loginUserUseCase.login("notfound@example.com", "1234");

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof UserNotFoundException &&
                        throwable.getMessage().contains("User not found with email"))
                .verify();
    }

    @Test
    void loginInvalidPassword() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Mono.just(mockUser));
        when(roleRepository.getRoleById(1L)).thenReturn(Mono.just(mockRole));
        when(tokenRepository.passwordEncoderMatches("wrongPass", "encodedPassword")).thenReturn(false);

        // Act
        Mono<Token> result = loginUserUseCase.login("test@example.com", "wrongPass");

        // Assert
        StepVerifier.create(result)
                .expectError(InvalidUserCredentialsException.class)
                .verify();
    }
}
