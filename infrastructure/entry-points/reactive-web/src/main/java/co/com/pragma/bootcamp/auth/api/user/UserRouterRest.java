package co.com.pragma.bootcamp.auth.api.user;

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
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    beanClass = UserHandler.class,
                    beanMethod = "registerUser"
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/document/{document}",
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getUserByDocument"
            )
            ,
            @RouterOperation(
                    path = "/api/v1/usuarios/{id}",
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getUserById"
            )
    })
    public RouterFunction<ServerResponse> userRouterFunction(UserHandler handler, ErrorFilter errorFilter) {
        return route(POST("/api/v1/usuarios"), handler::registerUser)
                .andRoute(GET("/api/v1/usuarios/document/{document}"), handler::getUserByDocument)
                .andRoute(GET("/api/v1/usuarios/{id}"), handler::getUserById)
                .filter(errorFilter);
    }
}
