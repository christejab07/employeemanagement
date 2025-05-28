package com.example.employeemanagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType; // Ensure this import is present
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme; // Ensure this import is present
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration for the Employee Management System API.
 * Configures API metadata and defines the HTTP Basic security scheme for Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Employee Management System API",
                version = "1.0",
                description = "API documentation for managing employees and departments."
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Development Server"
                )
        }
)
@SecurityScheme( // This is likely line 22 where the error occurs
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        description = "Use your username and password for HTTP Basic Authentication."
)
public class OpenApiConfig {
        // No additional methods needed here for basic setup
}