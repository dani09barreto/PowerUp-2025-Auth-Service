package co.com.pragma.bootcamp.auth.api;

import co.com.pragma.bootcamp.auth.api.error.ErrorFilter;
import co.com.pragma.bootcamp.auth.api.user.UserHandler;
import co.com.pragma.bootcamp.auth.api.user.UserRouterRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserRouterRestTest {

    @Mock
    private UserHandler userHandler;

    @Mock
    private ErrorFilter errorFilter;

    private UserRouterRest userRouterRest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRouterRest = new UserRouterRest();
    }

    @Test
    void testRouterFunction() {
        // Act
        RouterFunction<ServerResponse> routerFunction = userRouterRest.userRouterFunction(userHandler, errorFilter);

        // Assert
        assertNotNull(routerFunction, "The router function should not be null");
    }
}