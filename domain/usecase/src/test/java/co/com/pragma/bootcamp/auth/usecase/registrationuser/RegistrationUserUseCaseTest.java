package co.com.pragma.bootcamp.auth.usecase.registrationuser;

import co.com.pragma.bootcamp.auth.model.role.Role;
import co.com.pragma.bootcamp.auth.model.role.gateways.IRoleRepository;
import co.com.pragma.bootcamp.auth.model.token.gateways.TokenRepository;
import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserDataException;
import co.com.pragma.bootcamp.auth.usecase.error.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegistrationUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private RegistrationUserUseCase registrationUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        User user = createValidUser();
        when(roleRepository.getRoleByName("ADMIN")).thenReturn(Mono.just(Role.builder().name("ADMIN").build()));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        // Act & Assert
        StepVerifier.create(registrationUserUseCase.registerUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        // Arrange
        User user = createValidUser();
        when(roleRepository.getRoleByName("ADMIN")).thenReturn(Mono.just(Role.builder().name("ADMIN").build()));
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(registrationUserUseCase.registerUser(user))
                .expectError(UserAlreadyExistsException.class)
                .verify();

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void testValidateUser_InvalidFirstName() {
        // Arrange
        User user = createValidUser();
        user.setFirstName(null);

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> registrationUserUseCase.validateUser(user));
    }

    @Test
    void testValidateUser_InvalidLastName() {
        // Arrange
        User user = createValidUser();
        user.setLastName("");

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> registrationUserUseCase.validateUser(user));
    }

    @Test
    void testValidateUser_InvalidEmailFormat() {
        // Arrange
        User user = createValidUser();
        user.setEmail("invalid-email");

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> registrationUserUseCase.validateUser(user));
    }

    @Test
    void testValidateUser_InvalidBaseSalary() {
        // Arrange
        User user = createValidUser();
        user.setBaseSalary(BigDecimal.valueOf(20000000));

        // Act & Assert
        assertThrows(InvalidUserDataException.class, () -> registrationUserUseCase.validateUser(user));
    }

    private User createValidUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setAddress("123 Main St");
        user.setPhone("1234567890");
        user.setIdentificationNumber("123456789");
        user.setEmail("john.doe@example.com");
        user.setPassword("securePassword123");
        user.setRole(Role.builder().name("ADMIN").build());
        user.setBaseSalary(BigDecimal.valueOf(5000000));
        return user;
    }
}