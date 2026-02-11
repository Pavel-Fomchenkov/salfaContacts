package com.fpavel.salfaContacts.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Contacts manager API")
//                )
//                .addSecurityItem(new SecurityRequirement().addList("jwtToken"))
//                .components(new Components()
//                        .addSecuritySchemes("jwtToken",
//                                new SecurityScheme()
//                                        .name("jwtToken")
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                        )
                );
    }
}