package javasharks.excusas_challenge.config;

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
                        .title("Excusas Tech API")
                        .version("0.0.1")
                        .description("API para generar excusas tech, memes y leyes. Documentación generada por springdoc-openapi.")
                        .contact(new Contact().name("Java Sharks").email("team@example.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                );
    }
}
