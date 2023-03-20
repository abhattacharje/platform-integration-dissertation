package com.compliance.documentgenerator.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentGeneratorException extends RuntimeException {

    private HttpStatusCode statusCode;
    private String message;
}
