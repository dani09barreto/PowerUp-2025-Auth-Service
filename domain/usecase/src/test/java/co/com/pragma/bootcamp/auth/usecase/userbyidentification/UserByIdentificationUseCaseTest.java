package co.com.pragma.bootcamp.auth.usecase.userbyidentification;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class UserByIdentificationUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserByIdentificationUseCase userByIdentificationUseCase;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = User.builder()
                .id(1L)
                .firstName("Daniel")
                .lastName("Barreto")
                .email("test@example.com")
                .birthDate(LocalDate.of(1995, 1, 1))
                .baseSalary(BigDecimal.valueOf(5000))
                .build();
    }

    @Test
    void testGetUserByIdentification_UserExists() {
        String identificationNumber = "123456789";
        User user = createValidUser();
        when(userRepository.findByNumberIdentification(identificationNumber)).thenReturn(Mono.just(user));

        StepVerifier.create(userByIdentificationUseCase.getUserByIdentification(identificationNumber))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findByNumberIdentification(identificationNumber);
    }

    @Test
    void testGetUserByIdentification_UserNotFound() {
        String identificationNumber = "987654321";
        when(userRepository.findByNumberIdentification(identificationNumber)).thenReturn(Mono.empty());

        StepVerifier.create(userByIdentificationUseCase.getUserByIdentification(identificationNumber))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(userRepository, times(1)).findByNumberIdentification(identificationNumber);
    }

    private User createValidUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setIdentificationNumber("123456789");
        user.setEmail("john.doe@example.com");
        return user;
    }


    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Mono.just(mockUser));

        StepVerifier.create(userByIdentificationUseCase.getUserById(1L))
                .expectNextMatches(user -> user.getId().equals(1L) &&
                        user.getFirstName().equals("Daniel") &&
                        user.getEmail().equals("test@example.com"))
                .verifyComplete();
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Mono.empty());

        StepVerifier.create(userByIdentificationUseCase.getUserById(99L))
                .expectErrorMatches(error ->
                        error instanceof UserNotFoundException &&
                                error.getMessage().contains("User not found with ID: 99"))
                .verify();
    }
}