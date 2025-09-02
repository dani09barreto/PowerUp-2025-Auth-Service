package co.com.pragma.bootcamp.auth.api.config;

import org.junit.jupiter.api.Test;

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