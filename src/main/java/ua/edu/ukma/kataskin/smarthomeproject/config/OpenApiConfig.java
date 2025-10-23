package ua.edu.ukma.kataskin.smarthomeproject.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Smart Home API",
                version = "v1"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local")
        },
        tags = {
                @Tag(name = "Devices", description = "Operations for managing devices")
        }
)
@Configuration
public class OpenApiConfig {
}
