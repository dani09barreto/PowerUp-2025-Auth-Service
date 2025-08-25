package co.com.pragma.bootcamp.auth.api;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    beanClass = UserHandler.class,
                    beanMethod = "registerUser"
            )
    })
    public RouterFunction<ServerResponse> routerFunction(UserHandler handler) {
        return route(POST("/api/v1/usuarios"), handler::registerUser);
    }
}
