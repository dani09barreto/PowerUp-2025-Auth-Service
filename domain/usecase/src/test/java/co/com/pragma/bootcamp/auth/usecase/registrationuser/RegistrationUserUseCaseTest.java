package co.com.pragma.bootcamp.auth.usecase.registrationuser;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
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

    @InjectMocks
    private RegistrationUserUseCase registrationUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        User user = createValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.save(user)).thenReturn(Mono.just(user));

        StepVerifier.create(registrationUserUseCase.registerUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        User user = createValidUser();
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(Mono.just(true));

        StepVerifier.create(registrationUserUseCase.registerUser(user))
                .expectError(IllegalArgumentException.class)
                .verify();

        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(user);
    }

    @Test
    void testValidateUser_InvalidFields() {
        User user = new User();
        assertThrows(IllegalArgumentException.class, () -> registrationUserUseCase.validateUser(user));
    }

    private User createValidUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(LocalDate.parse("1990-01-01"));
        user.setAddress("123 Main St");
        user.setPhone("1234567890");
        user.setIdentificationNumber("123456789");
        user.setEmail("john.doe@example.com");
        user.setBaseSalary(BigDecimal.valueOf(5000000.0));
        return user;
    }
}