package com.compliance.documentvalidator.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanCreator {

    @Bean
    public RestTemplate getRestTemplateBean(RestTemplateBuilder builder) {

        RestTemplate restTemplate = builder.build();
        return restTemplate;
    }
}
