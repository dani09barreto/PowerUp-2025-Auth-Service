package co.com.pragma.bootcamp.auth.r2dbc;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.r2dbc.entity.UserEntity;
import co.com.pragma.bootcamp.auth.r2dbc.user.IUserReactiveRepository;
import co.com.pragma.bootcamp.auth.r2dbc.user.UserRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class UserRepositoryAdapterTest {

    @Mock
    private IUserReactiveRepository userReactiveRepository;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        User user = createValidUser();
        UserEntity userEntity = UserEntity.fromDomain(user);

        when(userReactiveRepository.save(userEntity)).thenReturn(Mono.just(userEntity));

        StepVerifier.create(userRepositoryAdapter.save(user))
                .expectNextMatches(savedUser -> savedUser.getEmail().equals(user.getEmail()))
                .verifyComplete();

        verify(userReactiveRepository, times(1)).save(userEntity);
    }

    @Test
    void testExistsByEmail() {
        String email = "test@example.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);

        when(userReactiveRepository.findByEmail(email)).thenReturn(Mono.just(userEntity));

        StepVerifier.create(userRepositoryAdapter.existsByEmail(email))
                .expectNext(true)
                .verifyComplete();

        verify(userReactiveRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindAll() {
        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("user1@example.com");
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setEmail("user2@example.com");

        when(userReactiveRepository.findAll()).thenReturn(Flux.just(userEntity1, userEntity2));

        StepVerifier.create(userRepositoryAdapter.findAll())
                .expectNextCount(2)
                .verifyComplete();

        verify(userReactiveRepository, times(1)).findAll();
    }

    private User createValidUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setAddress("123 Main St");
        user.setPhone("1234567890");
        user.setIdentificationNumber("123456789");
        return user;
    }
}