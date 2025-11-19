package com.example.superviseme.config;

//todo Read the note here before pushing to production

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfiguration {
    /**
     * Creating an OpenAPI to aid in authentication.
     */
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Supervise Me")
                        .description("Supervise me service Spring boot RESTful service using springdoc-openAPI")
                        .termsOfService("terms")
                        .contact(new Contact()
                                .email("oforisml@gmail.com")
                                .name("Developer: Samuel Ofori"))
                        .license(new License().name("Draka"))
                        .version("2.0"));
    }
}