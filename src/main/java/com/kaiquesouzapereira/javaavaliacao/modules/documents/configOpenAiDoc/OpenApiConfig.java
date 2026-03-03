package com.kaiquesouzapereira.javaavaliacao.modules.documents.configOpenAiDoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String bearerAuth = "bearerAuth";
        final String apiKeyAuth = "apiKeyAuth";
        final String apiKeySecret = "apiKeySecret";

        return new OpenAPI()
                .info(new Info().title("VEXYN API").version("1.0"))

                .addSecurityItem(new SecurityRequirement()
                        .addList(bearerAuth)
                        .addList(apiKeyAuth)
                        .addList(apiKeySecret))

                .components(new Components()

                        .addSecuritySchemes(bearerAuth,
                                new SecurityScheme()
                                        .name(bearerAuth)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))

                        .addSecuritySchemes(apiKeyAuth,
                                new SecurityScheme()
                                        .name("X-API-KEY")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Access key for the VEXYN API"))

                        .addSecuritySchemes(apiKeySecret,
                                new SecurityScheme()
                                        .name("X-API-SECRET")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .description("Access key for the VEXYN API")));
    }
}