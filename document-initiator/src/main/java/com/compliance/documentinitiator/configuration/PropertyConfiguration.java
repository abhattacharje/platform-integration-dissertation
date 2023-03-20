package com.compliance.documentinitiator.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PropertyConfiguration {

    @Value("${docValidator.base.url}")
    private String docValidatorBaseUrl;

    @Value("${docValidator.userRegistration.api}")
    private String docValidatorUserRegistrationApi;

    @Value("${docValidator.tokenGenerator.api}")
    private String docValidatorTokenGeneratorApi;

    @Value("${docValidator.validation.api}")
    private String docValidatorValidationApi;
}
