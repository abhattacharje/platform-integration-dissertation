package com.compliance.documentvalidator.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PropertyConfiguration {

    @Value("${secret}")
    private String secret;

    @Value("${docGenerator.validation.url}")
    private String docGeneratorUrl;

    @Value("${docGenerator.validation.api}")
    private String docGeneratorApi;
}
