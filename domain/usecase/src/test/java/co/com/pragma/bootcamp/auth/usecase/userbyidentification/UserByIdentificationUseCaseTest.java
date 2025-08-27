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

import static org.mockito.Mockito.*;

class UserByIdentificationUseCaseTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserByIdentificationUseCase userByIdentificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
}