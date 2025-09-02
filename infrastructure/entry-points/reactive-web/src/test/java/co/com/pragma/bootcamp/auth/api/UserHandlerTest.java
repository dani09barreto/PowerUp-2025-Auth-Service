package co.com.pragma.bootcamp.auth.api;

import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationRequest;
import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationResponse;
import co.com.pragma.bootcamp.auth.api.mapper.UserDtoMapper;
import co.com.pragma.bootcamp.auth.api.user.UserHandler;
import co.com.pragma.bootcamp.auth.usecase.registrationuser.IRegistrationUserUseCase;
import co.com.pragma.bootcamp.auth.usecase.userbyidentification.IUserByIdentificationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserHandlerTest {

    @Mock
    private IRegistrationUserUseCase registrationUserUseCase;

    @Mock
    private IUserByIdentificationUseCase userByIdentificationUseCase;

    @InjectMocks
    private UserHandler userHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() {
        // Arrange
        UserRegistrationRequest request = generateUserRegistrationRequestData();
        UserRegistrationResponse response = generateUserRegistrationResponseData();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.bodyToMono(UserRegistrationRequest.class)).thenReturn(Mono.just(request));
        when(registrationUserUseCase.registerUser(any())).thenReturn(Mono.just(UserDtoMapper.toUser(request)));
        when(registrationUserUseCase.registerUser(any())).thenReturn(Mono.just(UserDtoMapper.toUser(request)));

        // Act
        Mono<ServerResponse> result = userHandler.registerUser(serverRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(registrationUserUseCase, times(1)).registerUser(any());
    }

    @Test
    void testGetUserByDocument_Success() {
        // Arrange
        String document = "123456789";
        UserRegistrationRequest request = generateUserRegistrationRequestData();
        ServerRequest serverRequest = mock(ServerRequest.class);

        when(serverRequest.pathVariable("document")).thenReturn(document);
        when(userByIdentificationUseCase.getUserByIdentification(document))
                .thenReturn(Mono.just(UserDtoMapper.toUser(request)));

        // Act
        Mono<ServerResponse> result = userHandler.getUserByDocument(serverRequest);

        // Assert
        StepVerifier.create(result)
                .expectNextMatches(serverResponse -> serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(userByIdentificationUseCase, times(1)).getUserByIdentification(document);
    }

    UserRegistrationRequest generateUserRegistrationRequestData(){
        return new UserRegistrationRequest(
               "Daniel",
                "Gomez",
                LocalDate.now(),
                "casa",
                "32322323",
                "1110001100",
                "email@example.com",
                new BigDecimal(1000000)
        );
    }


    UserRegistrationResponse generateUserRegistrationResponseData(){
        return new UserRegistrationResponse(
                1L,
                "Daniel",
                "Gomez",
                LocalDate.now(),
                "casa",
                "32322323",
                "1110001100",
                "email@example.com",
                new BigDecimal(1000000)
        );
    }


}