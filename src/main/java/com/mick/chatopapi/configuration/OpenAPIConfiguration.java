package com.mick.chatopapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    private final OpenAPIProperties openAPIProperties;

    public OpenAPIConfiguration(OpenAPIProperties openAPIProperties) {
        this.openAPIProperties = openAPIProperties;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url(openAPIProperties.getDevUrl()).description("ChatOp API URL in development environment"),
                        new Server().url(openAPIProperties.getProdUrl()).description("ChatOp API URL in production environment")))
                .info(new Info()
                        .title("ChatOp API")
                        .description("ChatOp API is the backend for a rental management application. " +
                                "It handles user management, rentals, and messages exchanged between users.")
                        .version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                ;
    }
}
