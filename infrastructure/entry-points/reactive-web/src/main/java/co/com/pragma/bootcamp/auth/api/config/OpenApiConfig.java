package co.com.pragma.bootcamp.auth.api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio Autenticación")
                        .description("API REST para registrar usuarios en el sistema (Arquitectura Hexagonal, WebFlux).")
                        .version("v1.0.0")
                        .contact(new Contact().name("Equipo Backend").email("danielf.barreto@outlook.com.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del Proyecto")
                        .url("https://github.com/dani09barreto/PowerUp-2025-Auth-Service"));
    }
}
