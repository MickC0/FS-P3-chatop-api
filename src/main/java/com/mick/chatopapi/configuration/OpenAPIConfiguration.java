package com.mick.chatopapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${chatop.openapi.dev-url}")
    private String devUrl;

    @Value("${chatop.openapi.prod-url}")
    private String prodUrl;

/*    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("ChatOp API URL in development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("ChatOp API URL in production environment");

        Info info = new Info()
                .title("ChatOp API")
                .description("ChatOp API is the backend for a rental management application. " +
                        "It handles user management, rentals, and messages exchanged between users.")
                .version("1.0");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .name("Bearer")
                .bearerFormat("JWT")
                .scheme("Bearer");
        return new OpenAPI().addSecurityItem(securityScheme).info(info).servers(List.of(devServer, prodServer));
    }*/

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(new Server().url(devUrl).description("ChatOp API URL in development environment"),
                        new Server().url(prodUrl).description("ChatOp API URL in production environment")))
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
