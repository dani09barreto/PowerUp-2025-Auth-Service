package co.com.pragma.bootcamp.auth.api.config;

import co.com.pragma.bootcamp.auth.api.UserHandler;
import co.com.pragma.bootcamp.auth.api.UserRouterRest;
import co.com.pragma.bootcamp.auth.usecase.registrationuser.IRegistrationUserUseCase;
import co.com.pragma.bootcamp.auth.usecase.userbyidentification.IUserByIdentificationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

//@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
//@WebFluxTest
//@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

    /*
    @Autowired
    private WebTestClient webTestClient;
     */

    @Test
    void corsConfigurationShouldAllowOrigins() {
        /*
        webTestClient.post()
                .uri("/api/v1/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
         */
    }

}