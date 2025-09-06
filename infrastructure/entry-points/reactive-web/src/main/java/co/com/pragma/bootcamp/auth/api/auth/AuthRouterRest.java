package co.com.pragma.bootcamp.auth.api.auth;

import co.com.pragma.bootcamp.auth.api.error.ErrorFilter;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/login",
                    method = RequestMethod.POST,
                    beanClass = AuthHandler.class,
                    beanMethod = "loginUser"
            )
    })
    public RouterFunction<ServerResponse> authRouterFunction(AuthHandler handler, ErrorFilter errorFilter) {
        return route(POST("/api/v1/login"), handler::loginUser)
                .filter(errorFilter);
    }
}
