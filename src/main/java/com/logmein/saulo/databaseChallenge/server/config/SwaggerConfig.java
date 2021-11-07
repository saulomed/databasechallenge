/*
 * Criação : 30/05/2020
 */

package com.logmein.saulo.databaseChallenge.server.config;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Classe de configuração para documentação do swagger
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.logmein.saulo.databaseChallenge.server.resource"))
                .paths(regex("/*.*"))
                .build();
    }


    private ApiInfo apiInfo() {

        return new ApiInfoBuilder()
                .title("SPRING REST API")
                .description("Documentação das APIs REST")
                .contact(new Contact("Saulo Santos", "https://www.linkedin.com/in/saulo-medeiros-santos-16734048/", "saulomed@gmail.com"))
                .build();
    }



}
